package com.tokopedia.kyc_centralized.ui.gotoKyc.main.status

import android.animation.ValueAnimator
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.text.HtmlCompat
import androidx.navigation.fragment.navArgs
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visibleWithCondition
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.common.KycStatus
import com.tokopedia.kyc_centralized.databinding.FragmentGotoKycStatusSubmissionBinding
import com.tokopedia.kyc_centralized.di.GoToKycComponent
import com.tokopedia.kyc_centralized.ui.gotoKyc.analytics.GotoKycAnalytics
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.ProjectInfoResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.transparent.GotoKycTransparentFragment
import com.tokopedia.kyc_centralized.ui.gotoKyc.utils.getGotoKycErrorMessage
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class StatusSubmissionFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentGotoKycStatusSubmissionBinding>()

    private val args: StatusSubmissionFragmentArgs by navArgs()
    private var kycFlowType: String = ""
    private var sourcePage: String = ""
    private var status: String = ""
    private var rejectionReason: String = ""
    private var isAccountPage: Boolean = false
    private var waitMessage: String = ""
    private var projectId: String = ""

    private var bottomSheetDetailBenefit: BenefitDetailBottomSheet? = null
    private var loaderRefreshStatus: LoaderDialog? = null

    private val startReVerifyKycForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        activity?.setResult(result.resultCode)
        activity?.finish()
    }

    @Inject
    lateinit var viewModel: StatusSubmissionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGotoKycStatusSubmissionBinding.inflate(inflater, container, false)

        kycFlowType = args.parameter.gotoKycType
        status = args.parameter.status
        sourcePage = args.parameter.sourcePage
        rejectionReason = args.parameter.rejectionReason
        isAccountPage = args.parameter.projectId == KYCConstant.PROJECT_ID_ACCOUNT
        waitMessage = args.parameter.waitMessage
        projectId = args.parameter.projectId

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        initListener()
        initObserver()
        initToolbar()
        initBackPressedListener()
    }

    private fun initBackPressedListener() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.setResult(Activity.RESULT_OK)
                    activity?.finish()
                }
            }
        )
    }

    private fun initToolbar() {
        binding?.unifyToolbar?.setNavigationOnClickListener {
            activity?.setResult(Activity.RESULT_OK)
            activity?.finish()
        }
    }

    private fun initObserver() {
        viewModel.kycStatus.observe(viewLifecycleOwner) {
            setUpLoaderStatus(false)
            when (it) {
                is ProjectInfoResult.StatusSubmission -> {
                    status = it.status
                    rejectionReason = it.rejectionReason
                    setUpView()
                }
                else -> {
                    val throwable = if (it is ProjectInfoResult.Failed) {
                        it.throwable
                    } else {
                        null
                    }
                    showToaster(throwable)
                }
            }
        }
    }

    private fun showToaster(throwable: Throwable?) {
        val message = throwable.getGotoKycErrorMessage(requireContext())
        Toaster.build(requireView(), message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
    }

    private fun setUpLoaderStatus(isLoading: Boolean) {
        if (isLoading) {
            if (loaderRefreshStatus == null) {
                loaderRefreshStatus = LoaderDialog(requireActivity())
                loaderRefreshStatus?.apply {
                    setLoadingText("")
                    dialog.setOverlayClose(false)
                    show()
                }
            }
        } else {
            loaderRefreshStatus?.dismiss()
            loaderRefreshStatus = null
        }
    }

    private fun setUpView() {
        when(status) {
            KycStatus.REJECTED.code.toString() -> {
                GotoKycAnalytics.sendViewStatusPage(
                    kycFlowType = GotoKycAnalytics.KYC_FLOW_NON_PROGRESSIVE,
                    status = GotoKycAnalytics.FAILED,
                    errorReason = rejectionReason,
                    projectId = projectId
                )
                onRejected()
            }
            KycStatus.PENDING.code.toString() -> {
                GotoKycAnalytics.sendViewStatusPage(
                    kycFlowType = GotoKycAnalytics.KYC_FLOW_NON_PROGRESSIVE,
                    status = GotoKycAnalytics.PENDING,
                    projectId = projectId
                )
                onPending()
            }
            KycStatus.VERIFIED.code.toString() -> {
                GotoKycAnalytics.sendViewStatusPage(
                    kycFlowType = GotoKycAnalytics.KYC_FLOW_NON_PROGRESSIVE,
                    status = GotoKycAnalytics.SUCCESS,
                    projectId = projectId
                )
                onVerified()
            }
            KycStatus.BLACKLISTED.code.toString() -> {
                onBlackListed()
            }
        }
    }

    private fun initListener() {
        binding?.layoutStatusSubmission?.btnSecondary?.setOnClickListener {
            when (status) {
                KycStatus.PENDING.code.toString() -> {
                    setUpLoaderStatus(true)
                    viewModel.kycStatus(projectId)
                }
                else -> {
                    goToTokopediaCare()
                }
            }
        }

        binding?.layoutStatusSubmission?.btnPrimary?.setOnClickListener {
            when(status) {
                KycStatus.BLACKLISTED.code.toString() -> {
                    goToTokopediaCare()
                }
                KycStatus.REJECTED.code.toString() -> {
                    GotoKycAnalytics.sendClickOnButtonVerifikasiUlangRejectPage(
                        kycFlowType = GotoKycAnalytics.KYC_FLOW_NON_PROGRESSIVE,
                        errorMessage = rejectionReason,
                        projectId = projectId
                    )
                    goToTransparentActivityToReVerify()
                }
                else -> {
                    activity?.setResult(Activity.RESULT_OK)
                    activity?.finish()
                }
            }
        }

        binding?.layoutBenefitNonAccount?.seeMoreBenefitButton?.setOnClickListener {
            showBottomSheetDetailBenefit()
        }
    }

    private fun onRejected() {
        loadInitImage(
            imageUrl = getString(R.string.img_url_goto_kyc_status_submission_rejected),
            usingLottie = false
        )

        binding?.apply {
            divider.showWithCondition(isAccountPage)
            layoutBenefitAccount.root.showWithCondition(isAccountPage)
            layoutBenefitAccount.tvTitle.text = getString(R.string.goto_kyc_benefit_account_title_trouble)
            layoutBenefitNonAccount.root.hide()
        }

        binding?.layoutStatusSubmission?.apply {
            tvHeader.text = getString(R.string.goto_kyc_status_rejected_title)
            tvDescription.hide()
            btnPrimary.text = getString(R.string.goto_kyc_status_rejected_button_primary)
            btnSecondary.text = getString(R.string.goto_kyc_status_rejected_button_secondary)
            btnPrimary.show()
            btnSecondary.show()
            cardReason.show()
            tvReason.text = rejectionReason
        }
    }

    private fun onBlackListed() {
        loadInitImage(
            imageUrl = getString(R.string.img_url_goto_kyc_status_submission_blacklisted),
            usingLottie = false
        )

        binding?.apply {
            divider.showWithCondition(isAccountPage)
            layoutBenefitAccount.root.showWithCondition(isAccountPage)
            layoutBenefitNonAccount.root.hide()
            layoutBenefitAccount.tvTitle.text = getString(R.string.goto_kyc_benefit_account_title_trouble)
        }

        binding?.layoutStatusSubmission?.apply {
            tvHeader.text = getString(R.string.goto_kyc_status_blacklisted_title)
            tvDescription.text = getString(R.string.goto_kyc_status_blacklisted_subtitle)
            btnPrimary.text = getString(R.string.goto_kyc_status_blacklisted_button)
            btnSecondary.hide()
        }
    }

    private fun onVerified() {
        loadInitImage(
            imageUrl = getString(R.string.img_url_goto_kyc_status_submission_verified),
            usingLottie = true
        )

        binding?.layoutStatusSubmission?.apply {
            tvHeader.text = getString(R.string.goto_kyc_status_verified_title)
            tvDescription.text = getString(R.string.goto_kyc_status_verified_subtitle)
            btnPrimary.text = if (sourcePage.isEmpty()) {
                getString(R.string.goto_kyc_back_to_source)
            } else {
                getString(R.string.goto_kyc_status_pending_button, sourcePage)
            }
            btnSecondary.hide()
            btnPrimary.showWithCondition(!isAccountPage)
        }

        binding?.apply {
            layoutBenefitNonAccount.root.showWithCondition(!isAccountPage)
            divider.show()
            layoutBenefitAccount.root.showWithCondition(isAccountPage)
            layoutBenefitAccount.tvTitle.text = getString(R.string.goto_kyc_benefit_account_title_verified)
        }

    }

    private fun onPending() {
        loadInitImage(
            imageUrl = getString(R.string.img_url_goto_kyc_status_submission_pending),
            usingLottie = true
        )

        binding?.layoutStatusSubmission?.apply {
            tvHeader.text = getString(R.string.goto_kyc_status_pending_title)
            tvDescription.text =
                waitMessage.ifEmpty {
                    HtmlCompat.fromHtml(
                        getString(R.string.goto_kyc_status_pending_subtitle),
                        HtmlCompat.FROM_HTML_MODE_COMPACT
                    )
                }
            btnPrimary.text = if (sourcePage.isEmpty()) {
                getString(R.string.goto_kyc_back_to_source)
            } else {
                getString(R.string.goto_kyc_status_pending_button, sourcePage)
            }
            btnSecondary.text = getString(R.string.goto_kyc_status_pending_refresh_status)
            btnSecondary.show()

            btnPrimary.showWithCondition(!isAccountPage)
        }

        binding?.layoutBenefitNonAccount?.root?.showWithCondition(!isAccountPage)
        binding?.layoutBenefitAccount?.root?.showWithCondition(isAccountPage)
        binding?.layoutBenefitAccount?.tvTitle?.text = getString(R.string.goto_kyc_benefit_account_title_pending)
    }

    private fun showBottomSheetDetailBenefit() {
        bottomSheetDetailBenefit = BenefitDetailBottomSheet()
        bottomSheetDetailBenefit?.show(childFragmentManager, TAG_BOTTOM_SHEET_DETAIL_BENEFIT)
    }

    private fun goToTransparentActivityToReVerify() {

        val intent = RouteManager.getIntent(activity, ApplinkConstInternalUserPlatform.GOTO_KYC).apply {
            putExtra(GotoKycTransparentFragment.IS_RE_VERIFY, true)
            putExtra(ApplinkConstInternalUserPlatform.PARAM_SOURCE, sourcePage)
            putExtra(ApplinkConstInternalUserPlatform.PARAM_PROJECT_ID, projectId)
        }
        startReVerifyKycForResult.launch(intent)
    }

    private fun goToTokopediaCare() {
        RouteManager.route(
            context,
            ApplinkConstInternalGlobal.WEBVIEW,
            TokopediaUrl.getInstance().WEB.plus(PATH_TOKOPEDIA_CARE)
        )
    }

    private fun loadInitImage(imageUrl: String, usingLottie: Boolean) {
        binding?.layoutStatusSubmission?.apply {
            ivStatusSubmission.visibleWithCondition(!usingLottie)
            ivStatusSubmissionLottie.visibleWithCondition(usingLottie)

            if (usingLottie) {
                LottieCompositionFactory.fromUrl(requireContext(), imageUrl).addListener { result: LottieComposition? ->
                    result?.let { ivStatusSubmissionLottie.setComposition(it) }
                    ivStatusSubmissionLottie.repeatCount = ValueAnimator.INFINITE
                    ivStatusSubmissionLottie.playAnimation()
                }
            } else {
                ivStatusSubmission.loadImageWithoutPlaceholder(imageUrl)
            }
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(GoToKycComponent::class.java).inject(this)
    }

    companion object {
        private const val PATH_TOKOPEDIA_CARE = "help/article/a-3881?nref='goto-kyc'"
        private const val TAG_BOTTOM_SHEET_DETAIL_BENEFIT = "tag bottom sheet detail benefit"
    }
}
