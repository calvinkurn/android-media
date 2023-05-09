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
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.common.getMessage
import com.tokopedia.kyc_centralized.databinding.FragmentGotoKycLoaderBinding
import com.tokopedia.kyc_centralized.di.GoToKycComponent
import com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet.OnboardNonProgressiveBottomSheet
import com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet.OnboardProgressiveBottomSheet
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.CheckEligibilityResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.ProjectInfoResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.GotoKycMainActivity
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.GotoKycMainParam
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.GotoKycRouterFragment
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class GotoKycTransparentFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentGotoKycLoaderBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[GotoKycTransparentViewModel::class.java]
    }

    private val startKycForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
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
        val projectId = activity?.intent?.extras?.getString(ApplinkConstInternalUserPlatform.PARAM_PROJECT_ID)
        val source = activity?.intent?.extras?.getString(ApplinkConstInternalUserPlatform.PARAM_SOURCE)
        validationParameter(projectId = projectId, source = source)
        initObserver()
    }

    private fun validationParameter(projectId: String?, source: String?) {
        if (projectId?.toIntOrNull() == null) {
            // TODO: set toaster why activity finish
            activity?.finish()
        } else {
            // set value project id
            viewModel.setProjectId(projectId)
            viewModel.setSource(source.orEmpty())

            // please, make sure project id already set in viewModel
            viewModel.getProjectInfo(viewModel.projectId.toInt())
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
                        listReason = it.listReason
                    )
                    gotoStatusSubmission(parameter)
                }
                is ProjectInfoResult.NotVerified -> {
                    if (!it.isAccountLinked) {
                        val parameter = GotoKycMainParam(
                            projectId = viewModel.projectId,
                            sourcePage = viewModel.source
                        )
                        gotoBridgingAccountLinking(parameter)
                    } else {
                        viewModel.checkEligibility()
                    }
                }
                is ProjectInfoResult.Failed -> {
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
    }

    private fun handleProgressiveFlow(encryptedName: String) {
        if (viewModel.source == KYCConstant.GotoKycSourceAccountPage) {
            val parameter = GotoKycMainParam(
                gotoKycType = KYCConstant.GotoKycFlow.PROGRESSIVE,
                encryptedName = encryptedName,
                isAccountLinked = viewModel.projectInfo.value?.isAccountLinked == true,
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
        if (viewModel.source == KYCConstant.GotoKycSourceAccountPage) {
            val parameter = GotoKycMainParam(
                gotoKycType = KYCConstant.GotoKycFlow.NON_PROGRESSIVE,
                isAccountLinked = viewModel.projectInfo.value?.isAccountLinked == true,
                isKtpTaken = true,
                sourcePage = viewModel.source
            )
            gotoOnboardBenefit(parameter)
        } else {
            showNonProgressiveBottomSheet(
                source = viewModel.source,
                isAccountLinked = viewModel.projectInfo.value?.isAccountLinked == true,
                isKtpTaken = true
            )
        }
    }

    private fun gotoBridgingAccountLinking(parameter: GotoKycMainParam) {
        val intent = Intent(activity, GotoKycMainActivity::class.java)
        intent.putExtra(GotoKycRouterFragment.PARAM_REQUEST_PAGE, GotoKycRouterFragment.PAGE_BRIDGING_ACCOUNT_LINKING)
        intent.putExtra(GotoKycRouterFragment.PARAM_DATA, parameter)
        startKycForResult.launch(intent)
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
        val onBoardProgressiveBottomSheet = OnboardProgressiveBottomSheet(
            projectId = viewModel.projectId,
            source = source,
            encryptedName = encryptedName
        )

        onBoardProgressiveBottomSheet.show(
            childFragmentManager,
            TAG_BOTTOM_SHEET_ONBOARD_PROGRESSIVE
        )

        onBoardProgressiveBottomSheet.setOnDismissListener {
            activity?.setResult(Activity.RESULT_CANCELED)
            activity?.finish()
        }
    }

    private fun showNonProgressiveBottomSheet(source: String, isAccountLinked: Boolean, isKtpTaken: Boolean) {
        val onBoardNonProgressiveBottomSheet = OnboardNonProgressiveBottomSheet(
            source = source,
            isAccountLinked = isAccountLinked,
            isKtpTaken = isKtpTaken
        )

        onBoardNonProgressiveBottomSheet.show(
            childFragmentManager,
            TAG_BOTTOM_SHEET_ONBOARD_NON_PROGRESSIVE
        )

        onBoardNonProgressiveBottomSheet.setOnDismissListener {
            activity?.setResult(Activity.RESULT_CANCELED)
            activity?.finish()
        }
    }

    private fun showToaster(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun getScreenName(): String = SCREEN_NAME

    override fun initInjector() {
        getComponent(GoToKycComponent::class.java).inject(this)
    }

    companion object {
        private const val TAG_BOTTOM_SHEET_ONBOARD_NON_PROGRESSIVE = "bottom_sheet_non_progressive"
        private const val TAG_BOTTOM_SHEET_ONBOARD_PROGRESSIVE = "bottom_sheet_progressive"
        private val SCREEN_NAME = GotoKycTransparentFragment::class.java.simpleName

        fun createInstance(): Fragment = GotoKycTransparentFragment()
    }
}
