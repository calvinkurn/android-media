package com.tokopedia.topads.headline.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.response.GroupInfoResponse
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION_ACTIVATE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION_DEACTIVATE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.EDIT_GROUP_REQUEST_CODE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.EDIT_HEADLINE_REQUEST_CODE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.GROUP_ID
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.IS_CHANGED
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.KATA_KUNCI
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.data.model.FragmentTabItem
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.activity.TopAdsBaseDetailActivity
import com.tokopedia.topads.dashboard.view.adapter.TopAdsDashboardBasePagerAdapter
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDashStatisticFragment
import com.tokopedia.topads.dashboard.view.fragment.TopAdsProductIklanFragment
import com.tokopedia.topads.dashboard.view.model.GroupDetailViewModel
import com.tokopedia.topads.headline.view.fragment.TopAdsHeadlineKeyFragment
import com.tokopedia.unifycomponents.setCounter
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.partial_top_ads_dashboard_statistics.*
import kotlinx.android.synthetic.main.topads_dash_headline_detail_layout.*
import kotlinx.android.synthetic.main.topads_dash_headline_detail_view_widget.*
import javax.inject.Inject
import kotlin.math.abs

/**
 * Created by Pika on 16/10/20.
 */

private const val click_edit_icon = "click - edit on detail iklan toko"
private const val click_toggle_icon = "click - toggle on detail iklan toko"
private const val view_detail_iklan = "view - detail iklan toko"
class TopAdsHeadlineAdDetailViewActivity : TopAdsBaseDetailActivity(), HasComponent<TopAdsDashboardComponent>, CompoundButton.OnCheckedChangeListener {

    private var dataStatistic: DataStatistic? = null
    private var selectedStatisticType: Int = 0
    private var groupId: Int? = 0
    private var priceSpent: String? = ""
    private var groupStatus: String? = ""
    private var groupName: String? = ""
    private var priceDaily = 0
    private var groupTotal = 0
    private var isDataChanged = false

    @Inject
    lateinit var userSession: UserSessionInterface

    private var mCurrentState = TopAdsProductIklanFragment.State.IDLE

