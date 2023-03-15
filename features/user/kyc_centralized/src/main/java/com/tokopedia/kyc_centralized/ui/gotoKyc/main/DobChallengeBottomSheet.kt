package com.tokopedia.kyc_centralized.ui.gotoKyc.main

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.databinding.LayoutGotoKycDobChallengeFailedBinding
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class DobChallengeBottomSheet(private val source: String = "") : BottomSheetUnify() {

    private var binding by autoClearedNullable<LayoutGotoKycDobChallengeFailedBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutGotoKycDobChallengeFailedBinding.inflate(inflater, container, false)
        overlayClickDismiss = false
        showCloseIcon = false
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        initListener()
    }

    private fun setUpView() {
        binding?.apply {
            ivDobFailed.loadImageWithoutPlaceholder(
                getString(R.string.img_url_goto_kyc_dob_challenge_failed)
            )

            btnPrimary.text = getString(R.string.goto_kyc_back_to_source, source)
        }
    }

    private fun initListener() {
        binding?.btnPrimary?.setOnClickListener {
            activity?.setResult(Activity.RESULT_CANCELED)
            activity?.finish()
        }
    }
}
