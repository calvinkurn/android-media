package com.tokopedia.troubleshooter.notification.ui.fragment

import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.fcmcommon.FirebaseMessagingManager
import com.tokopedia.fcmcommon.di.DaggerFcmComponent
import com.tokopedia.fcmcommon.di.FcmModule
import com.tokopedia.settingnotif.usersetting.util.CacheManager.saveLastCheckedDate
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.analytics.TroubleshooterTimber
import com.tokopedia.troubleshooter.notification.data.entity.NotificationSendTroubleshoot
import com.tokopedia.troubleshooter.notification.di.DaggerTroubleshootComponent
import com.tokopedia.troubleshooter.notification.di.module.TroubleshootModule
import com.tokopedia.troubleshooter.notification.ui.activity.TroubleshootActivity
import com.tokopedia.troubleshooter.notification.ui.adapter.TroubleshooterAdapter
import com.tokopedia.troubleshooter.notification.ui.adapter.factory.TroubleshooterItemFactory
import com.tokopedia.troubleshooter.notification.ui.listener.ConfigItemListener
import com.tokopedia.troubleshooter.notification.ui.uiview.*
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigState.*
import com.tokopedia.troubleshooter.notification.ui.uiview.RingtoneState.Silent
import com.tokopedia.troubleshooter.notification.ui.uiview.RingtoneState.Vibrate
import com.tokopedia.troubleshooter.notification.ui.uiview.TickerItemUIView.Companion.ticker
import com.tokopedia.troubleshooter.notification.ui.viewmodel.TroubleshootViewModel
import com.tokopedia.troubleshooter.notification.util.*
import com.tokopedia.troubleshooter.notification.ui.state.Error
import com.tokopedia.troubleshooter.notification.ui.state.Result
import com.tokopedia.troubleshooter.notification.ui.state.Success
import kotlinx.android.synthetic.main.fragment_notif_troubleshooter.*
import javax.inject.Inject

class TroubleshootFragment : BaseDaggerFragment(), ConfigItemListener {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var fcmManager: FirebaseMessagingManager

    private lateinit var viewModel: TroubleshootViewModel

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        TroubleshooterAdapter(TroubleshooterItemFactory(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
                this,
                viewModelFactory
        ).get(TroubleshootViewModel::class.java)
        lifecycle.addObserver(viewModel)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context).inflate(
                R.layout.fragment_notif_troubleshooter,
                container,
                false
        ).apply { setupToolbar() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservable()
        initView()

        /*
        * adding interval to request
        * for user experience purpose
        * */
        Handler().postDelayed({
            viewModel.troubleshoot()
        }, REQ_DELAY)
    }

    override fun onResume() {
        super.onResume()
        adapter.removeTicker()
    }

    private fun initView() {
        lstConfig?.layoutManager = LinearLayoutManager(context)
        lstConfig?.adapter = adapter
        adapter.status(StatusState.Loading)
        adapter.addElement(ConfigUIView.items())
        adapter.addElement(FooterUIView())
    }

    private fun initObservable() {
        viewModel.token.observe(viewLifecycleOwner, Observer { newToken ->
            setUpdateTokenStatus(newToken)
            TroubleshooterTimber.token(newToken)
        })

        viewModel.notificationSetting.observe(viewLifecycleOwner, Observer {
            notificationSetting(it)
        })

        viewModel.deviceSetting.observe(viewLifecycleOwner, Observer {
            deviceSetting(it)
        })

        viewModel.notificationRingtoneUri.observe(viewLifecycleOwner, Observer {
            ringtoneSetting(it)
        })

        viewModel.troubleshoot.observe(viewLifecycleOwner, Observer {
            troubleshooterPushNotification(it)
        })

        combineFourth(
                viewModel.token,
                viewModel.notificationSetting,
                viewModel.deviceSetting,
                viewModel.notificationRingtoneUri
        ).observe(viewLifecycleOwner, Observer {
            val token = it.first
            val notification = it.second
            val device = it.third
            val ringtone = it.fourth?.second

            if (token.isNotNull() && notification.isNotNull() && device.isNotNull() && ringtone.isNotNull()) {
                if (notification.isTrue() && device.isTrue() && ringtone.isRinging()) {
                    troubleshooterStatusPassed()
                } else {
                    troubleshooterStatusWarning()
                }

                adapter.addWarningTicker(TickerUIView(viewModel.tickerItems))
                TroubleshooterTimber.combine(token, notification, device)
            }
        })
    }

    private fun notificationSetting(result: Result<UserSettingUIView>) {
        when (result) {
            is Success -> {
                setNotificationSettingStatus(result.data)
                TroubleshooterTimber.notificationSetting(result.data)
            }
            is Error -> {
                updateConfigStatus(
                        Notification,
                        StatusState.Error,
                        getString(R.string.notif_ticker_net_error),
                        R.string.btn_notif_try_again
                )
            }
        }
    }

