package com.tokopedia.troubleshooter.notification.ui.fragment

import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.NotificationManager.IMPORTANCE_LOW
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.fcmcommon.FirebaseMessagingManager
import com.tokopedia.fcmcommon.di.DaggerFcmComponent
import com.tokopedia.fcmcommon.di.FcmModule
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.di.DaggerTroubleshootComponent
import com.tokopedia.troubleshooter.notification.di.module.TroubleshootModule
import com.tokopedia.troubleshooter.notification.ui.activity.TroubleshootActivity
import com.tokopedia.troubleshooter.notification.ui.adapter.TroubleshooterAdapter
import com.tokopedia.troubleshooter.notification.ui.adapter.factory.TroubleshooterItemFactory
import com.tokopedia.troubleshooter.notification.ui.listener.ConfigItemListener
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigState.*
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigUIView
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigUIView.Companion.importantNotification
import com.tokopedia.troubleshooter.notification.ui.uiview.TickerUIView
import com.tokopedia.troubleshooter.notification.ui.viewmodel.TroubleshootViewModel
import com.tokopedia.troubleshooter.notification.util.gotoNotificationSetting
import com.tokopedia.troubleshooter.notification.util.isNotNull
import com.tokopedia.troubleshooter.notification.util.prefixToken
import kotlinx.android.synthetic.main.fragment_notif_troubleshooter.*
import timber.log.Timber
import javax.inject.Inject

class TroubleshootFragment : BaseDaggerFragment(), ConfigItemListener {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var fcmManager: FirebaseMessagingManager

    private lateinit var viewModel: TroubleshootViewModel

    private val errors = mutableListOf<TickerUIView>()

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        TroubleshooterAdapter(TroubleshooterItemFactory(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(TroubleshootViewModel::class.java)

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

    override fun onResume() {
        adapter.removeTicker()
        super.onResume()
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

    private fun initView() {
        lstConfig?.layoutManager = LinearLayoutManager(context)
        lstConfig?.adapter = adapter
        adapter.addElement(ConfigUIView.items())
    }

    private fun initObservable() {
        viewModel.troubleshoot.observe(viewLifecycleOwner, Observer {
            adapter.updateStatus(PushNotification, it.isTroubleshootSuccess())
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            adapter.addMessage(PushNotification, getString(R.string.notif_token_error))
            adapter.isTroubleshootError()
        })

        viewModel.token.observe(viewLifecycleOwner, Observer { newToken ->
            setUpdateTokenStatus(newToken)
            Timber.d("Troubleshoot Token $newToken")
        })

        viewModel.notificationSetting.observe(viewLifecycleOwner, Observer { status ->
            setNotificationSettingStatus(status)
            Timber.d("Troubleshoot Setting $status")
        })

        viewModel.notificationImportance.observe(viewLifecycleOwner, Observer { status ->
            setNotificationImportanceStatus(status)
            Timber.d("Troubleshoot Importance $status")
        })

        viewModel.notificationRingtoneUri.observe(viewLifecycleOwner, Observer { ringtone ->
            adapter.setRingtoneStatus(ringtone, ringtone != null)
        })

        viewModel.combineTuple(viewModel.token, viewModel.notificationSetting, viewModel.notificationImportance)
                .observe(viewLifecycleOwner, Observer {triple ->
                    Timber.d("Troubleshoot Triple $triple")
                    sendLog(triple)
        })
    }

    private fun sendLog(triple: Triple<String?, Boolean?, Int?>) {
        if(triple.first.isNullOrEmpty() || !triple.second.isNotNull() || !triple.third.isNotNull()) return

        Timber.w("P2#LOG_PUSH_NOTIF#'Troubleshooter';token='%s';setting='%s';importance='%s';",
                triple.first,
                triple.second,
                triple.third)
    }

    private fun setUpdateTokenStatus(newToken: String) {
        val currentToken = fcmManager.currentToken().prefixToken().trim()
        val newTrimToken = newToken.prefixToken().trim()

        val message = if (fcmManager.isNewToken(newToken)) {
            viewModel.updateToken(newToken)
            tokenUpdateMessage(currentToken, newTrimToken)
        } else {
            tokenCurrentMessage(currentToken, newTrimToken)
        }

        adapter.addMessage(PushNotification, message)
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

    private fun setNotificationSettingStatus(notificationEnable: Boolean) {
        adapter.updateStatus(Notification, notificationEnable)

        if (!notificationEnable){
            val message = getString(R.string.notif_ticker_notif_disabled)
            adapter.addTicker(message)
        }
    }

    private fun setNotificationImportanceStatus(importance: Int) {
        val isImportance = importantNotification(importance)
        val hasNotCategory = importance == Int.MAX_VALUE

        if (hasNotCategory) {
            //adapter.hideNotificationChannel()
            return
        }

        when {
            isImportance -> {
                adapter.updateStatus(Channel, isImportance)
            }
            importance != IMPORTANCE_HIGH -> {
                adapter.updateStatus(Channel, false)
                onShowTicker(importance)
            }
        }
    }

    private fun onShowTicker(importance: Int) {
        adapter.addTicker(when (importance) {
            IMPORTANCE_LOW -> getString(
                    R.string.notif_ticker_importance_low,
                    importance
            ).parseAsHtml().trim()
            else -> getString(
                    R.string.notif_ticker_importance_none,
                    importance
            ).parseAsHtml().trim()
        })
    }

    override fun onRingtoneTest(uri: Uri) {
        RingtoneManager.getRingtone(context, uri).play()
    }

    override fun goToNotificationSettings() {
        context.gotoNotificationSetting()
    }

    private fun setupToolbar() {
        activity?.let {
            it as? TroubleshootActivity?
        }.also {
            it?.supportActionBar?.title = screenName
        }
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