package com.tokopedia.kyc_centralized.view.fragment

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_KYC_TYPE
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kyc_centralized.KycConstant.PADDING_0_5F
import com.tokopedia.kyc_centralized.KycConstant.PADDING_16
import com.tokopedia.kyc_centralized.KycConstant.PADDING_ZERO
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.common.KycUrl
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationCameraActivity.Companion.createIntent
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationFormActivity
import com.tokopedia.kyc_centralized.view.model.UserIdentificationStepperModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.utils.permission.request

/**
 * @author by alvinatin on 02/11/18.
 */
class UserIdentificationFormKtpFragment :
    BaseUserIdentificationStepperFragment<UserIdentificationStepperModel>(),
    UserIdentificationFormActivity.Listener {

    private var permissionCheckerHelper = PermissionCheckerHelper()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        analytics?.eventViewKtpPage()
    }

    override fun getScreenName(): String = ""

    override fun setContentView() {
        val scale = context?.resources?.displayMetrics?.density.orZero()
        viewBinding?.formOnboardingImage?.setPadding(
            PADDING_ZERO,
            (PADDING_16 * scale + PADDING_0_5F).toInt(),
            PADDING_ZERO,
            PADDING_ZERO
        )
        setTextView()
        setButtonView()
        viewBinding?.formOnboardingImage?.loadImage(KycUrl.SCAN_KTP)
        if (activity is UserIdentificationFormActivity) {
            (activity as UserIdentificationFormActivity)
                .updateToolbarTitle(getString(R.string.title_kyc_info))
        }

        if (isKycSelfie) {
            viewBinding?.securityLayout?.hide()
        } else {
            viewBinding?.securityLayout?.show()
        }
    }

    private fun setTextView() {
        viewBinding?.title?.setText(R.string.ktp_title)
        viewBinding?.subtitle?.text = MethodChecker.fromHtml(getString(R.string.ktp_subtitle))
        viewBinding?.subtitle?.gravity = Gravity.LEFT
        viewBinding?.layoutInfoBullet?.let {
            context?.let { context ->
                (activity as? UserIdentificationFormActivity)?.setTextViewWithBullet(
                    getString(R.string.ktp_body_1),
                    context,
                    it
                )
            }
        }
        viewBinding?.layoutInfoBullet?.let {
            context?.let { context ->
                (activity as? UserIdentificationFormActivity)?.setTextViewWithBullet(
                    getString(R.string.ktp_body_2),
                    context,
                    it
                )
            }
        }
        viewBinding?.layoutInfoBullet?.let {
            context?.let { context ->
                (activity as? UserIdentificationFormActivity)?.setTextViewWithBullet(
                    getString(R.string.ktp_body_3),
                    context,
                    it
                )
            }
        }
    }

    private fun setButtonView() {
        viewBinding?.button?.setText(R.string.ktp_button)
        viewBinding?.button?.setOnClickListener { v: View? ->
            checkPermission {
                analytics?.eventClickNextKtpPage()
                val intent = context?.let {
                    createIntent(
                        it,
                        UserIdentificationCameraFragment.PARAM_VIEW_MODE_KTP,
                        projectId,
                        useCropping = true,
                        useCompression = true
                    )
                }
                startActivityForResult(intent, KYCConstant.REQUEST_CODE_CAMERA_KTP)
            }
        }
    }

    private fun checkPermission(isGranted: () -> Unit) {
        val listPermission = if (SDK_INT <= VERSION_CODES.P) {
            arrayOf(
                PermissionCheckerHelper.Companion.PERMISSION_CAMERA,
                PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE
            )
        } else {
            arrayOf(PermissionCheckerHelper.Companion.PERMISSION_CAMERA)
        }

        activity?.let {
            permissionCheckerHelper.request(it, listPermission,
                granted = {
                    isGranted.invoke()
                }, denied = {
                    it.finish()
                }
            )
        }
    }

    override fun trackOnBackPressed() {
        analytics?.eventClickBackKtpPage()
    }

    companion object {
        fun createInstance(kycType: String): Fragment {
            return UserIdentificationFormKtpFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_KYC_TYPE, kycType)
                }
            }
        }
    }

    override fun initInjector() {}
    override fun encryptImage() {}
}