    private fun setNotificationSettingStatus(userSetting: UserSettingUIView) {
        when {
            userSetting.totalOn == userSetting.notifications -> {
                adapter.updateStatus(Notification, StatusState.Success)
            }
            userSetting.totalOn != userSetting.notifications -> {
                val inactive = userSetting.notifications - userSetting.totalOn
                if (inactive != userSetting.notifications) {
                    updateConfigStatus(
                            Notification,
                            StatusState.Warning,
                            getString(R.string.notif_us_ticker_warning, inactive.toString()),
                            R.string.btn_notif_activation
                    )
                } else {
                    updateConfigStatus(
                            Notification,
                            StatusState.Error,
                            getString(R.string.notif_us_ticker_error),
                            R.string.btn_notif_try_again
                    )
                }
            }
        }
    }

    private fun deviceSetting(result: Result<DeviceSettingState>) {
        when (result) {
            is Error -> activity?.finish()
            is Success -> {
                when (result.data) {
                    is DeviceSettingState.Normal -> adapter.updateStatus(Device, StatusState.Success)
                    is DeviceSettingState.High -> adapter.updateStatus(Device, StatusState.Success)
                    is DeviceSettingState.Low -> {
                        updateConfigStatus(
                                Device,
                                StatusState.Warning,
                                getString(R.string.notif_dv_ticker_warning),
                                R.string.btn_notif_repair
                        )
                    }
                }
            }
        }
    }

    private fun ringtoneSetting(status: Pair<Uri?, RingtoneState>) {
        if (status.second == Vibrate || status.second == Silent) {
            updateConfigStatus(
                    Ringtone,
                    StatusState.Error,
                    getString(R.string.notif_rn_ticker_warning),
                    R.string.btn_notif_play
            )
        } else {
            adapter.setRingtoneStatus(status.first, StatusState.Success)
        }
    }

    private fun troubleshooterPushNotification(result: Result<NotificationSendTroubleshoot>) {
        when (result) {
            is Success -> {
                adapter.updateStatus(PushNotification, isState(
                        result.data.isTroubleshootSuccess()
                ))
                saveLastCheckedDate(requireContext())
            }
            is Error -> {
                updateConfigStatus(
                        PushNotification,
                        StatusState.Error,
                        getString(R.string.notif_ticker_net_error),
                        R.string.btn_notif_try_again
                )
            }
        }
    }

    private fun troubleshooterStatusPassed() {
        adapter.status(StatusState.Success)
    }

    private fun troubleshooterStatusWarning() {
        adapter.status(StatusState.Warning)
    }

    private fun setUpdateTokenStatus(newToken: String) {
        val currentToken = fcmManager.currentToken().prefixToken().trim()
        val newTrimToken = newToken.prefixToken().trim()

        txtToken?.text = if (fcmManager.isNewToken(newToken)) {
            viewModel.updateToken(newToken)
            tokenUpdateMessage(currentToken, newTrimToken)
        } else {
            tokenCurrentMessage(currentToken, newTrimToken)
        }
    }

    private fun tokenCurrentMessage(currentToken: String, newToken: String): String {
        return if (currentToken != newToken) {
            getString(R.string.notif_token_current_different, currentToken, newToken)
        } else {
            getString(R.string.notif_token_current, newToken)
        }
    }

    private fun tokenUpdateMessage(currentToken: String, newToken: String): String {
        return if (currentToken.isNotEmpty() && currentToken != newToken) {
            getString(R.string.notif_token_update, currentToken, newToken)
        } else {
            getString(R.string.notif_token_update_no_prev, newToken)
        }
    }

    private fun setupToolbar() {
        activity?.let {
            it as? TroubleshootActivity?
        }.also {
            it?.supportActionBar?.title = screenName
        }
    }

    private fun updateConfigStatus(
            type: ConfigState,
            status: StatusState,
            message: String,
            buttonText: Int
    ) {
        adapter.updateStatus(type, status)
        viewModel.tickers(ticker(type, message, getString(buttonText)))
    }

    override fun onRingtoneTest(uri: Uri) {
        RingtoneManager.getRingtone(context, uri).play()
    }

    override fun goToNotificationSettings() {
        context.gotoNotificationSetting()
    }

    override fun initInjector() {
        val fcmComponent = DaggerFcmComponent.builder()
                .fcmModule(FcmModule(requireContext()))
                .build()

        DaggerTroubleshootComponent.builder()
                .fcmComponent(fcmComponent)
                .troubleshootModule(TroubleshootModule(requireContext()))
                .build()
                .inject(this)
    }

    override fun getScreenName() = SCREEN_NAME

    companion object {
        private const val SCREEN_NAME = "Push Notification Troubleshooter"
        private const val REQ_DELAY = 2000L
    }

}