package com.tokopedia.tkpd.flashsale.presentation.list.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsBottomsheetCampaignDetailBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class CampaignDetailBottomSheet: BottomSheetUnify() {

    private var binding by autoClearedNullable<StfsBottomsheetCampaignDetailBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupBottomSheet() {
        binding = StfsBottomsheetCampaignDetailBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        setTitle("Detail Campaign")
    }

}