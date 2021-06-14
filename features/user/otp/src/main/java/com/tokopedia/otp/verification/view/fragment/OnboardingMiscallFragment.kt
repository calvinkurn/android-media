package com.tokopedia.otp.verification.view.fragment

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.otp.common.analytics.TrackingOtpConstant
import com.tokopedia.otp.common.IOnBackPressed
import com.tokopedia.otp.common.abstraction.BaseOtpToolbarFragment
import com.tokopedia.otp.common.analytics.TrackingOtpUtil
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.verification.data.OtpData
import com.tokopedia.otp.verification.domain.pojo.ModeListData
import com.tokopedia.otp.verification.domain.data.OtpConstant
import com.tokopedia.otp.verification.view.activity.VerificationActivity
import com.tokopedia.otp.verification.view.viewbinding.OnboardingMisscallViewBinding
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.utils.permission.request
import javax.inject.Inject

/**
 * Created by Ade Fulki on 22/04/20.
 * ade.hadian@tokopedia.com
 */

class OnboardingMiscallFragment : BaseOtpToolbarFragment(), IOnBackPressed {

    @Inject
    lateinit var analytics: TrackingOtpUtil

    private lateinit var otpData: OtpData
    private lateinit var modeListData: ModeListData

    override val viewBound = OnboardingMisscallViewBinding()

    private val permissionCheckerHelper = PermissionCheckerHelper()

    override fun getToolbar(): Toolbar = viewBound.toolbar ?: Toolbar(context)

    override fun getScreenName(): String = TrackingOtpConstant.Screen.SCREEN_COTP_MISSCALL

    override fun initInjector() = getComponent(OtpComponent::class.java).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissionGetPhoneNumber()
        otpData = arguments?.getParcelable(OtpConstant.OTP_DATA_EXTRA) ?: OtpData()
        modeListData = arguments?.getParcelable(OtpConstant.OTP_MODE_EXTRA) ?: ModeListData()
        KeyboardHandler.hideSoftKeyboard(activity)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onResume() {
        super.onResume()
        viewBound.imgAnimation?.isAnimating?.let {
            if (!it) {
                startAnimation()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        stopAnimation()
    }

    override fun onBackPressed(): Boolean = true

    private fun initView() {
        startAnimation()
        analytics.trackClickMethodOtpButton(otpData.otpType, modeListData.modeText)
        viewBound.btnCallMe?.setOnClickListener {
            (activity as VerificationActivity).goToVerificationPage(modeListData)
        }
    }

    private fun startAnimation() {
        viewBound.imgAnimation?.speed = ANIMATION_SPEED
        viewBound.imgAnimation?.playAnimation()
    }

    private fun stopAnimation() {
        viewBound.imgAnimation?.pauseAnimation()
    }

    private fun checkPermissionGetPhoneNumber(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity?.let {
                permissionCheckerHelper.request(it, getPermissions()) { }
            }
        }
    }

    private fun getPermissions(): Array<String> {
        return arrayOf(
                PermissionCheckerHelper.Companion.PERMISSION_READ_CALL_LOG,
                PermissionCheckerHelper.Companion.PERMISSION_CALL_PHONE,
                PermissionCheckerHelper.Companion.PERMISSION_READ_PHONE_STATE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context?.let {
                permissionCheckerHelper.onRequestPermissionsResult(it, requestCode, permissions, grantResults)
            }
        }
    }

    companion object {

        const val ANIMATION_SPEED = 1F

        fun createInstance(bundle: Bundle?): Fragment {
            val fragment = OnboardingMiscallFragment()
            fragment.arguments = bundle ?: Bundle()
            return fragment
        }
    }
}