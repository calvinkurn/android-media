package com.tokopedia.topads.dashboard.view.fragment.insightbottomsheet

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.view.adapter.insight.InsightAdObj
import com.tokopedia.topads.dashboard.view.adapter.insight.TopAdsInsightAdsTypeAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.topads_insight_select_ads_type_bottomsheet.*

class TopAdsInsightAdsTypeBottomSheet(
    adsList: List<InsightAdObj>,
    onAdSelected: (Int,InsightAdObj) -> Unit
) : BottomSheetUnify() {

    private val mAdapter by lazy { TopAdsInsightAdsTypeAdapter(adsList, onAdSelected) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val childView =
            View.inflate(context, R.layout.topads_insight_select_ads_type_bottomsheet, null)
        setChild(childView)
        setSheetValues()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
    }

    private fun setSheetValues() {
        showCloseIcon = true
        isHideable = true
        setTitle(getString(R.string.topads_insight_select_ads_type))
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, ADS_TYPE_BOTTOM_SHEET_TAG)
    }

    private fun initRecyclerView() {
        with(rvSelectAdsTypeTopAdsInsight) {

            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            hasFixedSize()
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    companion object {
        private const val ADS_TYPE_BOTTOM_SHEET_TAG = "ads_type_bottom_sheet"
        fun getInstance(
                list: List<InsightAdObj>,
                onAdSelected: (Int,InsightAdObj) -> Unit
        ) = TopAdsInsightAdsTypeBottomSheet(list, onAdSelected)
    }
}