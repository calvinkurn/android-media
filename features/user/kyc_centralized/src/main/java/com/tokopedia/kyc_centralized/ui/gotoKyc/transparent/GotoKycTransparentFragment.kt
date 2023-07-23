package com.tokopedia.kyc_centralized.ui.gotoKyc.transparent

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gojek.OneKycSdk
import com.gojek.kyc.plus.OneKycConstants
import com.gojek.kyc.plus.getKycSdkDocumentDirectoryPath
import com.gojek.kyc.plus.getKycSdkFrameDirectoryPath
import com.gojek.kyc.plus.getKycSdkLogDirectoryPath
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.common.KycServerLogger
import com.tokopedia.kyc_centralized.databinding.FragmentGotoKycLoaderBinding
import com.tokopedia.kyc_centralized.di.GoToKycComponent
import com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet.AwaitingApprovalGopayBottomSheet
import com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet.DobChallengeExhaustedBottomSheet
import com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet.FailedSavePreferenceBottomSheet
import com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet.OnboardNonProgressiveBottomSheet
import com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet.OnboardProgressiveBottomSheet
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.AccountLinkingStatusResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.CheckEligibilityResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.ProjectInfoResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.router.GotoKycMainActivity
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.router.GotoKycMainParam
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.router.GotoKycRouterFragment
import com.tokopedia.kyc_centralized.ui.gotoKyc.utils.getGotoKycErrorMessage
import com.tokopedia.kyc_centralized.ui.gotoKyc.utils.removeGotoKycImage
import com.tokopedia.kyc_centralized.ui.gotoKyc.utils.removeGotoKycPreference
import com.tokopedia.kyc_centralized.util.KycSharedPreference
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class GotoKycTransparentFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentGotoKycLoaderBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[GotoKycTransparentViewModel::class.java]
    }

    @Inject
    lateinit var kycSharedPreference: KycSharedPreference

    @Inject
    lateinit var oneKycSdk: OneKycSdk

    private var isReVerify = false
    private var directShowBottomSheetInOnboardBenefit = false

    private val startKycForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        when (result.resultCode) {
            KYCConstant.ActivityResult.RELOAD -> {
                directShowBottomSheetInOnboardBenefit = true
                viewModel.getProjectInfo(viewModel.projectId.toIntSafely())
            }
            else -> {
                finishWithResult(result.resultCode)
            }
        }
    }

    private fun finishWithResult(result: Int) {
        kycSharedPreference.removeProjectId()
        activity?.setResult(result)
        activity?.finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        removeGotoKycCache()
    }

    private fun removeGotoKycCache() {
        val preferenceName = OneKycConstants.KYC_SDK_PREFERENCE_NAME
        val preferenceKey = OneKycConstants.KYC_UPLOAD_PROGRESS_STATE
        removeGotoKycPreference(
            context = requireContext(),
            preferenceName = preferenceName,
            preferenceKey = preferenceKey
        )

        val directory1 = getKycSdkDocumentDirectoryPath(requireContext())
        val directory2 = getKycSdkFrameDirectoryPath(requireContext())
        val directory3 = getKycSdkLogDirectoryPath(requireContext())
        removeGotoKycImage(directory1)
        removeGotoKycImage(directory2)
        removeGotoKycImage(directory3)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGotoKycLoaderBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        oneKycSdk.init()
        val projectId = activity?.intent?.extras?.getString(ApplinkConstInternalUserPlatform.PARAM_PROJECT_ID)
        val source = activity?.intent?.extras?.getString(ApplinkConstInternalUserPlatform.PARAM_SOURCE)
        isReVerify = activity?.intent?.extras?.getBoolean(IS_RE_VERIFY).orFalse()
        validationParameter(projectId = projectId, source = source)
        initObserver()
    }

    private fun validationParameter(projectId: String?, source: String?) {
        if (projectId?.toIntOrNull() == null) {
            finishWithResult(Activity.RESULT_CANCELED)
        } else {
            // set value project id
            viewModel.setProjectId(projectId)
            viewModel.setSource(source.orEmpty())

            saveInitDataToPreference()
        }
    }

    private fun saveInitDataToPreference() {
        binding?.gotoKycLoader?.show()
        val isSuccessSavePreference = kycSharedPreference.saveProjectId(viewModel.projectId)

        KycServerLogger.sendLogStatusSavePreferenceKyc(
            flow = KycServerLogger.FLOW_GOTO_KYC,
            isSuccess = isSuccessSavePreference
        )

        if (isSuccessSavePreference) {
            if (isReVerify) {
                viewModel.accountLikingStatus()
            } else {
                // please, make sure project id already set in viewModel
                viewModel.getProjectInfo(viewModel.projectId.toIntSafely())
            }
        } else {
            binding?.gotoKycLoader?.invisible()
            showFailedSavePreferenceBottomSheet()
        }
    }

    private fun initObserver() {
        viewModel.projectInfo.observe(viewLifecycleOwner) {
            binding?.gotoKycLoader?.invisible()

            when (it) {
                is ProjectInfoResult.TokoKyc -> {
                    gotoTokoKyc(viewModel.projectId)
                }
                is ProjectInfoResult.StatusSubmission -> {
                    val parameter = GotoKycMainParam(
                        projectId = viewModel.projectId,
                        sourcePage = viewModel.source,
                        status = it.status,
                        rejectionReason = it.rejectionReason,
                        waitMessage = it.waitMessage
                    )
                    gotoStatusSubmission(parameter)
                }
                is ProjectInfoResult.NotVerified -> {
                    if (it.isAccountLinked) {
                        viewModel.checkEligibility()
                    } else {
                        handleNonProgressiveFlow()
                    }
                }
                is ProjectInfoResult.Failed -> {
                    showToaster(it.throwable)
                    finishWithResult(Activity.RESULT_CANCELED)
                }
            }
        }

        viewModel.checkEligibility.observe(viewLifecycleOwner) {
            binding?.gotoKycLoader?.invisible()

            when (it) {
                is CheckEligibilityResult.Progressive -> {
                    handleProgressiveFlow(it.encryptedName)
                }
                is CheckEligibilityResult.NonProgressive -> {
                    handleNonProgressiveFlow()
                }
                is CheckEligibilityResult.AwaitingApprovalGopay -> {
                    handleAwaitingApprovalGopayFlow()
                }
                is CheckEligibilityResult.Failed -> {
                    showToaster(it.throwable)
                    finishWithResult(Activity.RESULT_CANCELED)
                }
            }
        }

        viewModel.accountLinkingStatus.observe(viewLifecycleOwner) {
            when (it) {
                is AccountLinkingStatusResult.Loading -> {
                    binding?.gotoKycLoader?.show()
                }
                is AccountLinkingStatusResult.Linked -> {
                    viewModel.checkEligibility()
                }
                is AccountLinkingStatusResult.NotLinked -> {
                    binding?.gotoKycLoader?.invisible()
                    handleNonProgressiveFlow()
                }
                is AccountLinkingStatusResult.Failed -> {
                    binding?.gotoKycLoader?.hide()
                    showToaster(it.throwable)
                    finishWithResult(Activity.RESULT_CANCELED)
                }
            }
        }
    }

    private fun handleProgressiveFlow(encryptedName: String) {
        if (viewModel.projectId == KYCConstant.PROJECT_ID_ACCOUNT) {
            val parameter = GotoKycMainParam(
                projectId = viewModel.projectId,
                gotoKycType = KYCConstant.GotoKycFlow.PROGRESSIVE,
                encryptedName = encryptedName,
                isAccountLinked = viewModel.isAccountLinked,
                sourcePage = viewModel.source,
                directShowBottomSheet = directShowBottomSheetInOnboardBenefit
            )
            gotoOnboardBenefit(parameter)
        } else {
            showProgressiveBottomSheet(
                source = viewModel.source,
                encryptedName = encryptedName
            )
        }
    }

    private fun handleNonProgressiveFlow() {
        if (viewModel.projectId == KYCConstant.PROJECT_ID_ACCOUNT) {
            val parameter = GotoKycMainParam(
                projectId = viewModel.projectId,
                gotoKycType = KYCConstant.GotoKycFlow.NON_PROGRESSIVE,
                isAccountLinked = viewModel.isAccountLinked,
                sourcePage = viewModel.source,
                directShowBottomSheet = directShowBottomSheetInOnboardBenefit
            )
            gotoOnboardBenefit(parameter)
        } else {
            showNonProgressiveBottomSheet(
                projectId = viewModel.projectId,
                source = viewModel.source,
                isAccountLinked = viewModel.isAccountLinked
            )
        }
    }

    private fun handleAwaitingApprovalGopayFlow() {
        if (viewModel.projectId == KYCConstant.PROJECT_ID_ACCOUNT) {
            val parameter = GotoKycMainParam(
                projectId = viewModel.projectId,
                gotoKycType = KYCConstant.GotoKycFlow.AWAITING_APPROVAL_GOPAY,
                sourcePage = viewModel.source,
                directShowBottomSheet = directShowBottomSheetInOnboardBenefit
            )
            gotoOnboardBenefit(parameter)
        } else {
            showAwaitingApprovalBottomSheet()
        }
    }

    private fun gotoTokoKyc(projectId: String) {
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalUserPlatform.KYC_INFO,
            projectId
        )
        startKycForResult.launch(intent)
    }

    private fun gotoOnboardBenefit(parameter: GotoKycMainParam) {
        val intent = Intent(activity, GotoKycMainActivity::class.java)
        intent.putExtra(GotoKycRouterFragment.PARAM_REQUEST_PAGE, GotoKycRouterFragment.PAGE_ONBOARD_BENEFIT)
        intent.putExtra(GotoKycRouterFragment.PARAM_DATA, parameter)
        startKycForResult.launch(intent)
    }

    private fun gotoStatusSubmission(parameter: GotoKycMainParam) {
        val intent = Intent(activity, GotoKycMainActivity::class.java)
        intent.putExtra(GotoKycRouterFragment.PARAM_REQUEST_PAGE, GotoKycRouterFragment.PAGE_STATUS_SUBMISSION)
        intent.putExtra(GotoKycRouterFragment.PARAM_DATA, parameter)
        startKycForResult.launch(intent)
    }

    private fun showDobChallengeExhaustedBottomSheet(cooldownTimeInSeconds: String, maximumAttemptsAllowed: String) {
        val dobChallengeExhaustedBottomSheet = DobChallengeExhaustedBottomSheet.newInstance(
            projectId = viewModel.projectId,
            source = viewModel.source,
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

    private fun showProgressiveBottomSheet(source: String, encryptedName: String) {
        val onBoardProgressiveBottomSheet = OnboardProgressiveBottomSheet.newInstance(
            projectId = viewModel.projectId,
            source = source,
            encryptedName = encryptedName
        )

        onBoardProgressiveBottomSheet.show(
            childFragmentManager,
            TAG_BOTTOM_SHEET_ONBOARD_PROGRESSIVE
        )

        onBoardProgressiveBottomSheet.setOnDismissWithDataListener { dobChallengeExhaustedParam ->
            if (dobChallengeExhaustedParam.isExhausted) {
                showDobChallengeExhaustedBottomSheet(
                    cooldownTimeInSeconds = dobChallengeExhaustedParam.cooldownTimeInSeconds,
                    maximumAttemptsAllowed = dobChallengeExhaustedParam.maximumAttemptsAllowed
                )
            } else {
                finishWithResult(Activity.RESULT_CANCELED)
            }
        }
    }

    private fun showNonProgressiveBottomSheet(projectId: String, source: String, isAccountLinked: Boolean) {
        val onBoardNonProgressiveBottomSheet = OnboardNonProgressiveBottomSheet.newInstance(
            projectId = projectId,
            source = source,
            isAccountLinked = isAccountLinked
        )

        onBoardNonProgressiveBottomSheet.show(
            childFragmentManager,
            TAG_BOTTOM_SHEET_ONBOARD_NON_PROGRESSIVE
        )

        onBoardNonProgressiveBottomSheet.setOnDismissWithDataListener { isReload ->
            if (isReload) {
                viewModel.getProjectInfo(viewModel.projectId.toIntSafely())
            } else {
                kycSharedPreference.removeProjectId()
                activity?.setResult(Activity.RESULT_CANCELED)
                activity?.finish()
            }
        }
    }

    private fun showAwaitingApprovalBottomSheet() {
        val awaitingApprovalGopayBottomSheet = AwaitingApprovalGopayBottomSheet.newInstance(
            projectId = viewModel.projectId
        )

        awaitingApprovalGopayBottomSheet.show(
            childFragmentManager,
            TAG_BOTTOM_SHEET_AWAITING_APPROVAL_GOPAY
        )

        awaitingApprovalGopayBottomSheet.setOnDismissListener {
            kycSharedPreference.removeProjectId()
            activity?.setResult(Activity.RESULT_CANCELED)
            activity?.finish()
        }
    }

    private fun showFailedSavePreferenceBottomSheet() {
        val failedSavePreferenceBottomSheet = FailedSavePreferenceBottomSheet()

        failedSavePreferenceBottomSheet.show(
            childFragmentManager,
            TAG_BOTTOM_SHEET_FAILED_SAVE_PREFERENCE
        )

        failedSavePreferenceBottomSheet.setOnDismissWithDataListener { isReload ->
            if (isReload) {
                saveInitDataToPreference()
            } else {
                activity?.setResult(Activity.RESULT_CANCELED)
                activity?.finish()
            }
        }
    }

    private fun showToaster(throwable: Throwable?) {
        val message = throwable?.getGotoKycErrorMessage(requireContext())
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(GoToKycComponent::class.java).inject(this)
    }

    companion object {
        const val IS_RE_VERIFY = "isReVerify"
        private const val TAG_BOTTOM_SHEET_AWAITING_APPROVAL_GOPAY = "bottom_sheet_awaiting_approval_gopay"
        private const val TAG_BOTTOM_SHEET_ONBOARD_NON_PROGRESSIVE = "bottom_sheet_non_progressive"
        private const val TAG_BOTTOM_SHEET_ONBOARD_PROGRESSIVE = "bottom_sheet_progressive"
        private const val TAG_BOTTOM_SHEET_FAILED_SAVE_PREFERENCE = "bottom_sheet_failed_save_preference"
        private const val TAG_BOTTOM_SHEET_DOB_CHALLENGE_EXHAUSTED = "bottom_sheet_dob_challenge_exhausted"

        fun createInstance(): Fragment = GotoKycTransparentFragment()
    }
}
