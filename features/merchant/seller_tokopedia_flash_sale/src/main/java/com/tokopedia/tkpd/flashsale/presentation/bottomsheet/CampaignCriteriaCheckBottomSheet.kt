package com.tokopedia.tkpd.flashsale.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsBottomsheetCampaignCriteriaCheckBinding
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaCheckingResult
import com.tokopedia.tkpd.flashsale.presentation.bottomsheet.adapter.CampaignCriteriaCheckingResultAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class CampaignCriteriaCheckBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<StfsBottomsheetCampaignCriteriaCheckBinding>()
    private var criteriaCheckingResults: List<CriteriaCheckingResult> = emptyList()

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
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = CampaignCriteriaCheckingResultAdapter().apply {
                setDataList(criteriaCheckingResults)
                setOnTickerClick(::onListTickerClick)
            }
        }
    }

    private fun onListTickerClick(list: List<CriteriaCheckingResult.LocationCheckingResult>) {
        val bottomSheet = LocationCriteriaCheckBottomSheet()
        bottomSheet.show(list, childFragmentManager, "")
    }


    fun show(
        criteriaCheckingResults: List<CriteriaCheckingResult>,
        manager: FragmentManager,
        tag: String?
    ) {
        this.criteriaCheckingResults = criteriaCheckingResults
        show(manager, tag)
    }
}