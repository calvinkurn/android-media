package com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.gojek.kyc.sdk.core.extensions.isKtpExist
import com.gojek.kyc.sdk.core.extensions.isSelfieExist
import com.gojek.OneKycSdk
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.databinding.LayoutGotoKycOnboardNonProgressiveBinding
import com.tokopedia.kyc_centralized.di.DaggerGoToKycComponent
import com.tokopedia.kyc_centralized.ui.gotoKyc.analytics.GotoKycAnalytics
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.GotoKycMainActivity
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.GotoKycMainParam
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.GotoKycRouterFragment
import com.tokopedia.kyc_centralized.util.KycSharedPreference
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usercomponents.userconsent.domain.collection.ConsentCollectionParam
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class OnboardNonProgressiveBottomSheet : BottomSheetUnify() {

    @Inject
    lateinit var kycSharedPreference: KycSharedPreference

    private var binding by autoClearedNullable<LayoutGotoKycOnboardNonProgressiveBinding>()

    private var projectId = ""
    private var source = ""
    private var isAccountLinked = false
    private var isWaitingApprovalGopay = false

    private var dismissDialogWithDataListener: (Boolean) -> Unit = {}

    private val startKycForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                kycSharedPreference.removeProjectId()
                activity?.setResult(Activity.RESULT_OK)
                activity?.finish()
            }
            KYCConstant.ActivityResult.RESULT_FINISH -> {
                kycSharedPreference.removeProjectId()
                activity?.setResult(Activity.RESULT_CANCELED)
                activity?.finish()
            }
            KYCConstant.ActivityResult.AWAITING_APPROVAL_GOPAY -> {
                isWaitingApprovalGopay = true
                dismiss()
            }
            KYCConstant.ActivityResult.ACCOUNT_NOT_LINKED -> {}
            else -> {
                binding?.layoutAccountLinking?.root?.hide()
            }
        }
    }

    private val requestPermissionLocation = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            val parameter = GotoKycMainParam(
                projectId = projectId,
                sourcePage = source
            )
            gotoCaptureKycDocuments(parameter)
        } else {
            showPermissionRejected()
        }
    }

    @Inject
    lateinit var oneKycSdk: OneKycSdk

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        arguments?.let {
            projectId = it.getString(PROJECT_ID).orEmpty()
            source = it.getString(SOURCE).orEmpty()
            isAccountLinked = it.getBoolean(ACCOUNT_LINKED)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutGotoKycOnboardNonProgressiveBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GotoKycAnalytics.sendViewKycOnboardingPage(
            projectId = projectId,
            kycFlowType = KYCConstant.GotoKycFlow.NON_PROGRESSIVE
        )
        setUpViewAccountLinking()
        initListener()
        initUserConsent()
    }

    override fun onResume() {
        super.onResume()
        setUpViewKtp()
        setUpViewSelfie()
        setUpButton()
    }

    private fun initUserConsent() {
        val consentParam = ConsentCollectionParam(
            collectionId = if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
                KYCConstant.collectionIdGotoKycProgressiveStaging
            } else {
                KYCConstant.collectionIdGotoKycProgressiveProduction
            }
        )

        binding?.consentGotoKycNonProgressive?.load(
            lifecycleOwner = viewLifecycleOwner,
            viewModelStoreOwner = this,
            consentCollectionParam = consentParam
        )

        binding?.consentGotoKycNonProgressive?.setOnCheckedChangeListener { isChecked ->
            binding?.btnSubmit?.isEnabled = isChecked
        }
    }

    private fun initListener() {
        binding?.btnSubmit?.setOnClickListener {
            GotoKycAnalytics.sendClickOnButtonMulaiVerifikasi(
                projectId = projectId,
                kycFlowType = KYCConstant.GotoKycFlow.NON_PROGRESSIVE
            )
            if (isAccountLinked or (binding?.layoutAccountLinking?.root?.isShown == false)) {
                activity?.let {
                    if (ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissionLocation.launch(Manifest.permission.CAMERA)
                    } else {
                        val parameter = GotoKycMainParam(
                            projectId = projectId,
                            sourcePage = source
                        )
                        gotoCaptureKycDocuments(parameter)
                    }
                }
            } else {
                val parameter = GotoKycMainParam(
                    projectId = projectId,
                    sourcePage = source
                )
                gotoBridgingAccountLinking(parameter)
            }
        }
    }

    private fun setUpViewAccountLinking() {
        binding?.layoutAccountLinking?.apply {
            if (!isAccountLinked) {
                imgItemOnboard.loadImageWithoutPlaceholder(
                    getString(R.string.img_url_goto_kyc_onboard_account_linking)
                )

                tvItemTitle.text = getString(R.string.goto_kyc_onboard_non_progressive_item_account_linking_title)

                tvItemSubtitle.text = getString(R.string.goto_kyc_onboard_non_progressive_item_account_linking_subtitle_unlinked)
            }
            root.showWithCondition(!isAccountLinked)
        }

    }

    private fun setUpViewKtp() {
        val isKtpTaken = oneKycSdk.getKycPlusPreferencesProvider().getKycUploadProgressState().isKtpExist()
        binding?.layoutKtp?.apply {
            imgItemOnboard.loadImageWithoutPlaceholder(
                getString(R.string.img_url_goto_kyc_onboard_ktp)
            )

            tvItemTitle.text = getString(R.string.goto_kyc_onboard_non_progressive_item_ktp_title)

            val compoundDrawable = if (isKtpTaken) { R.drawable.ic_checklist_circle_green } else { 0 }
            tvItemTitle.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                compoundDrawable,
                0
            )

            tvItemSubtitle.text = if (isKtpTaken) {
                getString(R.string.goto_kyc_onboard_non_progressive_item_ktp_subtitle_taken)
            } else {
                getString(R.string.goto_kyc_onboard_non_progressive_item_ktp_subtitle_not_taken)
            }
        }
    }

    private fun setUpViewSelfie() {
        val isSelfieTaken = oneKycSdk.getKycPlusPreferencesProvider().getKycUploadProgressState().isSelfieExist()
        binding?.layoutSelfie?.apply {
            imgItemOnboard.loadImageWithoutPlaceholder(
                getString(R.string.img_url_goto_kyc_onboard_selfie)
            )

            tvItemTitle.text = getString(R.string.goto_kyc_onboard_non_progressive_item_selfie_title)
            val compoundDrawable = if (isSelfieTaken) { R.drawable.ic_checklist_circle_green } else { 0 }
            tvItemTitle.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                compoundDrawable,
                0
            )

            tvItemSubtitle.text = if (isSelfieTaken) {
                getString(R.string.goto_kyc_onboard_non_progressive_item_selfie_subtitle_taken)
            } else {
                getString(R.string.goto_kyc_onboard_non_progressive_item_selfie_subtitle_not_taken)
            }
        }
    }

    private fun setUpButton() {
        val isKtpTaken = oneKycSdk.getKycPlusPreferencesProvider().getKycUploadProgressState().isKtpExist()
        val isSelfieTaken = oneKycSdk.getKycPlusPreferencesProvider().getKycUploadProgressState().isSelfieExist()

        binding?.btnSubmit?.text = if (isKtpTaken || isSelfieTaken) {
            getString(R.string.goto_kyc_continue_verification)
        } else {
            getString(R.string.goto_kyc_start_verification)
        }
    }

    private fun gotoBridgingAccountLinking(parameter: GotoKycMainParam) {
        val intent = Intent(activity, GotoKycMainActivity::class.java)
        intent.putExtra(GotoKycRouterFragment.PARAM_REQUEST_PAGE, GotoKycRouterFragment.PAGE_BRIDGING_ACCOUNT_LINKING)
        intent.putExtra(GotoKycRouterFragment.PARAM_DATA, parameter)
        startKycForResult.launch(intent)
    }

    private fun gotoCaptureKycDocuments(parameter: GotoKycMainParam) {
        val intent = Intent(activity, GotoKycMainActivity::class.java)
        intent.putExtra(GotoKycRouterFragment.PARAM_REQUEST_PAGE, GotoKycRouterFragment.PAGE_CAPTURE_KYC_DOCUMENTS)
        intent.putExtra(GotoKycRouterFragment.PARAM_DATA, parameter)
        startKycForResult.launch(intent)
    }

    private fun initInjector() {
        DaggerGoToKycComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build().inject(this)
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissDialogWithDataListener(isWaitingApprovalGopay)

        GotoKycAnalytics.sendClickOnButtonCloseOnboardingBottomSheet(
            projectId = projectId,
            kycFlowType = KYCConstant.GotoKycFlow.NON_PROGRESSIVE
        )
    }

    fun setOnDismissWithDataListener(isAwaitingApprovalGopay: (Boolean) -> Unit) {
        dismissDialogWithDataListener = isAwaitingApprovalGopay
    }

    companion object {
        private const val PROJECT_ID = "project_id"
        private const val SOURCE = "source"
        private const val ACCOUNT_LINKED = "account_linked"
        private const val PACKAGE = "package"

        fun newInstance(projectId: String, source: String = "", isAccountLinked: Boolean) =
            OnboardNonProgressiveBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(PROJECT_ID, projectId)
                    putString(SOURCE, source)
                    putBoolean(ACCOUNT_LINKED, isAccountLinked)
                }
            }
    }
}
