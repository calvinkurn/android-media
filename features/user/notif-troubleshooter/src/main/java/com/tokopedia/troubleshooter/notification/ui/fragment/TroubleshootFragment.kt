package com.tokopedia.troubleshooter.notification.ui.fragment

import android.app.NotificationManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.fcmcommon.di.DaggerFcmComponent
import com.tokopedia.fcmcommon.di.FcmModule
import com.tokopedia.kotlin.extensions.view.show
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
import com.tokopedia.troubleshooter.notification.ui.viewmodel.TroubleshootViewModel
import com.tokopedia.troubleshooter.notification.util.gotoNotificationSetting
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.ticker.Ticker
import kotlinx.android.synthetic.main.fragment_notif_troubleshooter.*
import kotlinx.android.synthetic.main.item_notification_ticker.*
import javax.inject.Inject

class TroubleshootFragment : BaseDaggerFragment(), ConfigItemListener {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: TroubleshootViewModel

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        TroubleshooterAdapter(TroubleshooterItemFactory(this))
    }

    private var errorText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(TroubleshootViewModel::class.java)
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

    private fun initView() {
        lstConfig?.layoutManager = LinearLayoutManager(context)
        lstConfig?.adapter = adapter

        adapter.addElement(ConfigUIView.items())
    }

    private fun initObservable() {
        viewModel.troubleshoot.observe(viewLifecycleOwner, Observer {
            adapter.updateStatus(PushNotification, it.isSuccess == 1)
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            adapter.isTroubleshootError()
        })

        viewModel.token.observe(viewLifecycleOwner, Observer {
            if (!isNewToken(it)) {
                adapter.updateTroubleshootMessage("Token sudah terbaru")
            } else {
                val trimmedToken = it.substring(it.length - 8)
                var trimmedPrefToken: String?
                try {
                    trimmedPrefToken = getTokenFromPref()
                    trimmedPrefToken = trimmedPrefToken?.substring(trimmedPrefToken.length - 8)
                } catch (e: Exception) {
                    trimmedPrefToken = ""
                }
                val text = "$trimmedToken \ndari\n $trimmedPrefToken"
                viewModel.updateToken(it)
                adapter.updateTroubleshootMessage("Token baru saja diperbarui\n$text")
            }
        })

        viewModel.notificationSetting.observe(viewLifecycleOwner, Observer {
            setNotificationSettingStatus(it)
        })

        viewModel.notificationImportance.observe(viewLifecycleOwner, Observer {
            setNotificationImportanceStatus(it)
        })

        viewModel.notificationSoundUri.observe(viewLifecycleOwner, Observer {
            adapter.updateStatus(Ringtone, it != null)
        })
    }

    private fun setNotificationSettingStatus(notificationEnable: Boolean) {
        adapter.updateStatus(Notification, notificationEnable)

        if (!notificationEnable){
            ticker?.show()
            errorText = "$errorText\nMohon hidupkan pengaturan notifikasi anda."
            errorText.trimStart()
            txtDescription?.text = errorText
        }

        ticker?.setOnClickListener {
            goToNotificationSettings()
        }
    }

    private fun setNotificationImportanceStatus(importance: Int) {
        val isImportance = importantNotification(importance)

        when {
            importance == Int.MAX_VALUE -> {
                adapter.hideNotificationCategory()
                return
            }
            isImportance -> {
                adapter.updateStatus(Categories, isImportance)
            }
            importance != NotificationManager.IMPORTANCE_HIGH -> {
                adapter.updateStatus(Categories, false)
                onShowTicker(importance)
            }
        }
    }

    private fun onShowTicker(importance: Int) {
        ticker?.show()

        when (importance) {
            NotificationManager.IMPORTANCE_DEFAULT -> {
                errorText = "$errorText\nPengaturan notifikasi anda sudah memenuhi standar."
            }
            NotificationManager.IMPORTANCE_LOW -> {
                errorText = "$errorText\nNilai prioritas \"Notifikasi\": Low ($importance)." +
                        "\nNotifikasi muncul, tapi tidak ada suara." +
                        "\nAtur ke nilai prioritas lebih tinggi." +
                        "\nKlik untuk ke pengaturan."
            }
            else -> {
                errorText = "$errorText\nNilai prioritas \"Notifikasi\": None ($importance)." +
                        "\nNotifikasi tidak muncul." +
                        "\nAtur ke nilai prioritas lebih tinggi."
            }
        }
        errorText.trimStart()
        txtDescription?.text = errorText

        ticker?.setOnClickListener {
            goToNotificationSettings()
        }
    }

    private fun isNewToken(token: String): Boolean {
        val prefToken = getTokenFromPref()
        return prefToken != null && token != prefToken
    }

    private fun getTokenFromPref(): String? {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString("pref_fcm_token", "")
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

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onFlush()
    }

    override fun getScreenName() = SCREEN_NAME

    companion object {
        private const val SCREEN_NAME = "Push Notification Troubleshooter"
        private const val REQ_DELAY = 2000L
    }

}