package com.tokopedia.topads.dashboard.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.data.response.DepositAmount
import com.tokopedia.topads.credit.history.view.activity.TopAdsCreditHistoryActivity
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.DATA_INSIGHT
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.REQUEST_CODE_ADD_CREDIT
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.data.model.FragmentTabItem
import com.tokopedia.topads.dashboard.data.model.insightkey.InsightKeyData
import com.tokopedia.topads.dashboard.data.model.insightkey.KeywordInsightDataMain
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity
import com.tokopedia.topads.dashboard.view.adapter.TopAdsDashboardBasePagerAdapter
import com.tokopedia.topads.dashboard.view.adapter.insight.TopAdsInsightTabAdapter
import com.tokopedia.topads.dashboard.view.fragment.TopAdsProductIklanFragment.Companion.MANUAL_AD
import com.tokopedia.topads.dashboard.view.fragment.insight.TopAdsInsightMiniKeyFragment
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpStatus
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsAddCreditActivity
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsEditAutoTopUpActivity
import kotlinx.android.synthetic.main.partial_top_ads_dashboard_statistics.*
import kotlinx.android.synthetic.main.topads_dash_fragment_beranda_base.*
import java.util.*
import javax.inject.Inject

/**
 * Created by Pika on 15/5/20.
 */
open class BerandaTabFragment : TopAdsBaseTabFragment() {

    private var dataStatistic: DataStatistic? = null
    private var insightCallBack: GoToInsight? = null

    @TopAdsStatisticsType
    internal var selectedStatisticType: Int = TopAdsStatisticsType.PRODUCT_ADS

    companion object {

        private const val REQUEST_CODE_SET_AUTO_TOPUP = 6

        fun createInstance(): BerandaTabFragment {
            return BerandaTabFragment()
        }

        private const val SELLER_CENTER_URL = "https://seller.tokopedia.com/edu/about-topads/iklan/?source=help&medium=android"
    }

    @Inject
    lateinit var topAdsDashboardPresenter: TopAdsDashboardPresenter
    private val topAdsInsightTabAdapter: TopAdsInsightTabAdapter? by lazy {
        context?.run { TopAdsInsightTabAdapter() }
    }

    override fun getLayoutId(): Int {
        return R.layout.topads_dash_fragment_beranda_base
    }

    override fun setUpView(view: View) {
    }

    override fun getChildScreenName(): String {
        return BerandaTabFragment::class.java.name
    }

    override fun loadChildStatisticsData() {
        loadStatisticsData()
    }

