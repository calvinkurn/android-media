package com.tokopedia.topads.dashboard.view.fragment.insight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.model.insightkey.InsightKeyData
import com.tokopedia.topads.dashboard.data.model.insightkey.KeywordInsightDataMain
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.adapter.TopAdsDashInsightPagerAdapter
import com.tokopedia.topads.dashboard.view.adapter.insight.TopAdsInsightTabAdapter
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import kotlinx.android.synthetic.main.topads_dash_fragment_recommendation_layout.*
import java.util.*
import javax.inject.Inject

/**
 * Created by Pika on 9/7/20.
 */

class TopAdsRecommendationFragment : BaseDaggerFragment() {

    companion object {
        fun createInstance(): TopAdsRecommendationFragment {
            return TopAdsRecommendationFragment()
        }
    }

    @Inject
    lateinit var topAdsDashboardPresenter: TopAdsDashboardPresenter


    private val topAdsInsightTabAdapter: TopAdsInsightTabAdapter? by lazy {
        context?.run { TopAdsInsightTabAdapter(this) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.topads_dash_fragment_recommendation_layout, container, false)
    }

    override fun getScreenName(): String {
        return TopAdsRecommendationFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topAdsDashboardPresenter.getInsight(resources, ::onSuccessGetInsightData)
        Utils.setSearchListener(context, view, ::onSearch)
    }

    private fun onSuccessGetInsightData(response: InsightKeyData) {
        val data: HashMap<String, KeywordInsightDataMain> = response.data
        if (data.isEmpty()) {
            setEmptyView()
        } else {
            initInsightTabAdapter()
            renderViewPager()
        }
    }

    private fun setEmptyView() {
        rvTabInsight.visibility = View.GONE
        empty_view.visibility = View.VISIBLE
        view_pager.visibility = View.GONE
        searchBar.visibility = View.GONE
    }

    private fun onSearch() {
        val fragments = (view_pager?.adapter as TopAdsDashInsightPagerAdapter?)?.listFrag
        if (fragments?.get(0) is TopadsInsightBaseKeywordFragment) {
            (fragments[0] as TopadsInsightBaseKeywordFragment).searchAction(searchBar.searchBarTextField.text.toString())
        }
    }

    private fun initInsightTabAdapter() {
        val tabLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rvTabInsight.layoutManager = tabLayoutManager
        topAdsInsightTabAdapter?.setListener(object : TopAdsInsightTabAdapter.OnRecyclerTabItemClick {
            override fun onTabItemClick(position: Int) {
                view_pager.currentItem = position
            }
        })
        rvTabInsight.adapter = topAdsInsightTabAdapter
        view_pager.offscreenPageLimit = TopAdsDashboardConstant.OFFSCREEN_PAGE_LIMIT
    }

    private fun renderViewPager() {
        view_pager.adapter = getViewPagerAdapter()
        view_pager.disableScroll(true)
    }

    fun setCount(count: Int) {
        topAdsInsightTabAdapter?.setTabTitles(resources, count, 0, 0)
    }

    private fun getViewPagerAdapter(): TopAdsDashInsightPagerAdapter? {
        val list: ArrayList<Fragment> = arrayListOf()
        list.add(TopadsInsightBaseKeywordFragment.createInstance())
        list.add(TopAdsInsightBaseProductFragment())
        list.add(TopAdsInsightBaseBidFragment())
        val pagerAdapter = TopAdsDashInsightPagerAdapter(childFragmentManager, 0)
        pagerAdapter.setList(list)
        return pagerAdapter
    }
}