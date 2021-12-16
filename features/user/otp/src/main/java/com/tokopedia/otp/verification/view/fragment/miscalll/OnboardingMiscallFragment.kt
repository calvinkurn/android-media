package com.tokopedia.otp.verification.view.fragment.miscalll

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.otp.R
import com.tokopedia.otp.common.analytics.TrackingOtpConstant
import com.tokopedia.otp.common.IOnBackPressed
import com.tokopedia.otp.common.abstraction.BaseOtpToolbarFragment
import com.tokopedia.otp.common.analytics.TrackingOtpUtil
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.verification.data.OtpData
import com.tokopedia.otp.verification.domain.pojo.ModeListData
import com.tokopedia.otp.verification.domain.data.OtpConstant
import com.tokopedia.otp.verification.domain.data.ROLLANCE_KEY_MISCALL_OTP
import com.tokopedia.otp.verification.view.activity.VerificationActivity
import com.tokopedia.otp.verification.view.viewbinding.OnboardingMisscallViewBinding
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.utils.permission.request
import javax.inject.Inject

/**
 * Created by Ade Fulki on 22/04/20.
 * ade.hadian@tokopedia.com
 */

open class OnboardingMiscallFragment : BaseOtpToolbarFragment(), IOnBackPressed {

    @Inject
    lateinit var analytics: TrackingOtpUtil

    private lateinit var otpData: OtpData
    private lateinit var modeListData: ModeListData

    override val viewBound = OnboardingMisscallViewBinding()

    private val permissionCheckerHelper = PermissionCheckerHelper()

    private var remoteConfigInstance: RemoteConfigInstance? = null
    private var rollanceType = ""

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
        rollanceType = getAbTestPlatform()?.getString(ROLLANCE_KEY_MISCALL_OTP).toString()
        initView()
    }

    override fun onBackPressed(): Boolean = true

    private fun initView() {
        viewBound.btnCallMe?.setOnClickListener {
            analytics.trackClickMethodOtpButton(otpData.otpType, modeListData.modeText)
            (activity as VerificationActivity).goToVerificationPage(modeListData)
        }

        viewBound.title?.text = getTitle()
        viewBound.subtitle?.text = getDescription()

        setNewImage()
    }

    private fun setNewImage() {
        viewBound.img?.apply {
            setImageUrl(URL_IMG_ON_BOARDING_NEW)
            show()
        }
    }

    private fun checkPermissionGetPhoneNumber() {
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context?.let {
                permissionCheckerHelper.onRequestPermissionsResult(
                    it,
                    requestCode,
                    permissions,
                    grantResults
                )
            }
        }
    }

    private fun getTitle(): String {
        var title = ""
        context?.let {
            title = if (isOtpMiscallNew()) {
                getString(R.string.cotp_miscall_onboarding_title_new)
            } else {
                getString(R.string.cotp_miscall_onboarding_title)
            }
        }
        return title
    }

    private fun getDescription(): String {
        var description = ""
        context?.let {
            description = if (isOtpMiscallNew()) {
                getString(R.string.cotp_miscall_onboarding_desc_new)
            } else {
                getString(R.string.cotp_miscall_onboarding_desc)
            }
        }
        return description
    }

    private fun isOtpMiscallNew(): Boolean {
        return rollanceType.contains(ROLLANCE_KEY_MISCALL_OTP)
    }

    private fun getAbTestPlatform(): AbTestPlatform? {
        if (remoteConfigInstance == null) {
            remoteConfigInstance = RemoteConfigInstance(activity?.application)
        }
        return remoteConfigInstance?.abTestPlatform
    }

    companion object {
        private const val URL_IMG_ON_BOARDING_NEW = "https://images.tokopedia.net/img/android/user/miscall/ic_miscall_onboarding_2.png"

        fun createInstance(bundle: Bundle?): Fragment {
            val fragment = OnboardingMiscallFragment()
            fragment.arguments = bundle ?: Bundle()
            return fragment
        }
    }
}