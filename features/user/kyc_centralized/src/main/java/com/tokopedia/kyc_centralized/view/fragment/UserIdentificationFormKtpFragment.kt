package com.tokopedia.kyc_centralized.view.fragment

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationCameraActivity.Companion.createIntent
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationFormActivity
import com.tokopedia.kyc_centralized.view.model.UserIdentificationStepperModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.user_identification_common.KYCConstant
import com.tokopedia.user_identification_common.KycUrl
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.utils.permission.request

/**
 * @author by alvinatin on 02/11/18.
 */
class UserIdentificationFormKtpFragment : BaseUserIdentificationStepperFragment<UserIdentificationStepperModel>(), UserIdentificationFormActivity.Listener {

    private var permissionCheckerHelper = PermissionCheckerHelper()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        analytics?.eventViewKtpPage()
    }

    override fun getScreenName(): String = ""

    override fun setContentView() {
        val paddingDp = 16
        val scale = resources.displayMetrics.density
        onboardingImage?.setPadding(0, (paddingDp * scale + 0.5f).toInt(), 0, 0)
        setTextView()
        setButtonView()
        onboardingImage?.loadImage(KycUrl.SCAN_KTP)
        if (activity is UserIdentificationFormActivity) {
            (activity as UserIdentificationFormActivity)
                    .updateToolbarTitle(getString(R.string.title_kyc_info))
        }

        if (isKycSelfie) {
            layoutSecurity?.hide()
        } else {
            layoutSecurity?.show()
        }
    }

    private fun setTextView() {
        title?.setText(R.string.ktp_title)
        subtitle?.text = MethodChecker.fromHtml(getString(R.string.ktp_subtitle))
        subtitle?.gravity = Gravity.LEFT
        bulletTextLayout?.let { context?.let { context ->
                (activity as UserIdentificationFormActivity?)?.setTextViewWithBullet(getString(R.string.ktp_body_1), context, it)
            }
        }
        bulletTextLayout?.let { context?.let { context ->
        (activity as UserIdentificationFormActivity?)?.setTextViewWithBullet(getString(R.string.ktp_body_2), context, it)
            }
        }
        bulletTextLayout?.let {
            context?.let { context ->
                (activity as UserIdentificationFormActivity?)?.setTextViewWithBullet(getString(R.string.ktp_body_3), context, it)
            }
        }
    }

    private fun setButtonView() {
        button?.setText(R.string.ktp_button)
        button?.setOnClickListener { v: View? ->
            checkPermission {
                analytics?.eventClickNextKtpPage()
                val intent = createIntent(
                        context,
                        UserIdentificationCameraFragment.PARAM_VIEW_MODE_KTP,
                        useCropping = true,
                        useCompression = true
                )
                intent.putExtra(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, projectId)
                startActivityForResult(intent, KYCConstant.REQUEST_CODE_CAMERA_KTP)
            }
        }
    }

    private fun checkPermission(isGranted: () -> Unit) {
        activity?.let {
            permissionCheckerHelper.request(it, arrayOf(
                    PermissionCheckerHelper.Companion.PERMISSION_CAMERA,
                    PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE
            ), granted = {
                isGranted.invoke()
            }, denied = {
                it.finish()
            })
        }
    }

    override fun trackOnBackPressed() {
        analytics?.eventClickBackKtpPage()
    }

    companion object {
        fun createInstance(kycType: String): Fragment {
            return UserIdentificationFormKtpFragment().apply {
                arguments = Bundle().apply {
                    putString(ApplinkConstInternalGlobal.PARAM_KYC_TYPE, kycType)
                }
            }
        }
    }

    override fun initInjector() {}
    override fun encryptImage() {}
}