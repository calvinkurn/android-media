package com.tokopedia.tkpd.flashsale.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsBottomsheetLocationCriteriaCheckBinding
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaCheckingResult
import com.tokopedia.tkpd.flashsale.presentation.bottomsheet.adapter.LocationCriteriaCheckingResultAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class LocationCriteriaCheckBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<StfsBottomsheetLocationCriteriaCheckBinding>()
    private var locationCheckingResults: List<CriteriaCheckingResult.LocationCheckingResult> = emptyList()
    private var isVariant: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding = StfsBottomsheetLocationCriteriaCheckBinding.inflate(inflater, container, false)
        clearContentPadding = true
        setChild(binding?.root)
        setTitle(getString(R.string.commonbs_location_criteria_check_title))
        setupRecyclerView()
        binding?.tfProductName?.text = if (isVariant) getString(R.string.commonbs_criteria_check_description)
        else getString(R.string.commonbs_criteria_single_check_description)
    }

    private fun setupRecyclerView() {
        binding?.rvResult?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = LocationCriteriaCheckingResultAdapter().apply {
                setDataList(locationCheckingResults)
            }
        }
    }

    fun show(
        locationCheckingResults: List<CriteriaCheckingResult.LocationCheckingResult>,
        manager: FragmentManager,
        tag: String?,
        isVariant: Boolean = false
    ) {
        this.isVariant = isVariant
        this.locationCheckingResults = locationCheckingResults
        show(manager, tag)
    }
}