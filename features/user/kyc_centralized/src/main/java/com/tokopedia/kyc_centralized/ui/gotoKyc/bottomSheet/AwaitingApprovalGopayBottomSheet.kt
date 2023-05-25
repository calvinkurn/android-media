package com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.databinding.LayoutGotoKycAwaitingApprovalGopayBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class AwaitingApprovalGopayBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<LayoutGotoKycAwaitingApprovalGopayBinding>()


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

}
