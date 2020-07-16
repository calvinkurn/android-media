package com.tokopedia.topads.dashboard.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.model.FragmentTabItem
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.adapter.TopAdsDashInsightPagerAdapter
import com.tokopedia.topads.dashboard.view.adapter.TopAdsDashboardBasePagerAdapter
import kotlinx.android.synthetic.main.topads_dash_fragment_recommendation_layout.*

/**
 * Created by Pika on 9/7/20.
 */

class TopAdsRecommendationFragment : BaseDaggerFragment(){

    companion object {
        fun createInstance(): TopAdsRecommendationFragment {
            return TopAdsRecommendationFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate( R.layout.topads_dash_fragment_recommendation_layout,container,false)
    }

    override fun getScreenName(): String {
        return TopAdsRecommendationFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rb_keyword.isChecked = true
        renderViewPager()
        showFirstTimeDialog(context!!)
    }

    private fun renderViewPager() {
        view_pager.adapter = getViewPagerAdapter()
        view_pager.disableScroll(true)
    }

    private fun getViewPagerAdapter(): TopAdsDashInsightPagerAdapter? {
        val list: MutableList<FragmentTabItem> = mutableListOf()
        list.add(FragmentTabItem(resources.getString(R.string.topads_dash_beranda), TopadsKeywordInsightBase.createInstance()))
        val pagerAdapter = TopAdsDashInsightPagerAdapter(childFragmentManager, 0)
        pagerAdapter.setList(list)
        return pagerAdapter
    }
    private fun showFirstTimeDialog(context: Context) {
        val dialog = DialogUnify(context, DialogUnify.SINGLE_ACTION, DialogUnify.WITH_ICON)
        dialog.setImageDrawable(R.drawable.topads_insight_dialog)
        dialog.setDescription(context.getString(R.string.topads_dash_insight_dialog_desc))
        dialog.setTitle(context.getString(R.string.topads_dash_insight_dialog_title))
        dialog.setPrimaryCTAText(context.getString(R.string.topads_dash_insight_dialog_btn))
        dialog.setPrimaryCTAClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}