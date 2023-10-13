package com.tokopedia.kyc_centralized.ui.gotoKyc.main.router

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kyc_centralized.databinding.FragmentGotoKycLoaderBinding
import com.tokopedia.kyc_centralized.di.GoToKycComponent
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.challenge.DobChallengeParam
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.onboard.OnboardBenefitParam
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.bridging.BridgingAccountLinkingParam
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.capture.CaptureKycDocumentsParam
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.status.StatusSubmissionParam
import com.tokopedia.utils.lifecycle.autoClearedNullable

class GotoKycRouterFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentGotoKycLoaderBinding>()

    private var sourcePage = ""
    private var data: GotoKycMainParam? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intentExtras = requireActivity().intent.extras
        sourcePage = intentExtras?.getString(PARAM_REQUEST_PAGE).orEmpty()

        data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intentExtras?.getParcelable(PARAM_DATA, GotoKycMainParam::class.java)
        } else {
            @Suppress("DEPRECATION")
            intentExtras?.getParcelable(PARAM_DATA)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGotoKycLoaderBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @SuppressLint("PII Data Exposure")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (sourcePage) {
            PAGE_STATUS_SUBMISSION -> {
                val parameter = StatusSubmissionParam(
                    projectId = data?.projectId.orEmpty(),
                    gotoKycType = data?.gotoKycType.orEmpty(),
                    status = data?.status.orEmpty(),
                    sourcePage = data?.sourcePage.orEmpty(),
                    rejectionReason = data?.rejectionReason.orEmpty(),
                    waitMessage = data?.waitMessage.orEmpty(),
                    callback = data?.callback.orEmpty()
                )
                gotoStatusSubmission(parameter)
            }
            PAGE_ONBOARD_BENEFIT -> {
                val parameter = OnboardBenefitParam(
                    projectId = data?.projectId.orEmpty(),
                    gotoKycType = data?.gotoKycType.orEmpty(),
                    encryptedName = data?.encryptedName.orEmpty(),
                    isAccountLinked = data?.isAccountLinked.orFalse(),
                    isKtpTaken = data?.isKtpTaken.orFalse(),
                    isSelfieTaken = data?.isSelfieTaken.orFalse(),
                    sourcePage = data?.sourcePage.orEmpty(),
                    directShowBottomSheet = data?.directShowBottomSheet.orFalse(),
                    callback = data?.callback.orEmpty()
                )
                gotoOnboardBenefitGotoKyc(parameter)
            }
            PAGE_DOB_CHALLENGE -> {
                val parameter = DobChallengeParam(
                    projectId = data?.projectId.orEmpty(),
                    challengeId = data?.challengeId.orEmpty(),
                    pageSource = data?.sourcePage.orEmpty(),
                    callback = data?.callback.orEmpty()
                )
                gotoDobChallenge(parameter)
            }
            PAGE_BRIDGING_ACCOUNT_LINKING -> {
                val parameter = BridgingAccountLinkingParam(
                    projectId = data?.projectId.orEmpty(),
                    source = data?.sourcePage.orEmpty(),
                    callback = data?.callback.orEmpty()
                )
                gotoBridgingAccountLinking(parameter)
            }
            PAGE_CAPTURE_KYC_DOCUMENTS -> {
                val parameter = CaptureKycDocumentsParam(
                    projectId = data?.projectId.orEmpty(),
                    source = data?.sourcePage.orEmpty(),
                    callback = data?.callback.orEmpty()
                )
                gotoCaptureKycDocuments(parameter)
            }
            else -> {
                activity?.setResult(Activity.RESULT_CANCELED)
                activity?.finish()
            }
        }
        sourcePage = ""
    }

    private fun gotoOnboardBenefitGotoKyc(parameter: OnboardBenefitParam) {
        val toOnboardBenefitPage = GotoKycRouterFragmentDirections.actionRouterFragmentToGotoKycOnboardBenefitFragment(parameter)
        view?.findNavController()?.navigate(toOnboardBenefitPage)
    }

    private fun gotoStatusSubmission(parameter: StatusSubmissionParam) {
        val toSubmissionStatusPage = GotoKycRouterFragmentDirections.actionRouterFragmentToStatusSubmissionFragment(parameter)
        view?.findNavController()?.navigate(toSubmissionStatusPage)
    }

    private fun gotoDobChallenge(parameter: DobChallengeParam) {
        val toDobChallengePage = GotoKycRouterFragmentDirections.actionRouterFragmentToDobChallengeFragment(parameter)
        view?.findNavController()?.navigate(toDobChallengePage)
    }

    private fun gotoBridgingAccountLinking(parameter: BridgingAccountLinkingParam) {
        val toBridgingAccountLinking = GotoKycRouterFragmentDirections.actionRouterFragmentToBridgingAccountLinkingFragment(parameter)
        view?.findNavController()?.navigate(toBridgingAccountLinking)
    }

    private fun gotoCaptureKycDocuments(parameter: CaptureKycDocumentsParam) {
        val toCaptureKycDocuments = GotoKycRouterFragmentDirections.actionRouterFragmentToCaptureKycDocumentsFragment(parameter)
        view?.findNavController()?.navigate(toCaptureKycDocuments)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(GoToKycComponent::class.java).inject(this)
    }

    companion object {
        const val PARAM_REQUEST_PAGE = "request_page"
        const val PAGE_CAPTURE_KYC_DOCUMENTS = "page_capture_kyc_documents"
        const val PAGE_ONBOARD_BENEFIT = "page_onboard_benefit"
        const val PAGE_STATUS_SUBMISSION = "page_status_submission"
        const val PAGE_BRIDGING_ACCOUNT_LINKING = "page_bridging_page"
        const val PAGE_DOB_CHALLENGE = "page_challenge"
        const val PARAM_DATA = "parameter"
    }
}
