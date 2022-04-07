package com.tokopedia.topads.headline.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.AD_TYPE_SHOP_ADS
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.data.model.FragmentTabItem
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.adapter.TopAdsDashboardBasePagerAdapter
import com.tokopedia.topads.dashboard.view.fragment.TopAdsBaseTabFragment
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDashDeletedGroupFragment
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDashStatisticFragment
import com.tokopedia.topads.dashboard.view.fragment.TopAdsProductIklanFragment
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import com.tokopedia.unifycomponents.TabsUnify
import com.tokopedia.unifycomponents.setCounter
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.partial_top_ads_dashboard_statistics.*
import kotlinx.android.synthetic.main.topads_dash_headline_layout.*
import kotlinx.android.synthetic.main.topads_dash_product_iklan_empty_view.*
import kotlinx.android.synthetic.main.topads_dash_product_iklan_empty_view.view.*
import java.util.*
import javax.inject.Inject
import kotlin.math.abs

/**
 * Created by Pika on 16/10/20.
 */

private const val CLICK_MULAI_BERIKLAN = "click - mulai beriklan"
private const val VIEW_MULAI_BERIKLAN = "view - mulai iklan toko"

open class TopAdsHeadlineBaseFragment : TopAdsBaseTabFragment() {

    private var dataStatistic: DataStatistic? = null

    @Inject
    lateinit var presenter: TopAdsDashboardPresenter

    @Inject
    lateinit var userSession: UserSessionInterface
    private var mCurrentState = TopAdsProductIklanFragment.State.IDLE
    private var collapseStateCallBack: AppBarActionHeadline? = null
    private var currentDateText: String = ""
    private lateinit var headlineAdsViePager: ViewPager
    private lateinit var headlineTabLayout: TabsUnify
    private var groupPagerAdapter: TopAdsDashboardBasePagerAdapter? = null


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
    }

    override fun getChildScreenName(): String {
        return TopAdsHeadlineBaseFragment::class.java.name
    }

    override fun loadChildStatisticsData() {
        swipe_refresh_layout.isRefreshing = false
        val list = (headlineAdsViePager.adapter as? TopAdsDashboardBasePagerAdapter)?.getList()
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

    override fun renderGraph() {
        currentStatisticsFragment?.showLineGraph(dataStatistic)
    }

    override fun getCustomDateText(customDateText: String) {
        currentDateText = customDateText
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loader.visibility = View.VISIBLE
        loadStatisticsData()
        swipe_refresh_layout.setOnRefreshListener {
            loadChildStatisticsData()
        }
        app_bar_layout_2?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, offset ->
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
    }

    private fun renderHeadlineViewPager() {
        headlineAdsViePager.adapter = getHeadlineViewPagerAdapter()
        headlineTabLayout.setupWithViewPager(headlineAdsViePager)
    }

    private fun getHeadlineViewPagerAdapter(): TopAdsDashboardBasePagerAdapter {
        val list: ArrayList<FragmentTabItem> = arrayListOf()
        headlineTabLayout.getUnifyTabLayout().removeAllTabs()
        headlineTabLayout.addNewTab(TopAdsDashboardConstant.IKLAN_TOKO)
        headlineTabLayout.addNewTab(TopAdsDashboardConstant.DIHAPUS)
        list.add(
            FragmentTabItem(
                TopAdsDashboardConstant.IKLAN_TOKO,
                TopAdsHeadlineShopFragment.createInstance()
            )
        )
        list.add(
            FragmentTabItem(
                TopAdsDashboardConstant.DIHAPUS,
                TopAdsDashDeletedGroupFragment.createInstance(prepareBundle())
            )
        )
        val adapter = TopAdsDashboardBasePagerAdapter(childFragmentManager, 0)
        adapter.setList(list)
        groupPagerAdapter = adapter
        return adapter
    }

    private fun prepareBundle(): Bundle {
        val bundle = Bundle()
        bundle.putString(ParamObject.KEY_AD_TYPE, AD_TYPE_SHOP_ADS)
        return bundle
    }

    private fun onStateChanged(state: TopAdsProductIklanFragment.State?) {
        collapseStateCallBack?.setAppBarStateHeadline(state)
        swipe_refresh_layout.isEnabled = state == TopAdsProductIklanFragment.State.EXPANDED
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.getShopAdsInfo {
            val info = it.topadsGetShopInfoV2.data.ads[1]
            if (info.type == "headline") {
                if (!info.isUsed) {
                    showEmptyView()
                } else {
                    renderHeadlineViewPager()
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
        loader.visibility = View.GONE
        this.dataStatistic = dataStatistic
        if (this.dataStatistic != null && dataStatistic.cells.isNotEmpty()) {
            topAdsTabAdapter?.setSummary(
                dataStatistic.summary,
                resources.getStringArray(R.array.top_ads_tab_statistics_labels)
            )
            topAdsTabAdapter?.hideTabforHeadline()
        }
        val fragment = pager.adapter?.instantiateItem(pager, pager.currentItem) as? Fragment
        if (fragment != null && fragment is TopAdsDashStatisticFragment) {
            fragment.showLineGraph(this.dataStatistic)
        }
    }

    private fun showEmptyView() {
        app_bar_layout_2?.visibility = View.GONE
        empty_view?.visibility = View.VISIBLE
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(VIEW_MULAI_BERIKLAN, "{${userSession.shopId}}", userSession.userId)
        mulai_beriklan?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(CLICK_MULAI_BERIKLAN, "{${userSession.shopId}}", userSession.userId)
            RouteManager.route(context, ApplinkConstInternalTopAds.TOPADS_HEADLINE_ADS_CREATION)
        }
        empty_view.image_empty.setImageDrawable(context?.getResDrawable(R.drawable.topads_dashboard_empty_product))
        text_title.text = getString(R.string.topads_headline_empty_state_title)
        text_desc.text = getString(R.string.topads_headline_empty_state_desc)
        hari_ini?.visibility = View.GONE
    }

    override fun setDeletedGroupCount(size: Int) {
        headlineTabLayout.getUnifyTabLayout().getTabAt(1)?.setCounter(size)
    }

    override fun setGroupCount(size: Int) {
        headlineTabLayout.getUnifyTabLayout().getTabAt(0)?.setCounter(size)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AppBarActionHeadline)
            collapseStateCallBack = context
    }

    override fun onDetach() {
        super.onDetach()
        collapseStateCallBack = null
    }

    interface AppBarActionHeadline {
        fun setAppBarStateHeadline(state: TopAdsProductIklanFragment.State?)
    }

}