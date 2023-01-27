package com.tokopedia.kyc_centralized.ui.gotoKyc.transparent

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.common.getMessage
import com.tokopedia.kyc_centralized.databinding.FragmentGotoKycLoaderBinding
import com.tokopedia.kyc_centralized.di.GoToKycComponent
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.ProjectInfoResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.GotoKycMainActivity
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.GotoKycMainParam
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.RouterFragment
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class GotoKycTransparentFragment: BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentGotoKycLoaderBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            GotoKycTransparentViewModel::class.java
        )
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
            showToaster(getString(R.string.goto_kyc_transparent_something_wrong))
            activity?.finish()
        } else {
            //set value project id
            viewModel.setProjectId(projectId)
            viewModel.setSource(source.orEmpty())

            //please, make sure project id already set in viewModel
            viewModel.getProjectInfo(viewModel.projectId.toInt())
        }
    }

    private fun initObserver() {
        viewModel.projectInfo.observe(viewLifecycleOwner) {
            binding?.gotoKycLoader?.invisible()

            val parameter = GotoKycMainParam(
                projectId = viewModel.projectId,
                isCameFromAccountPage = viewModel.source.isEmpty(),
                sourcePage = viewModel.source,
                status = it.status,
                dataSource = it.dataSource,
                listReason = it.listReason
            )

            when (it) {
                is ProjectInfoResult.TokoKyc -> {
                    gotoTokoKyc(viewModel.projectId)
                }
                is ProjectInfoResult.StatusSubmission -> {
                    gotoStatusSubmission(parameter)
                }
                is ProjectInfoResult.Progressive -> {
                    showProgressiveBottomSheet(
                        source = viewModel.source
                    )
                }
                is ProjectInfoResult.NonProgressive -> {
                    showNonProgressiveBottomSheet(
                        source = viewModel.source,
                        isAccountLinked = it.isAccountLinked == true,
                        isKtpTaken = false
                    )
                }
                is ProjectInfoResult.Failed -> {
                    val message = it.throwable?.getMessage(requireContext())
                    showToaster(message.orEmpty())
                    activity?.finish()
                }
            }
        }
    }

    private fun gotoTokoKyc(projectId: String) {
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalUserPlatform.KYC_INFO,
            projectId
        )
        startActivity(intent)
        activity?.finish()
    }

    private fun gotoStatusSubmission(parameter: GotoKycMainParam) {
        val intent = Intent(activity, GotoKycMainActivity::class.java)
        intent.putExtra(RouterFragment.PARAM_REQUEST_PAGE, RouterFragment.PAGE_STATUS_SUBMISSION)
        intent.putExtra(RouterFragment.PARAM_DATA, parameter)
        startActivity(intent)
        activity?.finish()
    }

    private fun showProgressiveBottomSheet(source: String) {
        val onBoardProgressiveBottomSheet = OnboardProgressiveBottomSheet(
            source = source
        )

        onBoardProgressiveBottomSheet.show(
            childFragmentManager,
            TAG_BOTTOM_SHEET_ONBOARD_PROGRESSIVE
        )

        onBoardProgressiveBottomSheet.setOnDismissListener {
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
