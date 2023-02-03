package com.tokopedia.otp.verification.view.fragment.miscalll

import com.tokopedia.imageassets.ImageUrl

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.otp.R
import com.tokopedia.otp.common.IOnBackPressed
import com.tokopedia.otp.common.abstraction.BaseOtpToolbarFragment
import com.tokopedia.otp.common.analytics.TrackingOtpConstant
import com.tokopedia.otp.common.analytics.TrackingOtpUtil
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.verification.data.OtpConstant
import com.tokopedia.otp.verification.data.OtpData
import com.tokopedia.otp.verification.domain.pojo.ModeListData
import com.tokopedia.otp.verification.view.activity.VerificationActivity
import com.tokopedia.otp.verification.view.viewbinding.OnboardingMisscallViewBinding
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

    override fun getToolbar(): Toolbar = viewBound.toolbar ?: Toolbar(requireContext())

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

    override fun onBackPressed(): Boolean = true

    private fun initView() {
        viewBound.btnCallMe?.setOnClickListener {
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
        return context?.getString(R.string.cotp_miscall_onboarding_title).orEmpty()
    }

    private fun getDescription(): String {
        return context?.getString(R.string.cotp_miscall_onboarding_desc).orEmpty()
    }

    companion object {
        private const val URL_IMG_ON_BOARDING_NEW = ImageUrl.URL_IMG_ON_BOARDING_NEW

        fun createInstance(bundle: Bundle?): Fragment {
            val fragment = OnboardingMiscallFragment()
            fragment.arguments = bundle ?: Bundle()
            return fragment
        }
    }
}
