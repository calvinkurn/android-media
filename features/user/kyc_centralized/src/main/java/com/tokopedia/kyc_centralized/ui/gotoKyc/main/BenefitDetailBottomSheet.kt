package com.tokopedia.kyc_centralized.ui.gotoKyc.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.databinding.LayoutGotoKycBenefitDetailBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class BenefitDetailBottomSheet: BottomSheetUnify() {

    private var binding by autoClearedNullable<LayoutGotoKycBenefitDetailBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutGotoKycBenefitDetailBinding.inflate(inflater, container, false)
        setTitle(getString(R.string.goto_kyc_benefit_detail_bottom_sheet_title))
        setChild(binding?.root)
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

}
