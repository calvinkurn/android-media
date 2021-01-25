package com.tokopedia.topads.dashboard.view.fragment.insight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.PRODUK
import com.tokopedia.topads.dashboard.data.model.DailyBudgetRecommendationModel
import com.tokopedia.topads.dashboard.data.model.ProductRecommendationData
import com.tokopedia.topads.dashboard.data.model.ProductRecommendationModel
import com.tokopedia.topads.dashboard.data.model.TopadsGetDailyBudgetRecommendation
import com.tokopedia.topads.dashboard.data.model.insightkey.InsightKeyData
import com.tokopedia.topads.dashboard.data.model.insightkey.KeywordInsightDataMain
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.adapter.TopAdsDashInsightPagerAdapter
import com.tokopedia.topads.dashboard.view.adapter.insight.TopAdsInsightTabAdapter
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import kotlinx.android.synthetic.main.topads_dash_fragment_recommendation_layout.*
import kotlinx.android.synthetic.main.topads_dash_group_empty_state.view.*
import java.util.*
import javax.inject.Inject

/**
 * Created by Pika on 9/7/20.
 */

class TopAdsRecommendationFragment : BaseDaggerFragment() {

    private var productRecommendData: ProductRecommendationData? = null
    private var keywordRecommendData: InsightKeyData? = null
    private var dailyBudgetRecommendData: TopadsGetDailyBudgetRecommendation? = null
    private var countKey = 0
    private var countProduct = 0
    private var countBid = 0

    companion object {
        fun createInstance(): TopAdsRecommendationFragment {
            return TopAdsRecommendationFragment()
        }
    }

    @Inject
    lateinit var topAdsDashboardPresenter: TopAdsDashboardPresenter


    private val topAdsInsightTabAdapter: TopAdsInsightTabAdapter? by lazy {
        context?.run { TopAdsInsightTabAdapter() }
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
        topAdsDashboardPresenter.getProductRecommendation(::onSuccessProductRecommendation)
        topAdsDashboardPresenter.getDailyBudgetRecommendation(::onSuccessBudgetRecommendation)
    }

    private fun onSuccessBudgetRecommendation(dailyBudgetRecommendationModel: DailyBudgetRecommendationModel) {
        dailyBudgetRecommendData = dailyBudgetRecommendationModel.topadsGetDailyBudgetRecommendation
        countBid = dailyBudgetRecommendData?.data?.size ?: 0
        checkAllData()
    }

    private fun onSuccessGetInsightData(response: InsightKeyData) {
        keywordRecommendData = response
        val data: HashMap<String, KeywordInsightDataMain> = response.data
        countKey = data.size
        checkAllData()
    }

    private fun onSuccessProductRecommendation(productRecommendationModel: ProductRecommendationModel) {
        productRecommendData = productRecommendationModel.topadsGetProductRecommendation.data
        countProduct = productRecommendData?.products?.size ?: 0
        checkAllData()
    }

    private fun checkAllData() {
        if (productRecommendData == null || keywordRecommendData == null || dailyBudgetRecommendData == null)
            return
        if (countProduct == 0 && countBid == 0 && countKey == 0) {
            setEmptyView()
        } else {
            if (countProduct != 0)
                bottomInsight.visibility = View.VISIBLE
            initInsightTabAdapter()
            renderViewPager()
            topAdsInsightTabAdapter?.setTabTitles(resources, countProduct, countBid, countKey)
        }
    }

    private fun setEmptyView() {
        loderRecom?.visibility = View.GONE
        rvTabInsight?.visibility = View.GONE
        empty_view?.visibility = View.VISIBLE
        empty_view?.image_empty?.setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.ill_success))
        view_pager?.visibility = View.GONE
    }

    private fun initInsightTabAdapter() {
        val tabLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rvTabInsight.layoutManager = tabLayoutManager
        topAdsInsightTabAdapter?.setListener(object : TopAdsInsightTabAdapter.OnRecyclerTabItemClick {
            override fun onTabItemClick(position: Int) {
                view_pager.currentItem = position
                if (position == 0 && topAdsInsightTabAdapter?.getTab()?.get(position)?.contains(PRODUK) == true) {
                    setPadding()
                    bottomInsight.visibility = View.VISIBLE
                } else {
                    bottomInsight.visibility = View.GONE
                    view_pager?.setPadding(0, 0, 0,  0)
                }
            }
        })
        rvTabInsight.adapter = topAdsInsightTabAdapter
        view_pager.offscreenPageLimit = TopAdsDashboardConstant.OFFSCREEN_PAGE_LIMIT
    }

     fun setPadding() {
        editProduct?.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val height = editProduct?.measuredHeight
        view_pager?.setPadding(0, 0, 0, height ?: 0)
    }

    private fun renderViewPager() {
        loderRecom?.visibility = View.GONE
        view_pager.adapter = getViewPagerAdapter()
        view_pager.disableScroll(true)
    }

    fun setCount(count: Int, type: Int) {
        when (type) {
            0 -> topAdsInsightTabAdapter?.setTabTitles(resources, count, countBid, countKey)
            1 -> topAdsInsightTabAdapter?.setTabTitles(resources, countProduct, count, countKey)
            2 -> topAdsInsightTabAdapter?.setTabTitles(resources, countProduct, countBid, count)
        }
    }

    private fun getViewPagerAdapter(): TopAdsDashInsightPagerAdapter? {
        val list: ArrayList<Fragment> = arrayListOf()
        if (countProduct != 0)
            list.add(TopAdsInsightBaseProductFragment(productRecommendData))
        if (countBid != 0)
            list.add(TopAdsInsightBaseBidFragment(dailyBudgetRecommendData))
        if (countKey != 0)
            list.add(TopadsInsightBaseKeywordFragment.createInstance())
        val pagerAdapter = TopAdsDashInsightPagerAdapter(childFragmentManager, 0)
        pagerAdapter.setList(list)
        return pagerAdapter
    }

    fun setEmpty() {
        bottomInsight?.visibility = View.GONE
    }

    fun setEnable(enable: Boolean) {
        editProduct?.isEnabled = enable
    }

    fun setClick() {
        editProduct.setOnClickListener {
            val fragments = (view_pager?.adapter as? TopAdsDashInsightPagerAdapter)?.listFrag
            if (fragments?.get(0) is TopAdsInsightBaseProductFragment) {
                (fragments[0] as TopAdsInsightBaseProductFragment).openBottomSheet()
            }
        }
    }
}