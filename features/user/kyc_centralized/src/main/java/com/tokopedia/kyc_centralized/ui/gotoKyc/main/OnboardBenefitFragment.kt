package com.tokopedia.kyc_centralized.ui.gotoKyc.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.databinding.FragmentGotoKycOnboardBenefitBinding
import com.tokopedia.kyc_centralized.di.GoToKycComponent
import com.tokopedia.kyc_centralized.ui.gotoKyc.analytics.GotoKycAnalytics
import com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet.AwaitingApprovalGopayBottomSheet
import com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet.OnboardNonProgressiveBottomSheet
import com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet.OnboardProgressiveBottomSheet
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.utils.lifecycle.autoClearedNullable

class OnboardBenefitFragment: BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentGotoKycOnboardBenefitBinding>()

    private val args: OnboardBenefitFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGotoKycOnboardBenefitBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadInitImage()
        initListener()
    }

    override fun getScreenName(): String = ""

    private fun loadInitImage() {
        binding?.ivOnboardBenefit?.loadImageWithoutPlaceholder(
            getString(R.string.img_url_goto_kyc_onboard_benefit)
        )
    }

    private fun initListener() {
        binding?.btnVerification?.setOnClickListener {
            GotoKycAnalytics.sendClickOnButtonAmbilKeuntungan(
                projectId = args.parameter.projectId,
                kycFlowType = args.parameter.gotoKycType
            )
            showBottomSheet()
        }

        binding?.unifyToolbar?.setNavigationOnClickListener {
            GotoKycAnalytics.sendClickOnButtonBackFromOnboardingPage(
                projectId = args.parameter.projectId,
                kycFlowType = args.parameter.gotoKycType
            )
            activity?.finish()
        }
    }

    private fun showBottomSheet() {
        when (args.parameter.gotoKycType) {
            KYCConstant.GotoKycFlow.PROGRESSIVE -> {
                showProgressiveBottomSheet(args.parameter.sourcePage, args.parameter.encryptedName)
            }
            KYCConstant.GotoKycFlow.NON_PROGRESSIVE -> {
                showNonProgressiveBottomSheet(args.parameter.projectId, args.parameter.sourcePage, args.parameter.isAccountLinked)
            }
            KYCConstant.GotoKycFlow.AWAITING_APPROVAL_GOPAY -> {
                showAwaitingApprovalBottomSheet()
            }
        }
    }

    private fun showProgressiveBottomSheet(source: String, encryptedName: String) {
        val onBoardProgressiveBottomSheet = OnboardProgressiveBottomSheet.newInstance(
            projectId = args.parameter.projectId,
            source = source,
            encryptedName = encryptedName
        )

        onBoardProgressiveBottomSheet.show(
            childFragmentManager,
            TAG_BOTTOM_SHEET_ONBOARD_PROGRESSIVE
        )
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

        onBoardNonProgressiveBottomSheet.setOnDismissWithDataListener { isWaitingApprovalGopay ->
            if (isWaitingApprovalGopay) {
                showAwaitingApprovalBottomSheet()
            }
        }
    }

    private fun showAwaitingApprovalBottomSheet() {
        val awaitingApprovalGopayBottomSheet = AwaitingApprovalGopayBottomSheet()

        awaitingApprovalGopayBottomSheet.show(
            childFragmentManager,
            TAG_BOTTOM_SHEET_AWAITING_APPROVAL_GOPAY
        )
    }

    override fun initInjector() {
        getComponent(GoToKycComponent::class.java).inject(this)
    }

    companion object {
        private const val TAG_BOTTOM_SHEET_AWAITING_APPROVAL_GOPAY = "bottom_sheet_awaiting_approval_gopay"
        private const val TAG_BOTTOM_SHEET_ONBOARD_NON_PROGRESSIVE = "bottom_sheet_non_progressive"
        private const val TAG_BOTTOM_SHEET_ONBOARD_PROGRESSIVE = "bottom_sheet_progressive"
    }
}
