package com.tokopedia.topads.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.topads.create.R
import com.tokopedia.topads.create.databinding.LayoutBottomsheetListBinding
import com.tokopedia.topads.view.activity.SeePerformanceTopadsActivity
import com.tokopedia.topads.view.adapter.ItemListAdapter
import com.tokopedia.topads.view.adapter.ItemListTypeFactory
import com.tokopedia.topads.view.uimodel.ItemListUiModel
import com.tokopedia.topads.view.utils.ScheduleSlotListener
import com.tokopedia.unifycomponents.BottomSheetUnify

class ListBottomSheet:
    BottomSheetUnify(), ScheduleSlotListener {

    private var itemList: List<ItemListUiModel> = listOf()

    private val adapterItemList: ItemListAdapter by lazy {
        ItemListAdapter(ItemListTypeFactory(this))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initView() {
        clearContentPadding = true

        val binding = LayoutBottomsheetListBinding.inflate(layoutInflater).apply {
            rvScheduleSlot.apply {
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                this.adapter = adapterItemList
                adapterItemList.setData(itemList)
            }

            rvScheduleSlot.addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        setChild(binding.root)
    }

    companion object {

        fun show(fm: FragmentManager, title:String, list: List<ItemListUiModel>): ListBottomSheet {
            val bottomSheet = ListBottomSheet().apply {
                setTitle(title)
                itemList = list
            }
            bottomSheet.show(fm, "")
            return bottomSheet
        }
    }

    override fun onClickItemListener(title: String) {
        when(title){
            getString(R.string.ads_active) -> (activity as SeePerformanceTopadsActivity).updateStatusIklan("2")
            getString(R.string.topads_non_active) -> (activity as SeePerformanceTopadsActivity).updateStatusIklan("3")
            getString(R.string.topads_ads_performance_all_placements_filter_title) -> (activity as SeePerformanceTopadsActivity).updateAdsPlacingFilter(1)
            getString(R.string.topads_ads_performance_in_search_filter_title) -> (activity as SeePerformanceTopadsActivity).updateAdsPlacingFilter(2)
            getString(R.string.topads_ads_performance_in_recommendation_filter_title) -> (activity as SeePerformanceTopadsActivity).updateAdsPlacingFilter(3)
            else -> (activity as SeePerformanceTopadsActivity).updateDateFilter(title)
        }
        this.dismiss()
    }
}
