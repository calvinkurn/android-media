package com.tokopedia.topads.dashboard.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.data.model.DataDeposit
import com.tokopedia.topads.credit.history.view.activity.TopAdsCreditHistoryActivity
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CUSTOM_DATE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.DATA_INSIGHT
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.DATE_RANGE_BERANDA
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.END_DATE_BERANDA
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.START_DATE_BERANDA
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.data.model.insightkey.InsightKeyData
import com.tokopedia.topads.dashboard.data.model.insightkey.KeywordInsightDataMain
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.data.utils.Utils.format
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.activity.TopAdsAddCreditActivity
import com.tokopedia.topads.dashboard.view.adapter.TopAdsDashInsightPagerAdapter
import com.tokopedia.topads.dashboard.view.adapter.TopAdsStatisticPagerAdapter
import com.tokopedia.topads.dashboard.view.adapter.TopAdsTabAdapter
import com.tokopedia.topads.dashboard.view.adapter.insight.TopAdsInsightTabAdapter
import com.tokopedia.topads.dashboard.view.fragment.insight.TopAdsInsightMiniBidFragment
import com.tokopedia.topads.dashboard.view.fragment.insight.TopAdsInsightMiniKeyFragment
import com.tokopedia.topads.dashboard.view.fragment.insight.TopAdsInsightMiniProductFragment
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import com.tokopedia.topads.dashboard.view.sheet.CustomDatePicker
import com.tokopedia.topads.dashboard.view.sheet.DatePickerSheet
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpStatus
import com.tokopedia.unifycomponents.setImage
import kotlinx.android.synthetic.main.partial_top_ads_dashboard_statistics.*
import kotlinx.android.synthetic.main.topads_dash_fragment_beranda_base.*
import kotlinx.android.synthetic.main.topads_dash_fragment_beranda_base.hari_ini
import kotlinx.android.synthetic.main.topads_dash_fragment_beranda_base.swipe_refresh_layout
import kotlinx.android.synthetic.main.topads_dash_fragment_group_detail_view_layout.*
import kotlinx.android.synthetic.main.topads_dash_layout_hari_ini.*
import kotlinx.android.synthetic.main.topads_dash_layout_hari_ini.view.*
import java.util.*
import javax.inject.Inject

/**
 * Created by Pika on 15/5/20.
 */
open class BerandaTabFragment : BaseDaggerFragment(), CustomDatePicker.ActionListener {

    private var dataStatistic: DataStatistic? = null
    private var insightCallBack: GoToInsight? = null

    @TopAdsStatisticsType
    internal var selectedStatisticType: Int = TopAdsStatisticsType.PRODUCT_ADS
    private var datePickerSheet: DatePickerSheet? = null
    internal var startDate: Date? = null
    internal var endDate: Date? = null

    companion object {

        private const val REQUEST_CODE_ADD_CREDIT = 1
        private const val REQUEST_CODE_SET_AUTO_TOPUP = 6
        private const val SEVEN_DAYS_RANGE_INDEX = 2

        fun createInstance(): BerandaTabFragment {
            return BerandaTabFragment()
        }

        private const val SELLER_CENTER_URL = "https://seller.tokopedia.com/edu/about-topads/iklan/?source=help&medium=android"
    }

    @Inject
    lateinit var topAdsDashboardPresenter: TopAdsDashboardPresenter


    private val pagerAdapter: TopAdsStatisticPagerAdapter? by lazy {
        val fragmentList = listOf(
                TopAdsStatisticImprFragment.createInstance(),
                TopAdsStatisticKlikFragment.createInstance(),
                TopAdsStatisticSpentFragment.createInstance(),
                TopAdsStatisticIncomeFragment.createInstance(),
                TopAdsStatisticCtrFragment.createInstance(),
                TopAdsStatisticConversionFragment.createInstance(),
                TopAdsStatisticAvgFragment.createInstance(),
                TopAdsStatisticSoldFragment.createInstance())
        context?.run { TopAdsStatisticPagerAdapter(this, childFragmentManager, fragmentList) }

    }

    private val topAdsTabAdapter: TopAdsTabAdapter? by lazy {
        context?.run { TopAdsTabAdapter(this) }
    }

    private val topAdsInsightTabAdapter: TopAdsInsightTabAdapter? by lazy {
        context?.run { TopAdsInsightTabAdapter(this) }
    }

    protected val currentStatisticsFragment: TopAdsDashboardStatisticFragment?
        get() = pagerAdapter?.instantiateItem(pager, topAdsTabAdapter?.selectedTabPosition
                ?: 0) as? TopAdsDashboardStatisticFragment

