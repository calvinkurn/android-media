package com.tokopedia.kyc_centralized.ui.gotoKyc.main

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.common.getMessage
import com.tokopedia.kyc_centralized.databinding.FragmentGotoKycBridgingAccountLinkingBinding
import com.tokopedia.kyc_centralized.di.GoToKycComponent
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.AccountLinkingStatusResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.CheckEligibilityResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveResult
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
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

    private val startAccountLinkingForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        viewModel.checkAccountLinkingStatus()
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
        binding?.unifyToolbar?.setNavigationOnClickListener { activity?.finish() }
        gotoAccountLinking()
        initObserver()
        initSpannable()
        initListener()
    }

    private fun gotoAccountLinking() {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalUserPlatform.ACCOUNT_LINKING_WEBVIEW).apply {
            putExtra(
                ApplinkConstInternalGlobal.PARAM_LD,
                BACK_BTN_APPLINK
            )
        }
        startAccountLinkingForResult.launch(intent)
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
                    val message = it.throwable?.getMessage(requireContext())
                    showToaster(message.orEmpty())
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
                is CheckEligibilityResult.Failed -> {
                    val message = it.throwable?.getMessage(requireContext())
                    showToaster(message.orEmpty())
                    activity?.setResult(Activity.RESULT_CANCELED)
                    activity?.finish()
                }
            }
        }

        viewModel.registerProgressive.observe(viewLifecycleOwner) {
            when (it) {
                is RegisterProgressiveResult.Loading -> {
                    setButtonLoading(true)
                }
                is RegisterProgressiveResult.RiskyUser -> {
                    setButtonLoading(false)
                    val parameter = DobChallengeParam(
                        projectId = args.parameter.projectId,
                        challengeId = it.challengeId
                    )
                    gotoDobChallenge(parameter)
                }
                is RegisterProgressiveResult.NotRiskyUser -> {
                    setButtonLoading(false)
                    val parameter = StatusSubmissionParam(
                        isCameFromAccountPage = args.parameter.source == KYCConstant.GotoKycSourceAccountPage,
                        sourcePage = args.parameter.source,
                        gotoKycType = KYCConstant.GotoKycFlow.PROGRESSIVE,
                        status = "0"
                    )
                    gotoStatusSubmissionPending(parameter)
                }
                is RegisterProgressiveResult.Failed -> {
                    setButtonLoading(false)
                    val message = it.throwable?.getMessage(requireContext())
                    showToaster(message.orEmpty())
                }
            }
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

    private fun handleProgressiveFlow(encryptedName: String) {
        binding?.apply {
            loader.hide()
            ivBridgingAccountLinking.loadImageWithoutPlaceholder(
                getString(R.string.img_url_goto_kyc_bridging_account_linking)
            )

            tvSubtitle.text = getString(R.string.goto_kyc_bridging_done_gopay_subtitle)

            layoutDoneGopay.root.show()
            layoutNotDoneGopay.root.hide()

            btnConfirm.text = getString(R.string.goto_kyc_confirm_KTP_name)

            tvTokopediaCare.show()

            layoutDoneGopay.imgItem.loadImageWithoutPlaceholder(
                getString(R.string.img_url_goto_kyc_onboard_gopay)
            )
            layoutDoneGopay.tvNameKtp.text = encryptedName
        }
    }

    private fun handleNonProgressiveFlow() {
        binding?.apply {
            loader.hide()
            ivBridgingAccountLinking.loadImageWithoutPlaceholder(
                getString(R.string.img_url_goto_kyc_bridging_account_linking)
            )

            tvSubtitle.text = getString(R.string.goto_kyc_bridging_not_done_gopay_subtitle)

            layoutDoneGopay.root.hide()
            layoutNotDoneGopay.root.show()

            btnConfirm.text = getString(R.string.goto_kyc_bridging_not_done_gopay_button)

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
        binding?.btnConfirm?.setOnClickListener {
            if (viewModel.checkEligibility.value is CheckEligibilityResult.Progressive) {
                viewModel.registerProgressiveUseCase(args.parameter.projectId)
            } else {
                goToCaptureKycDocuments()
            }
        }
    }

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
                        com.tokopedia.unifyprinciples.R.color.Unify_G500
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

    private fun goToCaptureKycDocuments() {

    }

    private fun showToaster(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun getScreenName(): String = ""
    override fun initInjector() {
        getComponent(GoToKycComponent::class.java).inject(this)
    }

    companion object {
        private const val BACK_BTN_APPLINK = "tokopedia://back"
        private const val TOKOPEDIA_CARE_PATH = "help"
        private const val TOKOPEDIA_CARE_STRING_FORMAT = "%s?url=%s"
    }
}
