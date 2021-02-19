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
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_0
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_1
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_2
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.DAILY_BUDGET
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.KATA_KUNCI
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.PARAM_CURRENT_TAB
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.PRODUK
import com.tokopedia.topads.dashboard.data.model.DailyBudgetRecommendationModel
import com.tokopedia.topads.dashboard.data.model.ProductRecommendationData
import com.tokopedia.topads.dashboard.data.model.ProductRecommendationModel
import com.tokopedia.topads.dashboard.data.model.TopadsGetDailyBudgetRecommendation
import com.tokopedia.topads.dashboard.data.model.insightkey.InsightKeyData
import com.tokopedia.topads.dashboard.data.model.insightkey.KeywordInsightDataMain
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity
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
    private var index = 0

    companion object {
        const val HEIGHT = "addp_bar_height"
        fun createInstance(height: Int?, redirectToTabInsight: Int): TopAdsRecommendationFragment {
            val bundle = Bundle()
            bundle.putInt(HEIGHT, height ?: 0)
            bundle.putInt(PARAM_CURRENT_TAB, redirectToTabInsight)
            val fragment = TopAdsRecommendationFragment()
            fragment.arguments = bundle
            return fragment
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
        (activity as TopAdsDashboardActivity?)?.hideButton(countProduct == 0)
        if (countProduct == 0 && countBid == 0 && countKey == 0) {
            setEmptyView()
        } else {
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
                if (position == 0 && topAdsInsightTabAdapter?.getTab()?.get(position)?.contains(PRODUK) == true && countProduct != 0) {
                    (activity as TopAdsDashboardActivity?)?.hideButton(false)
                } else {
                    (activity as TopAdsDashboardActivity?)?.hideButton(true)
                }
            }
        })
        rvTabInsight.adapter = topAdsInsightTabAdapter
        view_pager.offscreenPageLimit = TopAdsDashboardConstant.OFFSCREEN_PAGE_LIMIT
    }

    private fun renderViewPager() {
        loderRecom?.visibility = View.GONE
        view_pager.adapter = getViewPagerAdapter()
        val page = arguments?.getInt(PARAM_CURRENT_TAB)
        if (ifPageAvailable(page)) {
            if (page == CONST_0)
                index = CONST_0
            if (page == CONST_1) {
                index = if (checkFragmentPosition(CONST_0, DAILY_BUDGET))
                    CONST_0
                else
                    CONST_1
            }
            if (page == CONST_2) {
                if (checkFragmentPosition(CONST_0, KATA_KUNCI))
                    index = CONST_0
                index = if (checkFragmentPosition(CONST_1, KATA_KUNCI))
                    CONST_1
                else
                    CONST_2
            }
        }
        view_pager?.currentItem = index
        topAdsInsightTabAdapter?.setSelectedTitle(index)
        view_pager.disableScroll(true)
    }

    private fun ifPageAvailable(page: Int?): Boolean {
        return when (page) {
            CONST_0 -> isProductAvailable()
            CONST_1 -> isDailyBudgetAvailable()
            CONST_2 -> isKeywordAvailable()
            else -> false
        }
    }

    private fun isKeywordAvailable(): Boolean {
        if (topAdsInsightTabAdapter?.itemCount ?: 0 >= CONST_1)
            return when (topAdsInsightTabAdapter?.itemCount) {
                CONST_1 -> {
                    checkFragmentPosition(CONST_0, KATA_KUNCI)
                }
                CONST_2 -> {
                    return !(!checkFragmentPosition(CONST_0, KATA_KUNCI) && !checkFragmentPosition(CONST_1, KATA_KUNCI))
                }
                else -> true
            }
        return false
    }

    private fun isDailyBudgetAvailable(): Boolean {
        if (topAdsInsightTabAdapter?.itemCount ?: 0 >= CONST_1)
            return if (topAdsInsightTabAdapter?.itemCount == CONST_1) {
                checkFragmentPosition(CONST_0, DAILY_BUDGET)
            } else {
                !(!checkFragmentPosition(CONST_0, DAILY_BUDGET) && !checkFragmentPosition(CONST_1, DAILY_BUDGET))
            }
        return false
    }

    private fun checkFragmentPosition(index: Int, value: String): Boolean {
        return topAdsInsightTabAdapter?.getTab()?.get(index)?.contains(value) == true
    }

    private fun isProductAvailable(): Boolean {
        if (topAdsInsightTabAdapter?.itemCount ?: 0 >= CONST_1)
            return topAdsInsightTabAdapter?.getTab()?.get(CONST_0)?.contains(PRODUK) == true
        return false
    }

    fun setCount(count: Int, type: Int) {
        when (type) {
            CONST_0 -> topAdsInsightTabAdapter?.setTabTitles(resources, count, countBid, countKey)
            CONST_1 -> topAdsInsightTabAdapter?.setTabTitles(resources, countProduct, count, countKey)
            CONST_2 -> topAdsInsightTabAdapter?.setTabTitles(resources, countProduct, countBid, count)
        }
    }

    private fun getViewPagerAdapter(): TopAdsDashInsightPagerAdapter? {
        val list: ArrayList<Fragment> = arrayListOf()
        if (countProduct != 0)
            list.add(TopAdsInsightBaseProductFragment(productRecommendData, arguments?.getInt(HEIGHT)))
        if (countBid != 0)
            list.add(TopAdsInsightBaseBidFragment(dailyBudgetRecommendData))
        if (countKey != 0)
            list.add(TopadsInsightBaseKeywordFragment.createInstance())
        val pagerAdapter = TopAdsDashInsightPagerAdapter(childFragmentManager, 0)
        pagerAdapter.setList(list)
        return pagerAdapter
    }

    fun setClick() {
        val fragments = (view_pager?.adapter as? TopAdsDashInsightPagerAdapter)?.listFrag
        if (fragments?.firstOrNull() is TopAdsInsightBaseProductFragment?) {
            (fragments?.get(0) as TopAdsInsightBaseProductFragment).openBottomSheet()
        }
    }

    fun setEmptyProduct() {
        countProduct = 0
    }

    fun checkButtonVisibility() {
        if (productRecommendData != null)
            (activity as TopAdsDashboardActivity?)?.hideButton(countProduct == 0)
    }
}