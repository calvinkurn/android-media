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
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.config.GlobalConfig
import com.tokopedia.fcmcommon.FirebaseMessagingManager
import com.tokopedia.fcmcommon.di.DaggerFcmComponent
import com.tokopedia.fcmcommon.di.FcmModule
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.analytics.TroubleshooterAnalytics.trackClearCacheClicked
import com.tokopedia.troubleshooter.notification.analytics.TroubleshooterAnalytics.trackImpression
import com.tokopedia.troubleshooter.notification.analytics.TroubleshooterTimber
import com.tokopedia.troubleshooter.notification.data.entity.NotificationSendTroubleshoot
import com.tokopedia.troubleshooter.notification.databinding.FragmentNotifTroubleshooterBinding
import com.tokopedia.troubleshooter.notification.di.DaggerTroubleshootComponent
import com.tokopedia.troubleshooter.notification.di.module.TroubleshootModule
import com.tokopedia.troubleshooter.notification.ui.activity.TroubleshootActivity
import com.tokopedia.troubleshooter.notification.ui.adapter.TroubleshooterAdapter
import com.tokopedia.troubleshooter.notification.ui.adapter.factory.TroubleshooterItemFactory
import com.tokopedia.troubleshooter.notification.ui.listener.ConfigItemListener
import com.tokopedia.troubleshooter.notification.ui.listener.FooterListener
import com.tokopedia.troubleshooter.notification.ui.state.*
import com.tokopedia.troubleshooter.notification.ui.state.ConfigState.*
import com.tokopedia.troubleshooter.notification.ui.state.RingtoneState.Companion.isSilent
import com.tokopedia.troubleshooter.notification.ui.state.RingtoneState.Normal
import com.tokopedia.troubleshooter.notification.ui.uiview.TickerItemUIView.Companion.ticker
import com.tokopedia.troubleshooter.notification.ui.uiview.TickerUIView
import com.tokopedia.troubleshooter.notification.ui.uiview.UserSettingUIView
import com.tokopedia.troubleshooter.notification.ui.viewmodel.TroubleshootViewModel
import com.tokopedia.troubleshooter.notification.util.CacheManager.saveLastCheckedDate
import com.tokopedia.troubleshooter.notification.util.ClearCacheUtil.showClearCache
import com.tokopedia.troubleshooter.notification.util.TroubleshooterDialog.showInformationDialog
import com.tokopedia.troubleshooter.notification.util.combineFourth
import com.tokopedia.troubleshooter.notification.util.isNotNull
import com.tokopedia.troubleshooter.notification.util.isTrue
import com.tokopedia.troubleshooter.notification.util.prefixToken
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import javax.inject.Inject

class TroubleshootFragment : BaseDaggerFragment(), ConfigItemListener, FooterListener {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var fcmManager: FirebaseMessagingManager
    @Inject lateinit var userSession: UserSessionInterface

    private lateinit var viewModel: TroubleshootViewModel
    private val binding by viewBinding(FragmentNotifTroubleshooterBinding::bind)

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        TroubleshooterAdapter(TroubleshooterItemFactory(this, this))
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(
            R.layout.fragment_notif_troubleshooter,
            container,
            false
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
                this,
                viewModelFactory
        ).get(TroubleshootViewModel::class.java)
        lifecycle.addObserver(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()

        initView()
        initObservable()
        troubleshooterStatus()

        /*
        * adding interval to request
        * for user experience purpose
        * */
        Handler().postDelayed({
            viewModel.troubleshoot()
        }, REQ_DELAY)

        // impression tracker
        trackImpression(userSession.userId, userSession.shopId)
    }

    override fun onResume() {
        super.onResume()
        viewModel.removeTickers()
    }

    private fun initView() {
        binding?.lstConfig?.layoutManager = LinearLayoutManager(context)
        binding?.lstConfig?.adapter = adapter
        adapter.status(StatusState.Loading)
        adapter.addElement(ConfigUIView.items())
        adapter.footerMessage(false)
    }

    private fun initObservable() {
        viewModel.notificationStatus.observe(viewLifecycleOwner, { isNotificationEnabled ->
            if (!GlobalConfig.isSellerApp()) {
                if (!isNotificationEnabled) activity?.finish()
            }
        })

        viewModel.token.observe(viewLifecycleOwner, {
            getNewToken(it)
        })

        viewModel.notificationSetting.observe(viewLifecycleOwner, {
            notificationSetting(it)
        })

        viewModel.deviceSetting.observe(viewLifecycleOwner, {
            deviceSetting(it)
        })

        viewModel.notificationRingtoneUri.observe(viewLifecycleOwner, {
            ringtoneSetting(it)
        })

        viewModel.troubleshootSuccess.observe(viewLifecycleOwner, {
            val isSuccess = StatusState(it.isTroubleshootSuccess())
            adapter.updateStatus(PushNotification, isSuccess)
        })

        viewModel.troubleshootError.observe(viewLifecycleOwner, {
            adapter.updateStatus(PushNotification, StatusState.Error)
            showToastError()
        })

        viewModel.dndMode.observe(viewLifecycleOwner, {
            adapter.footerMessage(it)
        })
    }

