package com.tokopedia.topads.dashboard.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.FragmentTabItem
import com.tokopedia.topads.dashboard.view.adapter.TopAdsDashboardBasePagerAdapter
import com.tokopedia.topads.dashboard.view.fragment.insightbottomsheet.TopAdsInsightSheetScreen1
import com.tokopedia.topads.dashboard.view.fragment.insightbottomsheet.TopAdsInsightSheetScreen2
import com.tokopedia.topads.dashboard.view.fragment.insightbottomsheet.TopAdsInsightSheetScreen3
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.topads_insight_bottom_sheet.*

/**
 * Created by Pika on 21/7/20.
 */

class InsightKeyBottomSheet : BottomSheetUnify() {

    private var contentView: View? = null
    private lateinit var adapter: TopAdsDashboardBasePagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        setCloseClickListener {
            dismiss()
        }
        renderTabAndViewPager()
    }

    companion object {
        private var POSITION = 0
        fun createInstance(pos: Int): InsightKeyBottomSheet {
            POSITION = pos
            return InsightKeyBottomSheet()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        contentView = View.inflate(context, R.layout.topads_insight_bottom_sheet, null)
        contentView?.height?.div(2)?.let { height ->
            customPeekHeight = height
        }
        setChild(contentView)
        context?.getString(R.string.topads_dash_insight_sheet_info_title)?.let { setTitle(it) }
    }

    private fun renderTabAndViewPager() {
        viewPagerBottomSheet?.adapter = getViewPagerAdapter()
        viewPagerBottomSheet?.currentItem = POSITION
    }

    private fun getViewPagerAdapter(): TopAdsDashboardBasePagerAdapter {
        val list: ArrayList<FragmentTabItem> = ArrayList()
        list.add(FragmentTabItem("", TopAdsInsightSheetScreen1()))
        list.add(FragmentTabItem("", TopAdsInsightSheetScreen2()))
        list.add(FragmentTabItem("", TopAdsInsightSheetScreen3()))
        adapter = TopAdsDashboardBasePagerAdapter(childFragmentManager, 0)
        adapter.setList(list)
        return adapter
    }
}