    override fun renderGraph() {
        currentStatisticsFragment?.showLineGraph(dataStatistic)
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedStatisticType = TopAdsStatisticsType.PRODUCT_ADS
        image.setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_wallet))
        arrow.setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_arrow))
        setUpClick()
        loadData()
        loadStatisticsData()
        swipe_refresh_layout.setOnRefreshListener {
            loadData()
            loadStatisticsData()
        }
    }

    private fun setUpClick() {
        help_section.setOnClickListener {
            RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, SELLER_CENTER_URL)
        }
        credit_history.setOnClickListener {
            goToCreditHistory(false)
        }
        addCredit.setOnClickListener {
            val intent = Intent(activity, TopAdsAddCreditActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_CREDIT)
        }
        goToInsights.setOnClickListener {
            insightCallBack?.gotToInsights()
        }
        autoTopUp?.setOnClickListener {
            startActivity(Intent(context, TopAdsEditAutoTopUpActivity::class.java))
        }
    }

    private fun renderInsightViewPager(data: HashMap<String, KeywordInsightDataMain>) {
        viewPagerInsight?.adapter = getViewPagerAdapter(data)
        viewPagerInsight?.disableScroll(true)
        viewPagerInsight?.currentItem = 0
        viewPagerInsight?.offscreenPageLimit = TopAdsDashboardConstant.OFFSCREEN_PAGE_LIMIT
    }

    private fun getViewPagerAdapter(data: HashMap<String, KeywordInsightDataMain>): TopAdsDashboardBasePagerAdapter? {
        val list: ArrayList<FragmentTabItem> = arrayListOf()
        val bundle = Bundle()
        bundle.putSerializable(DATA_INSIGHT, data)
        list.add(FragmentTabItem("", TopAdsInsightMiniKeyFragment.createInstance(bundle)))
        val adapter = TopAdsDashboardBasePagerAdapter(childFragmentManager, 0)
        adapter.setList(list)
        return adapter
    }

    private fun onSuccessGetInsightData(response: InsightKeyData) {
        if (response.data.isEmpty()) {
            insightCard.visibility = View.GONE
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
            with(sharedPref.edit()) {
                putBoolean(TopAdsDashboardConstant.FIRST_LAUNCH, false)
                commit()
            }
        } else {
            insightCard.visibility = View.VISIBLE
            initInsightTabAdapter(response)
            renderInsightViewPager(response.data)
        }
    }

    private fun loadData() {
        swipe_refresh_layout.isEnabled = true
        getAutoTopUpStatus()
        topAdsDashboardPresenter.getShopDeposit(::onLoadTopAdsShopDepositSuccess)
        topAdsDashboardPresenter.getInsight(resources, ::onSuccessGetInsightData)

    }

    private fun onLoadTopAdsShopDepositSuccess(dataDeposit: DepositAmount) {
        swipe_refresh_layout.isRefreshing = false
        credits.text = dataDeposit.amountFmt
    }

    private fun onSuccesGetStatisticsInfo(dataStatistic: DataStatistic) {

        this.dataStatistic = dataStatistic
        if (this.dataStatistic != null && dataStatistic.cells.isNotEmpty()) {
            topAdsTabAdapter?.setSummary(dataStatistic.summary, resources.getStringArray(R.array.top_ads_tab_statistics_labels))
        }
        val fragment = pager.adapter?.instantiateItem(pager, pager.currentItem) as? Fragment
        if (fragment != null && fragment is TopAdsDashStatisticFragment) {
            fragment.showLineGraph(this.dataStatistic)
        }
    }

    private fun onSuccessGetAutoTopUpStatus(data: AutoTopUpStatus) {
        val isAutoTopUpActive = (data.status.toIntOrZero()) != TopAdsDashboardConstant.AUTO_TOPUP_INACTIVE
        if (isAutoTopUpActive) {
            autoTopUp?.visibility = View.VISIBLE
            addCredit?.visibility = View.GONE
            img_auto_debit.setImageDrawable(context?.getResDrawable(R.drawable.topads_dash_auto_debit))
            img_auto_debit.visibility = View.VISIBLE
        } else {
            autoTopUp?.visibility = View.GONE
            addCredit?.visibility = View.VISIBLE
            img_auto_debit.visibility = View.GONE
        }
    }

    private fun initInsightTabAdapter(response: InsightKeyData) {
        val tabLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rvTabInsight.layoutManager = tabLayoutManager
        topAdsInsightTabAdapter?.setListener(object : TopAdsInsightTabAdapter.OnRecyclerTabItemClick {
            override fun onTabItemClick(position: Int) {
                viewPagerInsight.currentItem = position
                if (position == 1 || position == 2) {
                    goToInsights.visibility = View.GONE
                    arrow.visibility = View.GONE
                } else {
                    goToInsights.visibility = View.VISIBLE
                    arrow.visibility = View.VISIBLE
                }
            }
        })
        topAdsInsightTabAdapter?.setTabTitles(resources, 0, 0, response.data.size)
        rvTabInsight.adapter = topAdsInsightTabAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_CREDIT) {
            topAdsDashboardPresenter.getShopDeposit(::onLoadTopAdsShopDepositSuccess)
        } else if (requestCode == REQUEST_CODE_SET_AUTO_TOPUP && resultCode == Activity.RESULT_OK) {
            getAutoTopUpStatus()
            if (data?.getBooleanExtra("no_redirect", false) != true)
                goToCreditHistory(true)
        }
    }

    private fun getAutoTopUpStatus() {
        topAdsDashboardPresenter.getAutoTopUpStatus(resources, this::onSuccessGetAutoTopUpStatus)
    }

    private fun goToCreditHistory(isFromSelection: Boolean = false) {
        context?.let {
            startActivityForResult(TopAdsCreditHistoryActivity.createInstance(it, isFromSelection), REQUEST_CODE_SET_AUTO_TOPUP)
        }
    }

    fun loadStatisticsData() {
        if (startDate == null || endDate == null) return
        topAdsDashboardPresenter.getStatistic(startDate ?: Date(), endDate
                ?: Date(), selectedStatisticType, (activity as TopAdsDashboardActivity?)?.getAdInfo()
                ?: MANUAL_AD, ::onSuccesGetStatisticsInfo)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is GoToInsight) {
            insightCallBack = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        insightCallBack = null
    }

    interface GoToInsight {
        fun gotToInsights()
    }
}