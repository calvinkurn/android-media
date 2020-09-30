package com.tokopedia.otp.notif.view.fragment

import android.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.otp.common.IOnBackPressed
import com.tokopedia.otp.common.abstraction.BaseOtpFragment
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.notif.domain.pojo.DeviceStatusPushNotifData
import com.tokopedia.otp.notif.view.viewbinding.ActivePushNotifViewBinding
import com.tokopedia.otp.notif.view.viewbinding.SettingNotifViewBinding
import com.tokopedia.otp.notif.viewmodel.NotifViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject


/**
 * Created by Ade Fulki on 14/09/20.
 */

class SettingNotifFragment : BaseOtpFragment(), IOnBackPressed {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(NotifViewModel::class.java)
    }

    override val viewBound = SettingNotifViewBinding()

    override fun getScreenName(): String = ""

    override fun initInjector() = getComponent(OtpComponent::class.java).inject(this)

    override fun onBackPressed(): Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initView()
    }

    private fun initObserver() {
        viewModel.deviceStatusPushNotifResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessDeviceStatusPushNotif().invoke(it.data)
                is Fail -> onFailedDeviceStatusPushNotif().invoke(it.throwable)
            }
        })
    }

    private fun onSuccessDeviceStatusPushNotif(): (DeviceStatusPushNotifData) -> Unit {
        return { deviceStatusPushNotifData ->
            initStatusView(deviceStatusPushNotifData)
        }
    }

    private fun onFailedDeviceStatusPushNotif(): (Throwable) -> Unit {
        return { throwable ->
            throwable.printStackTrace()
        }
    }

    private fun initView() {
        viewBound.mainImage?.setImageUrl(LINK_IMG_PHONE_OTP_PUSH_NOTIF)
        viewModel.deviceStatusPushNotif()
    }

    private fun initStatusView(deviceStatusPushNotifData: DeviceStatusPushNotifData) {
        viewBound.switch?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                showActivePushNotif()
            } else {
                showInactivePushNotif()
            }
        }
        viewBound.switch?.isChecked = deviceStatusPushNotifData.isTrusted
    }

    private fun showActivePushNotif() {
        val activePushNotifFragment = ActivePushNotifFragment.createInstance(Bundle())
        replaceFragment(activePushNotifFragment)
    }

    private fun showInactivePushNotif() {
        val inactivePushNotifFragment = InactivePushNotifFragment.createInstance(Bundle())
        replaceFragment(inactivePushNotifFragment)
    }

    private fun replaceFragment(fragment: BaseDaggerFragment) {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        viewBound.switchLayout?.let { switchLayout ->
            fragmentTransaction.replace(switchLayout.id, fragment).commit()
        }
    }

    companion object {

        private const val LINK_IMG_PHONE_OTP_PUSH_NOTIF = "https://ecs7.tokopedia.net/android/user/phone_otp_push_notif.png"

        fun createInstance(bundle: Bundle): SettingNotifFragment {
            val fragment = SettingNotifFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}