package com.tokopedia.otp.notif.view.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.util.LetUtil
import com.tokopedia.otp.R
import com.tokopedia.otp.common.IOnBackPressed
import com.tokopedia.otp.common.LoadingDialog
import com.tokopedia.otp.common.abstraction.BaseOtpFragment
import com.tokopedia.otp.common.abstraction.BaseOtpToolbarFragment
import com.tokopedia.otp.common.analytics.TrackingOtpConstant
import com.tokopedia.otp.common.analytics.TrackingOtpUtil
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.notif.domain.pojo.ChangeStatusPushNotifData
import com.tokopedia.otp.notif.domain.pojo.DeviceStatusPushNotifData
import com.tokopedia.otp.notif.view.viewbinding.SettingNotifViewBinding
import com.tokopedia.otp.notif.viewmodel.NotifViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject


/**
 * Created by Ade Fulki on 14/09/20.
 */

class SettingNotifFragment : BaseOtpToolbarFragment(), IOnBackPressed {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var loadingDialog: LoadingDialog
    @Inject
    lateinit var analytics: TrackingOtpUtil

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(NotifViewModel::class.java)
    }

    override val viewBound = SettingNotifViewBinding()

    override fun getToolbar(): Toolbar = viewBound.toolbar ?: Toolbar(context)

    override fun getScreenName(): String = TrackingOtpConstant.Screen.SCREEN_PUSH_NOTIF_SETTING

    override fun initInjector() = getComponent(OtpComponent::class.java).inject(this)

    override fun onBackPressed(): Boolean = true

    override fun onStart() {
        super.onStart()
        analytics.trackScreen(screenName)
        analytics.trackViewOtpPushNotifSettingPage()
    }

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
        viewModel.changeStatusPushNotifResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessChangeOtpPushNotif().invoke(it.data)
                is Fail -> onFailedChangeOtpPushNotif().invoke(it.throwable)
            }
        })
    }

    private fun onSuccessDeviceStatusPushNotif(): (DeviceStatusPushNotifData) -> Unit {
        return { deviceStatusPushNotifData ->
            dismissLoading()
            initStatusView(deviceStatusPushNotifData)
        }
    }

    private fun onFailedDeviceStatusPushNotif(): (Throwable) -> Unit {
        return { throwable ->
            dismissLoading()
            throwable.printStackTrace()
            showErrorToaster(throwable.message)
        }
    }

    private fun onSuccessChangeOtpPushNotif(): (ChangeStatusPushNotifData) -> Unit {
        return {
            viewModel.deviceStatusPushNotif()
        }
    }

    private fun onFailedChangeOtpPushNotif(): (Throwable) -> Unit {
        return { throwable ->
            dismissLoading()
            throwable.printStackTrace()
            showErrorToaster(throwable.message)
        }
    }

    private fun initView() {
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = getString(R.string.title_setting_push_notif)
            setDisplayShowTitleEnabled(true)
        }
        showLoading()
        viewBound.mainImage?.setImageUrl(LINK_IMG_PHONE_OTP_PUSH_NOTIF)
        viewModel.deviceStatusPushNotif()
    }

    private fun initStatusView(deviceStatusPushNotifData: DeviceStatusPushNotifData) {
        viewBound.switch?.isEnabled = deviceStatusPushNotifData.isTrusted
        viewBound.switch?.isChecked = deviceStatusPushNotifData.isActive
        if (deviceStatusPushNotifData.isActive) {
            showActivePushNotif(deviceStatusPushNotifData)
        } else {
            showInactivePushNotif(deviceStatusPushNotifData)
        }
        viewBound.switch?.setOnCheckedChangeListener { _, isChecked ->
            analytics.trackClickSignInFromNotifSettingButton(if (isChecked) TrackingOtpConstant.Label.LABEL_ON else TrackingOtpConstant.Label.LABEL_OFF)
            showLoading()
            viewModel.changeStatusPushNotif(isChecked)
        }
    }

    private fun showActivePushNotif(deviceStatusPushNotifData: DeviceStatusPushNotifData) {
        val bundle = Bundle()
        bundle.putSerializable(PARAM_DEVICE_STATUS, deviceStatusPushNotifData)
        val activePushNotifFragment = ActivePushNotifFragment.createInstance(bundle)
        replaceFragment(activePushNotifFragment)
    }

    private fun showInactivePushNotif(deviceStatusPushNotifData: DeviceStatusPushNotifData) {
        val bundle = Bundle()
        bundle.putSerializable(PARAM_DEVICE_STATUS, deviceStatusPushNotifData)
        val inactivePushNotifFragment = InactivePushNotifFragment.createInstance(Bundle(bundle))
        replaceFragment(inactivePushNotifFragment)
    }

    private fun replaceFragment(fragment: BaseDaggerFragment) {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        viewBound.switchLayout?.let { switchLayout ->
            fragmentTransaction.replace(switchLayout.id, fragment).commit()
        }
    }

    private fun showLoading() {
        if (activity != null) {
            loadingDialog.show()
        }
    }

    private fun dismissLoading() {
        if (activity != null) {
            loadingDialog.dismiss()
        }
    }

    private fun showErrorToaster(message: String?) {
        LetUtil.ifLet(message, viewBound.containerView) { (message, containerView) ->
            Toaster.make(containerView as View, message as String, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
        }
    }

    companion object {

        const val PARAM_DEVICE_STATUS = "device_status"

        private const val LINK_IMG_PHONE_OTP_PUSH_NOTIF = "https://ecs7.tokopedia.net/android/user/phone_otp_push_notif.png"

        fun createInstance(bundle: Bundle): SettingNotifFragment {
            val fragment = SettingNotifFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}