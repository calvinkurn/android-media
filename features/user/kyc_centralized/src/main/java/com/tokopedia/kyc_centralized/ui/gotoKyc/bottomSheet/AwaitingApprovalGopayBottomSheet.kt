package com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet

import android.animation.ValueAnimator
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.databinding.LayoutGotoKycAwaitingApprovalGopayBinding
import com.tokopedia.kyc_centralized.ui.gotoKyc.analytics.GotoKycAnalytics
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class AwaitingApprovalGopayBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<LayoutGotoKycAwaitingApprovalGopayBinding>()
    private var projectId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            projectId = it.getString(PROJECT_ID).orEmpty()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutGotoKycAwaitingApprovalGopayBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadInitImage()
        initListener()

        GotoKycAnalytics.sendViewOnPendingBottomSheetOnboardingPage(projectId)
    }

    private fun initListener() {
        binding?.btnSubmit?.setOnClickListener {
            dismiss()
        }
    }

    private fun loadInitImage() {
        binding?.ivStatusSubmissionLottie?.apply {
            val imageUrl = getString(R.string.img_url_goto_kyc_status_submission_pending)
            LottieCompositionFactory.fromUrl(requireContext(), imageUrl).addListener { result: LottieComposition? ->
                result?.let { setComposition(it) }
                repeatCount = ValueAnimator.INFINITE
                playAnimation()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        GotoKycAnalytics.sendClickCloseOnPendingBottomSheetOnboardingPage(projectId)
    }

    companion object {
        private const val PROJECT_ID = "project_id"

        fun newInstance(projectId: String) =
            AwaitingApprovalGopayBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(PROJECT_ID, projectId)
                }
            }
    }

}
