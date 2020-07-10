package com.tokopedia.troubleshooter.notification.ui.fragment

import android.app.NotificationManager
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.provider.Settings
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
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.di.DaggerTroubleshootComponent
import com.tokopedia.troubleshooter.notification.di.module.TroubleshootModule
import com.tokopedia.troubleshooter.notification.ui.activity.TroubleshootActivity
import com.tokopedia.troubleshooter.notification.ui.adapter.TroubleshooterAdapter
import com.tokopedia.troubleshooter.notification.ui.adapter.factory.TroubleshooterItemFactory
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigState
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigUIView
import com.tokopedia.troubleshooter.notification.ui.viewmodel.TroubleshootViewModel
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.ticker.Ticker
import kotlinx.android.synthetic.main.fragment_notif_troubleshooter.*
import javax.inject.Inject
import com.tokopedia.abstraction.common.utils.view.MethodChecker.getDrawable as drawable
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigState.Categories as Categories
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigState.Notification as Notification
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigState.PushNotification as PushNotification
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigState.Ringtone as Ringtone

class TroubleshootFragment : BaseDaggerFragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: TroubleshootViewModel

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        TroubleshooterAdapter(TroubleshooterItemFactory())
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
            //onIconStatus(false)
        })

        viewModel.token.observe(viewLifecycleOwner, Observer {
            cardToken?.show()
            textToken?.show()
            if (!isNewToken(it)) {
                labelToken?.setLabel("Token sudah terbaru")
                labelToken?.setLabelType(Label.GENERAL_LIGHT_GREEN)
                textToken?.text = it.substring(it.length - 8)
            } else {
                viewModel.updateToken(it)
                labelToken?.setLabel("Token baru saja diperbarui")
                labelToken?.setLabelType(Label.GENERAL_LIGHT_ORANGE)
                val trimmedToken = it.substring(it.length - 8)
                var trimmedPrefToken: String?
                try {
                    trimmedPrefToken = getTokenFromPref()
                    trimmedPrefToken = trimmedPrefToken?.substring(trimmedPrefToken.length - 8)
                } catch (e: Exception) {
                    trimmedPrefToken = ""
                }
                val text = "$trimmedToken \ndari\n $trimmedPrefToken"
                textToken?.text = text
            }
        })

        viewModel.notificationSetting.observe(viewLifecycleOwner, Observer {
            setNotificationSettingStatus(it)
        })

        viewModel.notificationImportance.observe(viewLifecycleOwner, Observer {
            setNotificationImportanceStatus(it)
        })

        viewModel.notificationSoundUri.observe(viewLifecycleOwner, Observer {
            setUriClick(it)
        })
    }

    private fun setUriClick(uri: Uri?) {
        adapter.updateStatus(Ringtone, uri != null)

        uri?.let {
            val ringtone = RingtoneManager.getRingtone(context, uri)
            //textRingtone?.setOnClickListener { ringtone.play() } TODO update ke adapter
        }

        if(uri == null) {
            //textRingtone.text = "Ringtone tidak ditemukan." TODO update ke adapter
        } else {
            //textRingtone.text = "Ringtone anda berfungsi." TODO update ke adapter
        }
    }

    private fun setNotificationImportanceStatus(importance: Int) {
        // TODO handle for important categories
        val isSuccess = importance == NotificationManager.IMPORTANCE_HIGH
                ||importance == NotificationManager.IMPORTANCE_DEFAULT
        adapter.updateStatus(Categories, isSuccess)

        when {
            importance == Int.MAX_VALUE -> {
                //imgStatusCategorySetting?.invisible() TODO update ke adapter
                //textNotificationCategory?.invisible() TODO update ke adapter
            }
            importance != NotificationManager.IMPORTANCE_HIGH -> {
                ticker?.show()
                when (importance) {
                    NotificationManager.IMPORTANCE_DEFAULT -> {
                        errorText = "$errorText\nPengaturan notifikasi anda sudah memenuhi standar."
                    }
                    NotificationManager.IMPORTANCE_LOW -> {
                        ticker?.tickerTitle = "Error"
                        ticker?.tickerType = Ticker.TYPE_ERROR
                        errorText = "$errorText\nNilai prioritas \"Notifikasi\": Low ($importance)." +
                                "\nNotifikasi muncul, tapi tidak ada suara." +
                                "\nAtur ke nilai prioritas lebih tinggi." +
                                "\nKlik untuk ke pengaturan."
                    }
                    else -> {
                        ticker?.tickerTitle = "Error"
                        ticker?.tickerType = Ticker.TYPE_ERROR
                        errorText = "$errorText\nNilai prioritas \"Notifikasi\": None ($importance)." +
                                "\nNotifikasi tidak muncul." +
                                "\nAtur ke nilai prioritas lebih tinggi." +
                                "\nKlik untuk ke pengaturan."
                    }
                }
                errorText.trimStart()
                ticker?.setTextDescription(errorText)
            }
        }

//        textNotificationCategory?.setOnClickListener {
//            goToSettingNotification()
//        }
        ticker?.setOnClickListener {
            goToSettingNotification()
        }
    }

    private fun setNotificationSettingStatus(notificationEnable: Boolean) {
        adapter.updateStatus(Notification, notificationEnable)

        if (!notificationEnable){
            ticker?.show()
            ticker?.tickerTitle = "Error"
            errorText = "$errorText\nMohon hidupkan pengaturan notifikasi anda. Klik disini untuk ke pengaturan."
            errorText.trimStart()
            ticker?.setTextDescription(errorText)
        }

//        textNotificationSetting?.setOnClickListener {
//            goToSettingNotification()
//        }
        ticker?.setOnClickListener {
            goToSettingNotification()
        }
    }

    private fun isNewToken(token: String): Boolean {
        val prefToken = getTokenFromPref()
        return prefToken != null && token != prefToken
    }

    private fun getTokenFromPref(): String? {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("pref_fcm_token", "")
    }

    private fun goToSettingNotification() {
        activity?.let {
            val intent = Intent()
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                    intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, it.packageName)
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                    intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                    intent.putExtra("app_package", it.packageName)
                    intent.putExtra("app_uid", it.applicationInfo.uid)
                }
                else -> {
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    intent.addCategory(Intent.CATEGORY_DEFAULT)
                    intent.data = Uri.parse("package:" + it.packageName)
                }
            }

            it.startActivity(intent)
        }
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