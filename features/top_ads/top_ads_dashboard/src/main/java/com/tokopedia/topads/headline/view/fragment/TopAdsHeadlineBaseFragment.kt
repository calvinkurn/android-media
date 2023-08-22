package com.tokopedia.topads.headline.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.constant.TopAdsFeature
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.AD_TYPE_SHOP_ADS
import com.tokopedia.topads.common.data.model.WhiteListUserResponse
import com.tokopedia.topads.common.recommendation.RecommendationWidget
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.data.model.FragmentTabItem
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import com.tokopedia.topads.dashboard.view.adapter.TopAdsDashboardBasePagerAdapter
import com.tokopedia.topads.dashboard.view.fragment.TopAdsBaseTabFragment
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDashDeletedGroupFragment
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDashStatisticFragment
import com.tokopedia.topads.dashboard.view.fragment.TopAdsProductIklanFragment
import com.tokopedia.topads.dashboard.view.listener.RecommendationWidgetCTAListener
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject
import kotlin.math.abs

/**
 * Created by Pika on 16/10/20.
 */

private const val CLICK_MULAI_BERIKLAN = "click - mulai beriklan"
private const val VIEW_MULAI_BERIKLAN = "view - mulai iklan toko"

open class TopAdsHeadlineBaseFragment : TopAdsBaseTabFragment() {

    private var loader: LoaderUnify? = null
    private var swipeRefreshLayout: SwipeToRefresh? = null
    private var appBarLayout: AppBarLayout? = null
    private var hariIni: ConstraintLayout? = null
    private var pager: ViewPager? = null
    private var headlineAdsViePager: ViewPager? = null
    private var headlineTabLayout: TabsUnify? = null
    private var noTabSpace: View? = null
    private var recommendationWidget: RecommendationWidget? = null
    private var recommendationWidgetCTAListener: RecommendationWidgetCTAListener? = null

    @Inject
    lateinit var presenter: TopAdsDashboardPresenter

    @Inject
    lateinit var userSession: UserSessionInterface
    private var dataStatistic: DataStatistic? = null
    private var mCurrentState = TopAdsProductIklanFragment.State.IDLE
    private var collapseStateCallBack: AppBarActionHeadline? = null
    private var currentDateText: String = ""
    private var groupPagerAdapter: TopAdsDashboardBasePagerAdapter? = null
    private var isDeletedTabEnabled: Boolean = false

    companion object {
        fun createInstance(): TopAdsHeadlineBaseFragment {
            return TopAdsHeadlineBaseFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.topads_dash_headline_layout
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun setUpView(view: View) {
        headlineAdsViePager = view.findViewById(R.id.headlineAdsViePager)
        headlineTabLayout = view.findViewById(R.id.headlineTabLayout)
        noTabSpace = view.findViewById(R.id.noTabSpace)
        loader = view.findViewById(R.id.loader)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        appBarLayout = view.findViewById(R.id.app_bar_layout_2)
        hariIni = view.findViewById(R.id.hari_ini)
        pager = view.findViewById(R.id.pager)
        recommendationWidget = view.findViewById(R.id.insightCentreEntryPointHeadline)
    }

    override fun getChildScreenName(): String {
        return TopAdsHeadlineBaseFragment::class.java.name
    }

    override fun loadChildStatisticsData() {
        swipeRefreshLayout?.isRefreshing = false
        val list = (headlineAdsViePager?.adapter as? TopAdsDashboardBasePagerAdapter)?.getList()
        list?.forEach { fragmentTabItem ->
            when (val f = fragmentTabItem.fragment) {
                is TopAdsDashDeletedGroupFragment -> {
                    f.fetchFirstPage(AD_TYPE_SHOP_ADS)
                }
                is TopAdsHeadlineShopFragment -> {
                    f.fetchFirstPage()
                }
            }
        }
        loadStatisticsData()
    }

    private fun setUpObserver() {
        presenter.groupAdsInsight.observe(viewLifecycleOwner) {
            if (it is TopAdsListAllInsightState.Success) {
                recommendationWidget?.renderWidget(it.data.remainingAdsGroup, it.data.totalAdsGroup)
                recommendationWidget?.binding?.widgetCTAButton?.setOnClickListener {
                    recommendationWidgetCTAListener?.onWidgetCTAClick()
                }
            }
        }
    }

    override fun renderGraph() {
        currentStatisticsFragment?.showLineGraph(dataStatistic)
    }

    override fun getCustomDateText(customDateText: String) {
        currentDateText = customDateText
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loader?.visibility = View.VISIBLE
        loadStatisticsData()
        swipeRefreshLayout?.setOnRefreshListener {
            loadChildStatisticsData()
        }
        appBarLayout?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, offset ->
            when {
                offset == 0 -> {
                    if (mCurrentState != TopAdsProductIklanFragment.State.EXPANDED) {
                        onStateChanged(TopAdsProductIklanFragment.State.EXPANDED)
                    }
                    mCurrentState = TopAdsProductIklanFragment.State.EXPANDED
                }
                abs(offset) >= appBarLayout.totalScrollRange -> {
                    if (mCurrentState != TopAdsProductIklanFragment.State.COLLAPSED) {
                        onStateChanged(TopAdsProductIklanFragment.State.COLLAPSED)
                    }
                    mCurrentState = TopAdsProductIklanFragment.State.COLLAPSED
                }
                else -> {
                    if (mCurrentState != TopAdsProductIklanFragment.State.IDLE) {
                        onStateChanged(TopAdsProductIklanFragment.State.IDLE)
                    }
                    mCurrentState = TopAdsProductIklanFragment.State.IDLE
                }
            }
        })
        presenter.getAdGroupWithInsight(RecommendationConstants.HEADLINE_KEY)
        setUpObserver()
    }

