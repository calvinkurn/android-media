package com.tokopedia.tkpd.flashsale.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsBottomsheetCampaignCriteriaCheckBinding
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaCheckingResult
import com.tokopedia.tkpd.flashsale.presentation.bottomsheet.adapter.CampaignCriteriaCheckingResultAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class CampaignCriteriaCheckBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<StfsBottomsheetCampaignCriteriaCheckBinding>()
    private var criteriaCheckingResults: List<CriteriaCheckingResult> = emptyList()
    private var productName: String = ""
    private var productImageUrl: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyUnifyBackgroundColor()
        setupProductPreview()
        setupCheckingList(binding?.rvResult ?: return)
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding = StfsBottomsheetCampaignCriteriaCheckBinding.inflate(inflater, container, false)
        clearContentPadding = true
        isFullpage = true
        setChild(binding?.root)
        setTitle(getString(R.string.commonbs_category_criteria_check_title))
        val isMultiLoc = criteriaCheckingResults.firstOrNull { it.isMultiloc } != null
        binding?.tfDescription?.text = if (isMultiLoc) getString(R.string.commonbs_criteria_check_description)
        else getString(R.string.commonbs_criteria_variant_check_description)
    }

    private fun setupCheckingList(rvResult: RecyclerView) {
        rvResult.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = CampaignCriteriaCheckingResultAdapter().apply {
                setDataList(criteriaCheckingResults)
                setOnTickerClick(::onListTickerClick)
            }
        }
    }

    private fun setupProductPreview() {
        binding?.apply {
            tfProductName.text = productName
            imgProduct.setImageUrl(productImageUrl)
        }
    }

    private fun onListTickerClick(list: List<CriteriaCheckingResult.LocationCheckingResult>) {
        val bottomSheet = LocationCriteriaCheckBottomSheet()
        bottomSheet.show(list, childFragmentManager, "")
    }

    fun setProductPreview(productName: String, productImageUrl: String) {
        this.productName = productName
        this.productImageUrl = productImageUrl
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