    override fun getScreenName(): String {
        return BerandaTabFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(resources.getLayout(R.layout.topads_dash_fragment_beranda_base), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedStatisticType = TopAdsStatisticsType.PRODUCT_ADS
        initStatisticComponent()
        image.setImageDrawable(context?.getResDrawable(R.drawable.topads_ic_wallet))
        arrow.setImageDrawable(context?.getResDrawable(R.drawable.topads_ic_arrow))
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

        setDateRangeText(SEVEN_DAYS_RANGE_INDEX)
        endDate = Utils.getEndDate()
        startDate = Utils.getStartDate()
        loadData()
        hari_ini?.date_image?.setImageDrawable(context?.getResDrawable(R.drawable.topads_ic_calendar))
        hari_ini?.next_image?.setImageDrawable(context?.getResDrawable(R.drawable.topads_ic_arrow))
        hari_ini?.setOnClickListener {
            showBottomSheet()
        }
        swipe_refresh_layout.setOnRefreshListener {
            loadData()
        }
    }

    private fun renderInsightViewPager(data: HashMap<String, KeywordInsightDataMain>) {
        viewPagerInsight?.adapter = getViewPagerAdapter(data)
        viewPagerInsight?.disableScroll(true)
        viewPagerInsight?.currentItem = 0
        viewPagerInsight?.offscreenPageLimit = TopAdsDashboardConstant.OFFSCREEN_PAGE_LIMIT
    }

    private fun getViewPagerAdapter(data: HashMap<String, KeywordInsightDataMain>): TopAdsDashInsightPagerAdapter? {
        val list: ArrayList<Fragment> = arrayListOf()
        val bundle = Bundle()
        bundle.putSerializable(DATA_INSIGHT, data)
        list.add(TopAdsInsightMiniKeyFragment.createInstance(bundle))
        val adapter = TopAdsDashInsightPagerAdapter(childFragmentManager, 0)
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

    private fun showBottomSheet() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val index = sharedPref?.getInt(DATE_RANGE_BERANDA, 2)
        val customStartDate = sharedPref?.getString(START_DATE_BERANDA, "")
        val customEndDate = sharedPref?.getString(END_DATE_BERANDA, "")
        val dateRange: String
        dateRange = if (customStartDate?.isNotEmpty()!!) {
            "$customStartDate - $customEndDate"
        } else
            context?.getString(R.string.topads_dash_custom_date_desc) ?: ""
        datePickerSheet = DatePickerSheet.newInstance(context!!, index ?: 2, dateRange)
        datePickerSheet?.show()
        datePickerSheet?.onItemClick = { date1, date2, position ->
            handleDate(date1, date2, position)
        }
        datePickerSheet?.customDatepicker = {
            startCustomDatePicker()
        }
    }

    private fun loadData() {
        swipe_refresh_layout.isEnabled = true
        loadStatisticsData()
        getAutoTopUpStatus()
        topAdsDashboardPresenter.getShopDeposit(::onLoadTopAdsShopDepositSuccess)
        topAdsDashboardPresenter.getInsight(resources, ::onSuccessGetInsightData)

    }

    private fun handleDate(date1: Long, date2: Long, position: Int) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt(DATE_RANGE_BERANDA, position)
            commit()
        }
        startDate = Date(date1)
        endDate = Date(date2)
        setDateRangeText(position)
        loadStatisticsData()
    }

    private fun setDateRangeText(position: Int) {
        when (position) {
            1 -> current_date.text = context?.getString(com.tokopedia.datepicker.range.R.string.yesterday)
            0 -> current_date.text = context?.getString(R.string.topads_dash_hari_ini)
            2 -> current_date.text = context?.getString(com.tokopedia.datepicker.range.R.string.seven_days_ago)
            else -> {
                val text = Utils.outputFormat.format(startDate) + " - " + Utils.outputFormat.format(endDate)
                current_date.text = text
            }
        }
    }

    private fun startCustomDatePicker() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        val selectDate: String = format.format(calendar.time)
        calendar.add(Calendar.YEAR, -1)
        val date = calendar.time
        val minDate = format.format(date)
        val maxDate: String = format.format(Date())
        val sheet = CustomDatePicker.getInstance(minDate, maxDate, selectDate)
        sheet.setTitle(resources.getString(R.string.topads_dash_choose_date))
        sheet.setListener(this)
        sheet.show(childFragmentManager, "datepicker")
    }

    private fun onLoadTopAdsShopDepositSuccess(dataDeposit: DataDeposit) {
        swipe_refresh_layout.isRefreshing = false
        credits.text = dataDeposit.amountFmt
    }

    private fun onSuccesGetStatisticsInfo(dataStatistic: DataStatistic) {

        this.dataStatistic = dataStatistic
        if (this.dataStatistic != null) {
            topAdsTabAdapter?.setSummary(dataStatistic.summary, resources.getStringArray(R.array.top_ads_tab_statistics_labels))
        }
        val fragment = pager.adapter?.instantiateItem(pager, pager.currentItem) as? Fragment
        if (fragment != null && fragment is TopAdsDashboardStatisticFragment) {
            fragment.updateDataStatistic(this.dataStatistic)
        }
    }

    private fun onSuccessGetAutoTopUpStatus(data: AutoTopUpStatus) {
        val isAutoTopUpActive = (data.status.toIntOrZero()) != TopAdsDashboardConstant.AUTO_TOPUP_INACTIVE
        if (isAutoTopUpActive) {
            img_auto_debit.setImageDrawable(context?.getResDrawable(R.drawable.topads_dash_auto_debit))
            img_auto_debit.visibility = View.VISIBLE
        }
    }

    private fun initInsightTabAdapter(response: InsightKeyData) {
        val tabLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rvTabInsight.layoutManager = tabLayoutManager
        topAdsInsightTabAdapter?.setListener(object : TopAdsInsightTabAdapter.OnRecyclerTabItemClick {
            override fun onTabItemClick(position: Int) {
                viewPagerInsight.currentItem = position
                if(position==1||position==2){
                    goToInsights.visibility = View.GONE
                    arrow.visibility = View.GONE
                }
                else{
                    goToInsights.visibility = View.VISIBLE
                    arrow.visibility = View.VISIBLE
                }
            }
        })
        topAdsInsightTabAdapter?.setTabTitles(resources,  response.data.size, 0, 0)
        rvTabInsight.adapter = topAdsInsightTabAdapter
    }

    private fun initStatisticComponent() {
        val tabLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerview_tabLayout.layoutManager = tabLayoutManager
        topAdsTabAdapter?.setListener(object : TopAdsTabAdapter.OnRecyclerTabItemClick {
            override fun onTabItemClick(position: Int) {
                pager.currentItem = position
            }
        })
        recyclerview_tabLayout.adapter = topAdsTabAdapter
        val smoothScroller = object : LinearSmoothScroller(activity!!) {
            override fun getHorizontalSnapPreference(): Int {
                return SNAP_TO_START
            }

            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return TopAdsProductIklanFragment.MILLISECONDS_PER_INCH / displayMetrics.densityDpi
            }
        }

        pager.offscreenPageLimit = TopAdsDashboardConstant.OFFSCREEN_PAGE_LIMIT
        initTabLayouTitles()
        pager.adapter = pagerAdapter
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                smoothScroller.targetPosition = position
                tabLayoutManager.startSmoothScroll(smoothScroller)
                topAdsTabAdapter?.selected(position)
                currentStatisticsFragment?.updateDataStatistic(dataStatistic)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun initTabLayouTitles() {
        topAdsTabAdapter?.setSummary(null, resources.getStringArray(R.array.top_ads_tab_statistics_labels))
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
        topAdsDashboardPresenter.getAutoTopUpStatus(GraphqlHelper
                .loadRawString(resources, R.raw.gql_query_get_status_auto_topup), this::onSuccessGetAutoTopUpStatus)
    }

    private fun goToCreditHistory(isFromSelection: Boolean = false) {
        context?.let {
            startActivityForResult(TopAdsCreditHistoryActivity.createInstance(it, isFromSelection), REQUEST_CODE_SET_AUTO_TOPUP)
        }
    }

    override fun onCustomDateSelected(dateSelected: Date, dateEnd: Date) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt(DATE_RANGE_BERANDA, CUSTOM_DATE)
            commit()
        }
        startDate = dateSelected
        with(sharedPref.edit()) {
            putString(START_DATE_BERANDA, Utils.outputFormat.format(startDate))
            commit()
        }
        endDate = dateEnd
        with(sharedPref.edit()) {
            putString(END_DATE_BERANDA, Utils.outputFormat.format(endDate))
            commit()
        }
        setDateRangeText(CUSTOM_DATE)
        loadStatisticsData()
    }

    private fun loadStatisticsData() {
        if (startDate == null || endDate == null) return
        topAdsDashboardPresenter.getTopAdsStatistic(startDate!!, endDate!!, selectedStatisticType, ::onSuccesGetStatisticsInfo)
    }

    override fun onDestroy() {
        super.onDestroy()
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            remove(END_DATE_BERANDA)
            remove(START_DATE_BERANDA)
            remove(DATE_RANGE_BERANDA)
            commit()
        }
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