    companion object {
        private const val ACTIVE = "1"
        private const val TIDAK_TAMPIL = "2"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {

        ViewModelProvider(this, viewModelFactory).get(GroupDetailViewModel::class.java)
    }

    private lateinit var detailPagerAdapter: TopAdsDashboardBasePagerAdapter

    private fun renderTabAndViewPager() {
        viewPagerHeadline.adapter = getViewPagerAdapter()
        viewPagerHeadline.offscreenPageLimit = 2
        viewPagerHeadline.currentItem = 0
        tab_layout?.setupWithViewPager(viewPagerHeadline)
    }

    private fun getViewPagerAdapter(): PagerAdapter {
        val list: MutableList<FragmentTabItem> = mutableListOf()
        tab_layout?.getUnifyTabLayout()?.removeAllTabs()
        tab_layout?.addNewTab(KATA_KUNCI)
        tab_layout?.customTabMode = TabLayout.MODE_SCROLLABLE
        val bundle = Bundle()
        bundle.putInt(GROUP_ID, groupId ?: 0)
        list.add(FragmentTabItem(KATA_KUNCI, TopAdsHeadlineKeyFragment.createInstance(bundle)))
        detailPagerAdapter = TopAdsDashboardBasePagerAdapter(supportFragmentManager, 0)
        detailPagerAdapter.setList(list)
        return detailPagerAdapter
    }

    override fun getLayoutId(): Int {
        return R.layout.topads_dash_headline_detail_layout
    }

    override fun loadChildStatisticsData() {
        loadData()
        loadStatisticsData()
    }

    override fun renderGraph() {
        currentStatisticsFragment?.showLineGraph(dataStatistic)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        selectedStatisticType = TopAdsStatisticsType.PRODUCT_ADS
        getBundleArguments()
        loadData()
        loadStatisticsData()
        renderTabAndViewPager()
        swipe_refresh_layout.setOnRefreshListener {
            loadData()
            loadStatisticsData()
        }
        header_toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        header_toolbar.addRightIcon(0).apply {
            setImageDrawable(getIconUnifyDrawable(context, IconUnify.EDIT))
            setOnClickListener {
                val intent = RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_HEADLINE_ADS_EDIT)?.apply {
                    putExtra(TopAdsDashboardConstant.TAB_POSITION, 0)
                    putExtra(ParamObject.GROUP_ID, groupId.toString())
                }
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(click_edit_icon, "{${userSession.shopId}} - {$groupId}", userSession.userId)
                startActivityForResult(intent, EDIT_HEADLINE_REQUEST_CODE)
            }
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
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(view_detail_iklan, "{${userSession.shopId}} - {$groupId}", userSession.userId)
    }

    override fun onBackPressed() {
        if (isDataChanged) {
            val intent = Intent()
            intent.putExtra(IS_CHANGED, isDataChanged)
            setResult(Activity.RESULT_OK, intent)
        }
        super.onBackPressed()
    }

    private fun loadData() {
        viewModel.getGroupInfo(resources, groupId.toString(), ::onSuccessGroupInfo)
    }

    private fun onSuccessGroupInfo(data: GroupInfoResponse.TopAdsGetPromoGroup.Data) {
        groupStatus = data.status
        groupName = data.groupName
        groupTotal = data.groupTotal.toInt()
        priceDaily = data.priceDaily
        group_name.text = groupName
        btn_switch.setOnCheckedChangeListener(null)
        btn_switch.isChecked = data.status == ACTIVE || data.status == TIDAK_TAMPIL
        btn_switch.setOnCheckedChangeListener(this)
        if (priceDaily == 0) {
            progress_status1.text = TopAdsDashboardConstant.TIDAK_DIBATASI
            progress_status2.visibility = View.GONE
            progress_bar.visibility = View.GONE
        } else {
            progress_status2.visibility = View.VISIBLE
            progress_status2.text = String.format(resources.getString(com.tokopedia.topads.common.R.string.topads_dash_group_item_progress_status), priceDaily)
            progress_status1.text = priceSpent
            progress_bar.visibility = View.VISIBLE
            try {
                priceSpent = null
                Utils.convertMoneyToValue(priceSpent ?: "0").let {
                    progress_bar.setValue(it, false)
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }
    }

    private fun onStateChanged(state: TopAdsProductIklanFragment.State?) {
        swipe_refresh_layout.isEnabled = state == TopAdsProductIklanFragment.State.EXPANDED
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == EDIT_GROUP_REQUEST_CODE) {
                loadData()
            } else if (requestCode == EDIT_HEADLINE_REQUEST_CODE) {
                isDataChanged = true
                loadData()
                loadStatisticsData()
                renderTabAndViewPager()
            }
        }
    }

    private fun getBundleArguments() {
        groupId = intent?.extras?.getInt(GROUP_ID)
        priceSpent = intent?.extras?.getString(TopAdsDashboardConstant.PRICE_SPEND)
    }

    private fun loadStatisticsData() {
        if (startDate == null || endDate == null) return
        viewModel.getTopAdsStatistic(startDate!!, endDate!!, TopAdsStatisticsType.HEADLINE_ADS, ::onSuccesGetStatisticsInfo, groupId.toString())
    }

    private fun onSuccesGetStatisticsInfo(dataStatistic: DataStatistic) {
        swipe_refresh_layout.isRefreshing = false
        this.dataStatistic = dataStatistic
        if (this.dataStatistic != null) {
            topAdsTabAdapter?.setSummary(dataStatistic.summary, resources.getStringArray(R.array.top_ads_tab_statistics_labels))
        }
        val fragment = pager.adapter?.instantiateItem(pager, pager.currentItem) as? Fragment
        if (fragment != null && fragment is TopAdsDashStatisticFragment) {
            fragment.showLineGraph(this.dataStatistic)
        }
    }

    override fun getComponent(): TopAdsDashboardComponent = DaggerTopAdsDashboardComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent).build()

    private fun initInjector() {
        DaggerTopAdsDashboardComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent).build().inject(this)
    }

    fun setKeywordCount(size: Int) {
        tab_layout?.getUnifyTabLayout()?.getTabAt(0)?.setCounter(size)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        setResult(Activity.RESULT_OK)
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(click_toggle_icon, "{${userSession.shopId}} - {$groupId}", userSession.userId)
        when {
            isChecked -> viewModel.setGroupAction(ACTION_ACTIVATE, listOf(groupId.toString()), resources)
            else -> viewModel.setGroupAction(ACTION_DEACTIVATE, listOf(groupId.toString()), resources)
        }
    }
}