    private fun troubleshooterStatus() {
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

            if (viewModel.tickerItems.isNotEmpty()) {
                adapter.addWarningTicker(TickerUIView(viewModel.tickerItems))
            } else {
                adapter.removeTickers()
            }

            if (notification.isNotNull() && device.isNotNull() && ringtone.isNotNull() && token.isNotNull()) {
                TroubleshooterTimber.combine(token, notification, device)

                if (notification.isTrue() && device.isTrue() && !isSilent(ringtone) && token.isTrue()) {
                    adapter.status(StatusState.Success)
                } else {
                    adapter.status(StatusState.Warning)
                }
            }
        })
    }

    private fun getNewToken(result: Result<String>) {
        when (result) {
            is Success -> {
                setUpdateTokenStatus(result.data)
                TroubleshooterTimber.token(result.data)
                saveLastCheckedDate(requireContext())
            }
            is Fail -> {
                setUpdateTokenStatus("")
            }
        }
    }

    private fun notificationSetting(result: Result<UserSettingUIView>) {
        when (result) {
            is Success -> {
                setNotificationSettingStatus(result.data)
                TroubleshooterTimber.notificationSetting(result.data)
            }
            is Fail -> {
                adapter.updateStatus(Notification, StatusState.Error)
                showToastError()
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
            is Fail -> {
                updateConfigStatus(
                        type = Device,
                        status = StatusState.Error,
                        message = getString(R.string.notif_dv_none_ticker_warning),
                        buttonText = R.string.btn_notif_activation
                )
            }
            is Success -> {
                if (result.data == DeviceSettingState.High) {
                    adapter.updateStatus(Device, StatusState.Success)
                } else {
                    updateConfigStatus(
                            type = Device,
                            status = StatusState.Warning,
                            message = getString(R.string.notif_dv_low_ticker_warning),
                            buttonText = R.string.btn_notif_choose
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

    private fun setUpdateTokenStatus(newToken: String) {
        val currentToken = fcmManager.currentToken().prefixToken().trim()
        val newTrimToken = newToken.prefixToken().trim()
        binding?.txtToken?.show()

        binding?.txtToken?.text = when {
            newToken.isEmpty() -> {
                getString(R.string.notif_error_update_token)
            }
            fcmManager.isNewToken(newToken) -> {
                tokenUpdateMessage(currentToken, newTrimToken)
            }
            else -> {
                tokenCurrentMessage(currentToken, newTrimToken)
            }
        }

        if (newToken.isNotEmpty() && fcmManager.isNewToken(newToken)) {
            viewModel.updateToken(newToken)
        }
    }

    private fun tokenCurrentMessage(currentToken: String, newToken: String): String {
        return if (currentToken != newToken) {
            getString(R.string.notif_token_current_different, currentToken, newToken)
        } else {
            getString(R.string.notif_token_current, currentToken)
        }
    }

    private fun tokenUpdateMessage(currentToken: String, newToken: String): String {
        return if (currentToken.isNotEmpty() && currentToken != newToken) {
            getString(R.string.notif_token_update, currentToken, newToken)
        } else {
            getString(R.string.notif_token_update_no_prev, newToken)
        }
    }

    private fun showToastError() {
        view?.let {
            val errorMessage = getString(R.string.notif_network_issue)
            Toaster.make(it, errorMessage, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
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
        viewModel.tickers(ticker)
        adapter.updateStatus(type, status)
    }

    override fun onInfoClicked() {
        showInformationDialog(TEST_SCREEN_NAME)
    }

    override fun onClearCacheClicked() {
        showClearCache(context)
        trackClearCacheClicked(
                userSession.userId,
                userSession.shopId
        )
    }

    override fun onRingtoneTest(uri: Uri) {
        RingtoneManager.getRingtone(context, uri)?.play()
    }

    override fun initInjector() {
        val fcmComponent = DaggerFcmComponent.builder()
                .fcmModule(FcmModule(requireContext()))
                .build()
        val baseAppComponent = (requireActivity().application as BaseMainApplication).baseAppComponent

        DaggerTroubleshootComponent.builder()
                .fcmComponent(fcmComponent)
                .baseAppComponent(baseAppComponent)
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