    private fun onSuccessWhiteListing(response: WhiteListUserResponse.TopAdsGetShopWhitelistedFeature) {
        response.data.forEach {
            when (it.featureId) {
                TopAdsFeature.DELETED_TAB_PRODUCT_HEADLINE -> isDeletedTabEnabled = true
            }
        }
    }

    private fun renderHeadlineViewPager() {
        headlineAdsViePager?.adapter = getHeadlineViewPagerAdapter()
        headlineAdsViePager?.let { headlineTabLayout?.setupWithViewPager(it) }
    }

    private fun getHeadlineViewPagerAdapter(): TopAdsDashboardBasePagerAdapter {
        val list: ArrayList<FragmentTabItem> = arrayListOf()
        headlineTabLayout?.let {
            it.getUnifyTabLayout().removeAllTabs()
            it.addNewTab(TopAdsDashboardConstant.IKLAN_TOKO)
            it.customTabMode = TabLayout.MODE_SCROLLABLE
        }
        list.add(
            FragmentTabItem(
                TopAdsDashboardConstant.IKLAN_TOKO,
                TopAdsHeadlineShopFragment.createInstance()
            )
        )
        addDeletedTab(list)
        val adapter = TopAdsDashboardBasePagerAdapter(childFragmentManager, 0)
        adapter.setList(list)
        groupPagerAdapter = adapter
        return adapter
    }

    private fun addDeletedTab(list: ArrayList<FragmentTabItem>) {
        if (isDeletedTabEnabled) {
            headlineTabLayout?.show()
            noTabSpace?.hide()
            headlineTabLayout?.customTabMode = TabLayout.MODE_FIXED
            headlineTabLayout?.addNewTab(TopAdsDashboardConstant.DIHAPUS)
            list.add(
                FragmentTabItem(
                    TopAdsDashboardConstant.DIHAPUS,
                    TopAdsDashDeletedGroupFragment.createInstance(prepareBundle())
                )
            )
        }
    }

    private fun prepareBundle(): Bundle {
        val bundle = Bundle()
        bundle.putString(ParamObject.KEY_AD_TYPE, AD_TYPE_SHOP_ADS)
        return bundle
    }

    private fun onStateChanged(state: TopAdsProductIklanFragment.State?) {
        collapseStateCallBack?.setAppBarStateHeadline(state)
        swipeRefreshLayout?.isEnabled = state == TopAdsProductIklanFragment.State.EXPANDED
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.getWhiteListedUser(::onSuccessWhiteListing) {
            presenter.getShopAdsInfo {
                val info = it.topadsGetShopInfoV2_1.data.ads.getOrNull(1)
                if (info?.type == TopAdsDashboardConstant.HEADLINE) {
                    if (!info.isUsed) {
                        showEmptyView()
                    } else {
                        renderHeadlineViewPager()
                    }
                }
            }
        }
    }

    private fun loadStatisticsData() {
        if (startDate == null || endDate == null) return
        presenter.getStatistic(
            startDate!!,
            endDate!!,
            TopAdsStatisticsType.HEADLINE_ADS,
            "-1",
            ::onSuccessGetStatisticsInfo
        )
    }

    private fun onSuccessGetStatisticsInfo(dataStatistic: DataStatistic) {
        loader?.visibility = View.GONE
        this.dataStatistic = dataStatistic
        if (this.dataStatistic != null && dataStatistic.cells.isNotEmpty()) {
            topAdsTabAdapter?.setSummary(
                dataStatistic.summary,
                resources.getStringArray(R.array.top_ads_tab_statistics_labels)
            )
            topAdsTabAdapter?.hideTabforHeadline()
        }
        val fragment = pager?.let { it.adapter?.instantiateItem(it, it.currentItem) } as? Fragment
        if (fragment != null && fragment is TopAdsDashStatisticFragment) {
            fragment.showLineGraph(this.dataStatistic)
        }
    }

    private fun showEmptyView() {
        appBarLayout?.visibility = View.GONE
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(VIEW_MULAI_BERIKLAN,
            "{${userSession.shopId}}",
            userSession.userId)
        view?.findViewById<ConstraintLayout>(R.id.empty_view)?.visibility = View.VISIBLE
        view?.findViewById<UnifyButton>(R.id.mulai_beriklan)?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(CLICK_MULAI_BERIKLAN,
                "{${userSession.shopId}}",
                userSession.userId)
            RouteManager.route(context, ApplinkConstInternalTopAds.TOPADS_HEADLINE_ADS_CREATION)
        }
        view?.findViewById<ImageUnify>(R.id.image_empty)
            ?.setImageDrawable(context?.getResDrawable(R.drawable.topads_dashboard_empty_product))
        view?.findViewById<Typography>(R.id.text_title)?.text =
            getString(R.string.topads_headline_empty_state_title)
        view?.findViewById<Typography>(R.id.text_desc)?.text =
            getString(R.string.topads_headline_empty_state_desc)
        hariIni?.visibility = View.GONE
    }

    override fun setDeletedGroupCount(size: Int) {
        headlineTabLayout?.getUnifyTabLayout()?.getTabAt(1)?.setCounter(size)
    }

    override fun setGroupCount(size: Int) {
        headlineTabLayout?.getUnifyTabLayout()?.getTabAt(0)?.setCounter(size)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AppBarActionHeadline)
            collapseStateCallBack = context

        if (context is RecommendationWidgetCTAListener){
            recommendationWidgetCTAListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        collapseStateCallBack = null
    }

    interface AppBarActionHeadline {
        fun setAppBarStateHeadline(state: TopAdsProductIklanFragment.State?)
    }

}
