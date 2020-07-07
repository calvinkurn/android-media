package com.tokopedia.troubleshooter.notification.ui.fragment

import android.app.NotificationManager
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
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.fcmcommon.di.DaggerFcmComponent
import com.tokopedia.fcmcommon.di.FcmModule
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.di.DaggerTroubleshootComponent
import com.tokopedia.troubleshooter.notification.di.module.TroubleshootModule
import com.tokopedia.troubleshooter.notification.ui.activity.TroubleshootActivity
import com.tokopedia.troubleshooter.notification.ui.viewmodel.LocalNotificationSettingViewModel
import com.tokopedia.troubleshooter.notification.ui.viewmodel.TroubleshootViewModel
import kotlinx.android.synthetic.main.fragment_notif_troubleshooter.*
import javax.inject.Inject
import com.tokopedia.abstraction.common.utils.view.MethodChecker.getDrawable as drawable

class TroubleshootFragment : BaseDaggerFragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: TroubleshootViewModel
    private lateinit var settingViewModel: LocalNotificationSettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(TroubleshootViewModel::class.java)

        settingViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(LocalNotificationSettingViewModel::class.java)
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
        showLoading()

        /*
        * adding interval to request
        * for user experience purpose
        * */
        Handler().postDelayed({
            viewModel.troubleshoot()
            activity?.let {
                settingViewModel.getSetting(it)
            }
        }, REQ_DELAY)
    }

    private fun initObservable() {
        viewModel.troubleshoot.observe(viewLifecycleOwner, Observer {
            hideLoading()
            if (it.isSuccess == 1) {
                onIconStatus(true)
                viewModel.updateToken()
            } else {
                onIconStatus(false)
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            hideLoading()
            onIconStatus(false)
        })

        settingViewModel.notificationSetting.observe(viewLifecycleOwner, Observer {
            pgLoaderNotificationSetting?.invisible()
            setNotificationSettingStatus(it)
        })

        settingViewModel.notificationImportance.observe(viewLifecycleOwner, Observer {
            pgLoaderCategorySetting?.invisible()
            setNotificationImportanceStatus(it)
        })

        settingViewModel.notificationSoundUri.observe(viewLifecycleOwner, Observer {
            pgLoaderRingtone?.invisible()
            setUriClick(it)
        })
    }

    private fun setUriClick(uri: Uri?) {
        activity?.let {
            imgStatusRingtone.show()
            imgStatusRingtone?.setImageDrawable(
                    if (uri != null) {
                        drawable(it, R.drawable.ic_green_checked)
                    } else {
                        drawable(it, R.drawable.ic_red_error)
                    })
        }
        uri?.let {
            val ringtone = RingtoneManager.getRingtone(context, uri)
            textRingtone?.setOnClickListener { ringtone.play() }
        }

        if(uri == null) {
            textSummary?.show()
            textSummary?.append("\nRingtone tidak ditemukan.")
        }
    }

    private fun setNotificationImportanceStatus(importance: Int) {
        activity?.let {
            imgStatusCategorySetting?.show()
            imgStatusCategorySetting?.setImageDrawable(
                    if (importance != NotificationManager.IMPORTANCE_HIGH
                            && importance != NotificationManager.IMPORTANCE_DEFAULT) {
                        drawable(it, R.drawable.ic_green_checked)
                    } else {
                        drawable(it, R.drawable.ic_red_error)
                    })
        }

        if (importance != NotificationManager.IMPORTANCE_HIGH){
            textSummary?.append("\nMohon cek pengaturan notifikasi anda. ($importance)")
            textSummary?.show()
        }
    }

    private fun setNotificationSettingStatus(notificationEnable: Boolean) {
        activity?.let {
            imgStatusNotificationSetting?.show()
            imgStatusNotificationSetting?.setImageDrawable(if (notificationEnable) {
                drawable(it, R.drawable.ic_green_checked)
            } else {
                drawable(it, R.drawable.ic_red_error)
            })
        }

        if (!notificationEnable){
            textSummary?.append("\nMohon hidupkan pengaturan notifikasi anda.")
            textSummary?.show()
        }
    }

    private fun onIconStatus(isSuccess: Boolean) {
        imgStatusTestNotif?.show()

        activity?.let {
            imgStatusTestNotif?.setImageDrawable(if (isSuccess) {
                drawable(it, R.drawable.ic_green_checked)
            } else {
                drawable(it, R.drawable.ic_red_error)
            })
        }
    }

    private fun showLoading() {
        pgLoaderTestNotif?.show()
    }

    private fun hideLoading() {
        pgLoaderTestNotif?.invisible()
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