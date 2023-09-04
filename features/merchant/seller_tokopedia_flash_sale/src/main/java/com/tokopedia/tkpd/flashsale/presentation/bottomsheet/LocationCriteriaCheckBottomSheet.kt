package com.tokopedia.tkpd.flashsale.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsBottomsheetLocationCriteriaCheckBinding
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaCheckingResult
import com.tokopedia.tkpd.flashsale.presentation.bottomsheet.adapter.LocationCriteriaCheckingResultAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class LocationCriteriaCheckBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<StfsBottomsheetLocationCriteriaCheckBinding>()
    private var locationCheckingResults: List<CriteriaCheckingResult.LocationCheckingResult> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        applyUnifyBackgroundColor()
        setupBottomSheet(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding = StfsBottomsheetLocationCriteriaCheckBinding.inflate(inflater, container, false)
        clearContentPadding = true
        setChild(binding?.root)
        setTitle(getString(R.string.commonbs_location_criteria_check_title))
        setupRecyclerView()
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
        tag: String?
    ) {
        this.locationCheckingResults = locationCheckingResults
        show(manager, tag)
    }
}
