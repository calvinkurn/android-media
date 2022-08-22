package com.tokopedia.tkpd.flashsale.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsBottomsheetCampaignCriteriaCheckBinding
import com.tokopedia.tkpd.flashsale.presentation.bottomsheet.adapter.CampaignCriteriaCheckingResultAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class CampaignCriteriaCheckBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<StfsBottomsheetCampaignCriteriaCheckBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding = StfsBottomsheetCampaignCriteriaCheckBinding.inflate(inflater, container, false)
        clearContentPadding = true
        setChild(binding?.root)
        setTitle(getString(R.string.commonbs_category_criteria_check_title))
        binding?.rvResult?.apply {
            val criteriaItems = resources.getStringArray(R.array.criteria_items).toList()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = CampaignCriteriaCheckingResultAdapter().apply {
                setDataList(criteriaItems)
            }
        }
    }
}