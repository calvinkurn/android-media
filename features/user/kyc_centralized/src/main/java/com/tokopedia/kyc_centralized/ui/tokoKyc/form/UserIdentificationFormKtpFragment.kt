package com.tokopedia.kyc_centralized.ui.tokoKyc.form

import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_KYC_TYPE
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.analytics.UserIdentificationCommonAnalytics
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.common.KYCConstant.PADDING_0_5F
import com.tokopedia.kyc_centralized.common.KYCConstant.PADDING_16
import com.tokopedia.kyc_centralized.common.KYCConstant.PADDING_ZERO
import com.tokopedia.kyc_centralized.common.KycUrl
import com.tokopedia.kyc_centralized.di.UserIdentificationCommonComponent
import com.tokopedia.kyc_centralized.ui.tokoKyc.camera.UserIdentificationCameraActivity.Companion.createIntent
import com.tokopedia.kyc_centralized.ui.tokoKyc.camera.UserIdentificationCameraFragment
import com.tokopedia.kyc_centralized.ui.tokoKyc.form.stepper.BaseUserIdentificationStepperFragment
import com.tokopedia.kyc_centralized.ui.tokoKyc.form.stepper.UserIdentificationStepperModel
import com.tokopedia.kyc_centralized.util.KycSharedPreference
import com.tokopedia.media.loader.loadImage
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.permission.PermissionCheckerHelper
import javax.inject.Inject

/**
 * @author by alvinatin on 02/11/18.
 */
class UserIdentificationFormKtpFragment :
    BaseUserIdentificationStepperFragment<UserIdentificationStepperModel>(),
    UserIdentificationFormActivity.Listener {

    @Inject
    lateinit var kycSharedPreference: KycSharedPreference

    @Inject
    override lateinit var remoteConfig: RemoteConfig
    private var analytics: UserIdentificationCommonAnalytics? = null

    private val requestPermissionCamera = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { listGrantedResult ->
        checkPermissionResult(listGrantedResult)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        projectId = activity?.intent?.getIntExtra(ApplinkConstInternalUserPlatform.PARAM_PROJECT_ID, -1) ?: -1

        val kycFlowType = kycSharedPreference.getStringCache(KYCConstant.SharedPreference.KEY_KYC_FLOW_TYPE)
        analytics = UserIdentificationCommonAnalytics.createInstance(projectId, kycFlowType)

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
            checkPermission()
        }
    }

    private fun goToKtpCamera() {
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

    private fun checkPermission() {
        val listPermission = if (SDK_INT <= VERSION_CODES.P) {
            arrayOf(
                PermissionCheckerHelper.Companion.PERMISSION_CAMERA,
                PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE
            )
        } else {
            arrayOf(PermissionCheckerHelper.Companion.PERMISSION_CAMERA)
        }

        activity?.let {
            requestPermissionCamera.launch(listPermission)
        }
    }

    private fun checkPermissionResult(listPermission: Map<String, Boolean>) {
        var isGranted = true

        listPermission.forEach {
            if (isGranted && !it.value) {
                isGranted = false
            }
        }

        if (isGranted) {
            goToKtpCamera()
        } else {
            showPermissionRejected()
        }
    }

    private fun showPermissionRejected() {
        Toaster.build(
            requireView().rootView,
            getString(R.string.goto_kyc_permission_camera_denied),
            Toaster.LENGTH_LONG,
            Toaster.TYPE_ERROR,
            getString(R.string.goto_kyc_permission_action_active)
        ) { goToApplicationDetailActivity() }.show()
    }

    private fun goToApplicationDetailActivity() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts(PACKAGE, requireActivity().packageName, null)
        intent.data = uri
        requireActivity().startActivity(intent)
    }

    override fun trackOnBackPressed() {
        analytics?.eventClickBackKtpPage()
    }

    companion object {
        private const val PACKAGE = "package"
        fun createInstance(kycType: String): Fragment {
            return UserIdentificationFormKtpFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_KYC_TYPE, kycType)
                }
            }
        }
    }

    override fun initInjector() {
        getComponent(UserIdentificationCommonComponent::class.java).inject(this)
    }
    override fun encryptImage() {}
}
