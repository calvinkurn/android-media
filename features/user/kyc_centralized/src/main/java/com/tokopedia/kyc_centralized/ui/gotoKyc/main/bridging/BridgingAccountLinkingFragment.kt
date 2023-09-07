package com.tokopedia.kyc_centralized.ui.gotoKyc.main.bridging

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.databinding.FragmentGotoKycBridgingAccountLinkingBinding
import com.tokopedia.kyc_centralized.di.GoToKycComponent
import com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet.DobChallengeExhaustedBottomSheet
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.AccountLinkingStatusResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.CheckEligibilityResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.challenge.DobChallengeParam
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.status.StatusSubmissionParam
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.capture.CaptureKycDocumentsParam
import com.tokopedia.kyc_centralized.ui.gotoKyc.utils.getGotoKycErrorMessage
import com.tokopedia.kyc_centralized.util.KycSharedPreference
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class BridgingAccountLinkingFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentGotoKycBridgingAccountLinkingBinding>()

    private val args: BridgingAccountLinkingFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[BridgingAccountLinkingViewModel::class.java]
    }

    @Inject
    lateinit var kycSharedPreference: KycSharedPreference

    private val startAccountLinkingForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        viewModel.checkAccountLinkingStatus()
    }

    private val requestPermissionLocation = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            gotoCaptureKycDocuments()
        } else {
            showPermissionRejected()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGotoKycBridgingAccountLinkingBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gotoAccountLinking()
        initObserver()
        initSpannable()
        initListener()
    }

    private fun gotoAccountLinking() {
        if (viewModel.accountLinkingStatus.value !is AccountLinkingStatusResult.Linked) {
            val intent = RouteManager.getIntent(activity, ApplinkConstInternalUserPlatform.ACCOUNT_LINKING_WEBVIEW).apply {
                putExtra(
                    ApplinkConstInternalGlobal.PARAM_LD,
                    BACK_BTN_APPLINK
                )
            }
            startAccountLinkingForResult.launch(intent)
        }
    }

    private fun initObserver() {
        viewModel.accountLinkingStatus.observe(viewLifecycleOwner) {
            when (it) {
                is AccountLinkingStatusResult.Loading -> {
                    showLoadingScreen()
                }
                is AccountLinkingStatusResult.Linked -> {
                    viewModel.checkEligibility()
                }
                is AccountLinkingStatusResult.NotLinked -> {
                    activity?.setResult(Activity.RESULT_CANCELED)
                    activity?.finish()
                }
                is AccountLinkingStatusResult.Failed -> {
                    showToaster(it.throwable)
                    activity?.setResult(Activity.RESULT_CANCELED)
                    activity?.finish()
                }
            }
        }

        viewModel.checkEligibility.observe(viewLifecycleOwner) {
            when (it) {
                is CheckEligibilityResult.Progressive -> {
                    handleProgressiveFlow(it.encryptedName)
                }
                is CheckEligibilityResult.NonProgressive -> {
                    handleNonProgressiveFlow()
                }
                is CheckEligibilityResult.AwaitingApprovalGopay -> {
                    activity?.setResult(KYCConstant.ActivityResult.RELOAD)
                    activity?.finish()
                }
                is CheckEligibilityResult.Failed -> {
                    showToaster(it.throwable)
                    activity?.setResult(KYCConstant.ActivityResult.RELOAD)
                    activity?.finish()
                }
            }
        }

        viewModel.registerProgressive.observe(viewLifecycleOwner) {
            when (it) {
                is RegisterProgressiveResult.Loading -> {
                    setButtonLoading(true)
                }
                is RegisterProgressiveResult.Exhausted -> {
                    setButtonLoading(false)
                    showDobChallengeExhaustedBottomSheet(
                        cooldownTimeInSeconds = it.cooldownTimeInSeconds,
                        maximumAttemptsAllowed = it.maximumAttemptsAllowed
                    )
                }
                is RegisterProgressiveResult.RiskyUser -> {
                    setButtonLoading(false)
                    val parameter = DobChallengeParam(
                        projectId = args.parameter.projectId,
                        challengeId = it.challengeId,
                        callback = args.parameter.callback
                    )
                    gotoDobChallenge(parameter)
                }
                is RegisterProgressiveResult.NotRiskyUser -> {
                    setButtonLoading(false)
                    val parameter = StatusSubmissionParam(
                        projectId = args.parameter.projectId,
                        sourcePage = args.parameter.source,
                        gotoKycType = KYCConstant.GotoKycFlow.PROGRESSIVE,
                        status = it.status.toString(),
                        rejectionReason = it.rejectionReason,
                        callback = args.parameter.callback
                    )
                    gotoStatusSubmissionPending(parameter)
                }
                is RegisterProgressiveResult.Failed -> {
                    setButtonLoading(false)
                    showToaster(it.throwable)
                }
            }
        }
    }

    private fun showDobChallengeExhaustedBottomSheet(cooldownTimeInSeconds: String, maximumAttemptsAllowed: String) {
        val dobChallengeExhaustedBottomSheet = DobChallengeExhaustedBottomSheet.newInstance(
            projectId = args.parameter.projectId,
            source = args.parameter.source,
            cooldownTimeInSeconds = cooldownTimeInSeconds,
            maximumAttemptsAllowed = maximumAttemptsAllowed
        )

        dobChallengeExhaustedBottomSheet.show(
            childFragmentManager,
            TAG_BOTTOM_SHEET_DOB_CHALLENGE_EXHAUSTED
        )

        dobChallengeExhaustedBottomSheet.setOnDismissListener {
            activity?.setResult(KYCConstant.ActivityResult.RESULT_FINISH)
            activity?.finish()
        }
    }

    private fun showLoadingScreen() {
        binding?.apply {
            loader.show()
            unifyToolbar.hide()
            ivBridgingAccountLinking.hide()
            tvTitle.hide()
            tvSubtitle.hide()
            layoutDoneGopay.root.hide()
            layoutNotDoneGopay.root.hide()
            btnConfirm.hide()
            tvTokopediaCare.hide()
        }
    }

    private fun setButtonLoading(isLoading: Boolean) {
        binding?.btnConfirm?.isLoading = isLoading
    }

    @SuppressLint("PII Data Exposure")
    private fun handleProgressiveFlow(encryptedName: String) {
        binding?.apply {
            loader.hide()
            ivBridgingAccountLinking.loadImageWithoutPlaceholder(
                getString(R.string.img_url_goto_kyc_bridging_account_linking)
            )

            tvSubtitle.text = getString(R.string.goto_kyc_progressive_description)
            tvTitle.show()
            tvSubtitle.show()
            ivBridgingAccountLinking.show()
            btnConfirm.show()
            unifyToolbar.show()

            layoutDoneGopay.root.show()
            layoutNotDoneGopay.root.hide()

            btnConfirm.text = getString(R.string.goto_kyc_confirm_KTP_name)

            tvTokopediaCare.show()

            layoutDoneGopay.imgItem.loadImageWithoutPlaceholder(
                getString(R.string.img_url_goto_kyc_onboard_gopay)
            )
            layoutDoneGopay.icCard.show()
            layoutDoneGopay.imgItem.invisible()
            layoutDoneGopay.tvTitleKtp.text = getString(R.string.goto_kyc_bridging_progressive_item_title)
            layoutDoneGopay.tvNameKtp.text = encryptedName
        }
    }

    @SuppressLint("PII Data Exposure")
    private fun handleNonProgressiveFlow() {
        binding?.apply {
            loader.hide()
            ivBridgingAccountLinking.loadImageWithoutPlaceholder(
                getString(R.string.img_url_goto_kyc_bridging_account_linking)
            )

            tvSubtitle.text = getString(R.string.goto_kyc_bridging_not_done_gopay_subtitle)
            tvSubtitle.show()
            tvTitle.show()
            ivBridgingAccountLinking.show()
            btnConfirm.show()
            unifyToolbar.show()

            layoutDoneGopay.root.hide()
            layoutNotDoneGopay.root.show()

            btnConfirm.text = getString(R.string.goto_kyc_continue_verification)

            tvTokopediaCare.hide()

            layoutNotDoneGopay.ivKtp.loadImageWithoutPlaceholder(
                getString(R.string.img_url_goto_kyc_onboard_ktp)
            )
            layoutNotDoneGopay.ivSelfie.loadImageWithoutPlaceholder(
                getString(R.string.img_url_goto_kyc_onboard_selfie)
            )
        }
    }

    private fun initListener() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.setResult(KYCConstant.ActivityResult.RELOAD)
                    activity?.finish()
                }
            }
        )

        binding?.unifyToolbar?.setNavigationOnClickListener {
            activity?.setResult(KYCConstant.ActivityResult.RELOAD)
            activity?.finish()
        }

        binding?.btnConfirm?.setOnClickListener {
            if (viewModel.checkEligibility.value is CheckEligibilityResult.Progressive) {
                viewModel.registerProgressive(args.parameter.projectId)
            } else {
                activity?.let {
                    if (ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissionLocation.launch(Manifest.permission.CAMERA)
                    } else {
                        gotoCaptureKycDocuments()
                    }
                }
            }
        }
    }

    @SuppressLint("PII Data Exposure")
    private fun initSpannable() {
        val message = getString(R.string.goto_kyc_question_ktp_issue)
        val indexStar = message.indexOf(getString(R.string.goto_kyc_contact_tokopedia_care))
        val indexEnd = message.length

        val spannable = SpannableString(message)
        spannable.setSpan(
            object : ClickableSpan() {
                override fun onClick(view: View) {
                    goToTokopediaCare()
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = MethodChecker.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                    ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
            },
            indexStar,
            indexEnd,
            0
        )
        binding?.tvTokopediaCare?.apply {
            movementMethod = LinkMovementMethod.getInstance()
            setText(spannable, TextView.BufferType.SPANNABLE)
        }
    }

    private fun goToTokopediaCare() {
        RouteManager.route(
            context,
            String.format(
                TOKOPEDIA_CARE_STRING_FORMAT,
                ApplinkConst.WEBVIEW,
                TokopediaUrl.getInstance().WEB.plus(TOKOPEDIA_CARE_PATH)
            )
        )
    }

    private fun gotoStatusSubmissionPending(parameter: StatusSubmissionParam) {
        val toSubmissionStatusPage = BridgingAccountLinkingFragmentDirections.actionBridgingAccountLinkingFragmentToStatusSubmissionFragment(parameter)
        view?.findNavController()?.navigate(toSubmissionStatusPage)
    }

    private fun gotoDobChallenge(parameter: DobChallengeParam) {
        val toDobChallengePage = BridgingAccountLinkingFragmentDirections.actionBridgingAccountLinkingFragmentToDobChallengeFragment(parameter)
        view?.findNavController()?.navigate(toDobChallengePage)
    }

    private fun gotoCaptureKycDocuments() {
        val parameter = CaptureKycDocumentsParam(
            projectId = args.parameter.projectId,
            source = args.parameter.source,
            callback = args.parameter.callback
        )
        val toCaptureKycDocuments = BridgingAccountLinkingFragmentDirections.actionBridgingAccountLinkingFragmentToCaptureKycDocumentsFragment(parameter)
        view?.findNavController()?.navigate(toCaptureKycDocuments)
    }

    private fun showToaster(throwable: Throwable?) {
        val message = throwable.getGotoKycErrorMessage(requireContext())
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun showPermissionRejected() {
        Toaster.build(
            requireView(),
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

    override fun getScreenName(): String = ""
    override fun initInjector() {
        getComponent(GoToKycComponent::class.java).inject(this)
    }

    companion object {
        private const val BACK_BTN_APPLINK = "tokopedia://back"
        private const val TOKOPEDIA_CARE_PATH = "help/article/nama-yang-muncul-bukan-nama-saya?lang=id?isBack=true"
        private const val TOKOPEDIA_CARE_STRING_FORMAT = "%s?url=%s"
        private const val PACKAGE = "package"
        private const val TAG_BOTTOM_SHEET_DOB_CHALLENGE_EXHAUSTED = "bottom_sheet_dob_challenge_exhausted"
    }
}
