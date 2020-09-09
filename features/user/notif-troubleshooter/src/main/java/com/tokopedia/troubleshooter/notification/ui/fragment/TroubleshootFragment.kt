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
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.fcmcommon.FirebaseMessagingManager
import com.tokopedia.fcmcommon.di.DaggerFcmComponent
import com.tokopedia.fcmcommon.di.FcmModule
import com.tokopedia.network.utils.ErrorHandler
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
import com.tokopedia.troubleshooter.notification.ui.listener.FooterListener
import com.tokopedia.troubleshooter.notification.ui.state.*
import com.tokopedia.troubleshooter.notification.ui.uiview.*
import com.tokopedia.troubleshooter.notification.ui.state.ConfigState.*
import com.tokopedia.troubleshooter.notification.ui.state.DeviceSettingState.Companion.isPriorityNormalOrHigh
import com.tokopedia.troubleshooter.notification.ui.state.RingtoneState.Normal
import com.tokopedia.troubleshooter.notification.ui.uiview.TickerItemUIView.Companion.ticker
import com.tokopedia.troubleshooter.notification.ui.viewmodel.TroubleshootViewModel
import com.tokopedia.troubleshooter.notification.util.*
import com.tokopedia.troubleshooter.notification.util.TroubleshooterDialog.showInformationDialog
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_notif_troubleshooter.*
import javax.inject.Inject
import com.tokopedia.troubleshooter.notification.ui.state.RingtoneState.Companion.isSilent as isSilent

class TroubleshootFragment : BaseDaggerFragment(), ConfigItemListener, FooterListener {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var fcmManager: FirebaseMessagingManager

    private lateinit var viewModel: TroubleshootViewModel

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        TroubleshooterAdapter(TroubleshooterItemFactory(this, this))
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
        initView()
        initObservable()

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
        viewModel.removeTickers()
    }

    private fun initView() {
        lstConfig?.layoutManager = LinearLayoutManager(context)
        lstConfig?.adapter = adapter
        adapter.status(StatusState.Loading)
        adapter.addElement(ConfigUIView.items())
        adapter.footerMessage(false)
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

        viewModel.dndMode.observe(viewLifecycleOwner, Observer {
            adapter.footerMessage(it)
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
                TroubleshooterTimber.combine(token, notification, device)
                adapter.addWarningTicker(TickerUIView(viewModel.tickerItems))

                if (notification.isTrue() && device.isTrue() && !isSilent(ringtone)) {
                    adapter.status(StatusState.Success)
                } else {
                    adapter.status(StatusState.Warning)
                }
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
                        type = Notification,
                        status = StatusState.Error,
                        message = getString(R.string.notif_ticker_net_error),
                        buttonText = R.string.btn_notif_try_again
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
                            type = Notification,
                            status = StatusState.Warning,
                            message = getString(R.string.notif_us_ticker_warning, inactive.toString()),
                            buttonText = R.string.btn_notif_activation
                    )
                } else {
                    updateConfigStatus(
                            type = Notification,
                            status = StatusState.Error,
                            message = getString(R.string.notif_us_ticker_error),
                            buttonText = R.string.btn_notif_activation
                    )
                }
            }
        }
    }

    private fun deviceSetting(result: Result<DeviceSettingState>) {
        when (result) {
            is Error -> activity?.finish()
            is Success -> {
                if (isPriorityNormalOrHigh(result.data)) {
                    adapter.updateStatus(Device, StatusState.Success)
                } else {
                    updateConfigStatus(
                            type = Device,
                            status = StatusState.Warning,
                            message = getString(R.string.notif_dv_ticker_warning),
                            buttonText = R.string.btn_notif_activation
                    )
                }
            }
        }
    }

    private fun ringtoneSetting(status: Pair<Uri?, RingtoneState>) {
        if (status.second == Normal) {
            adapter.setRingtoneStatus(status.first, StatusState.Success)
        } else {
            updateConfigStatus(
                    type = Ringtone,
                    status = StatusState.Error,
                    message = getString(R.string.notif_rn_ticker_warning),
                    buttonText = R.string.btn_notif_play
            )
        }
    }

    private fun troubleshooterPushNotification(result: Result<NotificationSendTroubleshoot>) {
        when (result) {
            is Success -> {
                val isSuccess = StatusState(result.data.isTroubleshootSuccess())
                adapter.updateStatus(PushNotification, isSuccess)
                saveLastCheckedDate(requireContext())
            }
            is Error -> {
                showToastError(result.cause)
                updateConfigStatus(
                        type = PushNotification,
                        status = StatusState.Error,
                        message = getString(R.string.notif_ticker_net_error),
                        buttonText = R.string.btn_notif_try_again
                )
            }
        }
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

    private fun showToastError(e: Throwable?) {
        view?.let {
            val errorMessage = ErrorHandler.getErrorMessage(it.context, e)
            Toaster.showError(it, errorMessage, Snackbar.LENGTH_LONG)
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
            message: String = "",
            buttonText: Int = 0
    ) {
        val ticker = ticker(
                type,
                message,
                getString(buttonText)
        )
        viewModel.tickers(ticker, status)
        adapter.updateStatus(type, status)
    }

    override fun onInfoClicked() {
        showInformationDialog(TEST_SCREEN_NAME)
    }

    override fun onRingtoneTest(uri: Uri) {
        RingtoneManager.getRingtone(context, uri).play()
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

    override fun getScreenName() = MAIN_SCREEN_NAME

    companion object {
        private const val MAIN_SCREEN_NAME = "Push Notification Troubleshooter"
        private const val TEST_SCREEN_NAME = "Tes push notification"
        private const val REQ_DELAY = 2000L
    }

}