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
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.databinding.FragmentGotoKycLoaderBinding
import com.tokopedia.kyc_centralized.di.GoToKycComponent
import com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet.AwaitingApprovalGopayBottomSheet
import com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet.OnboardNonProgressiveBottomSheet
import com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet.OnboardProgressiveBottomSheet
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.CheckEligibilityResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.ProjectInfoResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.GotoKycMainActivity
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.GotoKycMainParam
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.GotoKycRouterFragment
import com.tokopedia.kyc_centralized.ui.gotoKyc.utils.getGotoKycErrorMessage
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

    private val startKycForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        kycSharedPreference.removeProjectId()
        activity?.setResult(result.resultCode)
        activity?.finish()
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
        kycSharedPreference.saveProjectId(projectId.toString())
        validationParameter(projectId = projectId, source = source)
        initObserver()
    }

    private fun validationParameter(projectId: String?, source: String?) {
        if (projectId?.toIntOrNull() == null) {
            // TODO: set toaster why activity finish
            kycSharedPreference.removeProjectId()
            activity?.finish()
        } else {
            // set value project id
            viewModel.setProjectId(projectId)
            viewModel.setSource(source.orEmpty())

            if (isReVerify) {
                viewModel.checkEligibility()
            } else {
                // please, make sure project id already set in viewModel
                viewModel.getProjectInfo(viewModel.projectId.toIntSafely())
            }
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
                    kycSharedPreference.removeProjectId()
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
                    handleAwaitingApprovalGopayFlow()
                }
                is CheckEligibilityResult.Failed -> {
                    showToaster(it.throwable)
                    kycSharedPreference.removeProjectId()
                    activity?.setResult(Activity.RESULT_CANCELED)
                    activity?.finish()
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
                sourcePage = viewModel.source
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
                sourcePage = viewModel.source
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
                sourcePage = viewModel.source
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

        onBoardProgressiveBottomSheet.setOnDismissListener {
            kycSharedPreference.removeProjectId()
            activity?.setResult(Activity.RESULT_CANCELED)
            activity?.finish()
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

        onBoardNonProgressiveBottomSheet.setOnDismissWithDataListener { isAwaitingApprovalGopay ->
            if (isAwaitingApprovalGopay) {
                showAwaitingApprovalBottomSheet()
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

        fun createInstance(): Fragment = GotoKycTransparentFragment()
    }
}
