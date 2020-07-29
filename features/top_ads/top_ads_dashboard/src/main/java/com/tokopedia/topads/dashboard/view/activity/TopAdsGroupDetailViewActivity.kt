package com.tokopedia.topads.dashboard.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.topads.common.data.response.GroupInfoResponse
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION_ACTIVATE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION_DEACTIVATE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CUSTOM_DATE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.DATE_RANGE_DETAIL
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.EDIT_GROUP_REQUEST_CODE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.END_DATE_DETAIL
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.GROUP_ID
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.GROUP_NAME
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.GROUP_TOTAL
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.START_DATE_DETAIL
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.data.utils.Utils.format
import com.tokopedia.topads.dashboard.data.utils.Utils.outputFormat
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.adapter.TopAdsDashGroupDetailPagerAdapter
import com.tokopedia.topads.dashboard.view.adapter.TopAdsStatisticPagerAdapter
import com.tokopedia.topads.dashboard.view.adapter.TopAdsTabAdapter
import com.tokopedia.topads.dashboard.view.fragment.*
import com.tokopedia.topads.dashboard.view.model.GroupDetailViewModel
import com.tokopedia.topads.dashboard.view.sheet.CustomDatePicker
import com.tokopedia.topads.dashboard.view.sheet.DatePickerSheet
import kotlinx.android.synthetic.main.partial_top_ads_dashboard_statistics.*
import kotlinx.android.synthetic.main.topads_dash_detail_view_widget.*
import kotlinx.android.synthetic.main.topads_dash_fragment_group_detail_view_layout.*
import kotlinx.android.synthetic.main.topads_dash_layout_hari_ini.*
import java.lang.NumberFormatException
import java.util.*
import javax.inject.Inject
import kotlin.math.abs

/**
 * Created by Pika on 1/6/20.
 */

class TopAdsGroupDetailViewActivity : BaseActivity(), HasComponent<TopAdsDashboardComponent>, CustomDatePicker.ActionListener, CompoundButton.OnCheckedChangeListener, ProductTabFragment.FetchDate {

    internal var dataStatistic: DataStatistic? = null
    private var selectedStatisticType: Int = 0
    private var groupId: Int? = 0
    private var priceSpent: String? = ""
    private var groupStatus: String? = ""
    private var groupName: String? = ""
    private var datePickerSheet: DatePickerSheet? = null
    internal var startDate: Date? = null
    internal var endDate: Date? = null
    private var priceDaily = 0
    private var groupTotal = 0
    private val ACTIVE = "1"
    private val TIDAK_TAMPIL = "2"
    private val SEVEN_DAYS_RANGE_INDEX = 2
    private var mCurrentState = TopAdsProductIklanFragment.State.IDLE

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy {
        ViewModelProviders.of(this, viewModelFactory)
    }
    private val viewModel by lazy {
        viewModelProvider.get(GroupDetailViewModel::class.java)
    }

    private lateinit var detailPagerAdapter: TopAdsDashGroupDetailPagerAdapter
    private val topAdsTabAdapter: TopAdsTabAdapter? by lazy {
        this.run { TopAdsTabAdapter(this) }
    }

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
        this.run { TopAdsStatisticPagerAdapter(this, supportFragmentManager, fragmentList) }
    }

    private val currentStatisticsFragment: TopAdsDashboardStatisticFragment?
        get() = pagerAdapter?.instantiateItem(pager, topAdsTabAdapter?.selectedTabPosition
                ?: 0) as? TopAdsDashboardStatisticFragment


    private fun initTabLayouTitles() {
        topAdsTabAdapter?.setSummary(null, resources.getStringArray(R.array.top_ads_tab_statistics_labels))
    }

    private fun renderTabAndViewPager() {
        view_pager_frag.adapter = getViewPagerAdapter()
        view_pager_frag.offscreenPageLimit = 3
        view_pager_frag.currentItem = 0
        view_pager_frag.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))
        tab_layout.setupWithViewPager(view_pager_frag)
    }

    private fun getViewPagerAdapter(): PagerAdapter {
        val list: MutableList<Fragment> = mutableListOf()
        val bundle = Bundle()
        bundle.putInt(GROUP_ID, groupId ?: 0)
        bundle.putString(GROUP_NAME, groupName)
        bundle.putInt(GROUP_TOTAL, groupTotal)
        list.add(ProductTabFragment.createInstance(bundle))
        list.add(KeywordTabFragment.createInstance(bundle))
        list.add(NegKeywordTabFragment.createInstance(bundle))
        detailPagerAdapter = TopAdsDashGroupDetailPagerAdapter(supportFragmentManager, 0)
        detailPagerAdapter.setList(list)
        return detailPagerAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        selectedStatisticType = TopAdsStatisticsType.PRODUCT_ADS
        setContentView(R.layout.topads_dash_fragment_group_detail_view_layout)
        getBundleArguments()
        setDateRangeText(SEVEN_DAYS_RANGE_INDEX)
        initStatisticComponent()
        startDate = Utils.getStartDate()
        endDate = Utils.getEndDate()
        loadData()
        swipe_refresh_layout.setOnRefreshListener {
            loadData()
        }
        header_toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }
        hari_ini?.setOnClickListener {
            showBottomSheet()
        }

        header_toolbar.addRightIcon(com.tokopedia.topads.common.R.drawable.topads_edit_pen_icon).setOnClickListener {

            val intent = RouteManager.getIntent(this, ApplinkConstInternalTopAds.TOPADS_EDIT_ADS)?.apply {
                putExtra(TopAdsDashboardConstant.TAB_POSITION, 2)
                putExtra(TopAdsDashboardConstant.GROUPID, groupId.toString())
                putExtra(TopAdsDashboardConstant.GROUPNAME, groupName)
            }
            startActivityForResult(intent, EDIT_GROUP_REQUEST_CODE)
        }
        app_bar_layout_2.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
            when {
                offset == 0 -> {
                    if (mCurrentState != TopAdsProductIklanFragment.State.EXPANDED) {
                        onStateChanged(app_bar_layout_2, TopAdsProductIklanFragment.State.EXPANDED);
                    }
                    mCurrentState = TopAdsProductIklanFragment.State.EXPANDED;
                }
                abs(offset) >= app_bar_layout_2.totalScrollRange -> {
                    if (mCurrentState != TopAdsProductIklanFragment.State.COLLAPSED) {
                        onStateChanged(app_bar_layout_2, TopAdsProductIklanFragment.State.COLLAPSED);
                    }
                    mCurrentState = TopAdsProductIklanFragment.State.COLLAPSED;
                }
                else -> {
                    if (mCurrentState != TopAdsProductIklanFragment.State.IDLE) {
                        onStateChanged(app_bar_layout_2, TopAdsProductIklanFragment.State.IDLE);
                    }
                    mCurrentState = TopAdsProductIklanFragment.State.IDLE;
                }
            }
        })
    }

    private fun loadData() {
        viewModel.getGroupInfo(resources, groupId.toString(), ::onSuccessGroupInfo)
        viewModel.getTopAdsStatistic(startDate!!, endDate!!, selectedStatisticType, ::onSuccesGetStatisticsInfo, groupId.toString())
    }

    private fun onSuccessGroupInfo(data: GroupInfoResponse.TopAdsGetPromoGroup.Data) {
        groupStatus = data.status
        groupName = data.groupName
        groupTotal = data.groupTotal.toInt()
        priceDaily = data.priceDaily
        budgetPerClick.text = "Rp " + data.priceBid
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
        renderTabAndViewPager()
    }

    private fun onStateChanged(appBarLayout: AppBarLayout?, state: TopAdsProductIklanFragment.State?) {
        swipe_refresh_layout.isEnabled = state == TopAdsProductIklanFragment.State.EXPANDED
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_GROUP_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK)
                loadData()
        }
    }

    private fun getBundleArguments() {
        groupId = intent?.extras?.getInt(GROUP_ID)
        priceSpent = intent?.extras?.getString(TopAdsDashboardConstant.PRICE_SPEND)
    }

    private fun initStatisticComponent() {
        val tabLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerview_tabLayout.layoutManager = tabLayoutManager
        topAdsTabAdapter?.setListener(object : TopAdsTabAdapter.OnRecyclerTabItemClick {
            override fun onTabItemClick(position: Int) {
                pager.currentItem = position
            }
        })
        recyclerview_tabLayout.adapter = topAdsTabAdapter
        val smoothScroller = object : LinearSmoothScroller(this) {
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

    private fun showBottomSheet() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val index = sharedPref?.getInt(DATE_RANGE_DETAIL, 2)
        val customStartDate = sharedPref?.getString(START_DATE_DETAIL, "")
        val customEndDate = sharedPref?.getString(END_DATE_DETAIL, "")
        val dateRange: String
        dateRange = if (customStartDate?.isNotEmpty()!!) {
            "$customStartDate - $customEndDate"
        } else
            getString(R.string.topads_dash_custom_date_desc) ?: ""

        datePickerSheet = DatePickerSheet.newInstance(this, index ?: 2, dateRange)
        datePickerSheet?.show()

        datePickerSheet?.onItemClick = { date1, date2, position ->
            handleDate(date1, date2, position)
        }

        datePickerSheet?.customDatepicker = {
            startCustomDatePicker()
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
        sheet.show(supportFragmentManager, "datepicker")
    }


    private fun handleDate(date1: Long, date2: Long, position: Int) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt(DATE_RANGE_DETAIL, position)
            commit()
        }
        startDate = Date(date1)
        endDate = Date(date2)
        setDateRangeText(position)

        loadStatisticsData()
    }

    private fun setDateRangeText(position: Int) {
        when (position) {
            1 -> current_date.text = getString(com.tokopedia.datepicker.range.R.string.yesterday)
            0 -> current_date.text = getString(R.string.topads_dash_hari_ini)
            2 -> current_date.text = getString(com.tokopedia.datepicker.range.R.string.seven_days_ago)
            else -> {
                val text = outputFormat.format(startDate) + " - " + outputFormat.format(endDate)
                current_date.text = text
            }
        }
    }

    private fun loadStatisticsData() {
        if (startDate == null || endDate == null) return
        viewModel.getTopAdsStatistic(startDate!!, endDate!!, selectedStatisticType, ::onSuccesGetStatisticsInfo, groupId.toString())
    }

    fun onSuccesGetStatisticsInfo(dataStatistic: DataStatistic) {
        swipe_refresh_layout.isRefreshing = false
        this.dataStatistic = dataStatistic
        if (this.dataStatistic != null) {
            topAdsTabAdapter?.setSummary(dataStatistic.summary, resources.getStringArray(R.array.top_ads_tab_statistics_labels))
        }
        val fragment = pager.adapter?.instantiateItem(pager, pager.currentItem) as? Fragment
        if (fragment != null && fragment is TopAdsDashboardStatisticFragment) {
            fragment.updateDataStatistic(this.dataStatistic)
        }
    }

    override fun getComponent(): TopAdsDashboardComponent = DaggerTopAdsDashboardComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent).build()

    override fun onCustomDateSelected(dateStart: Date, dateEnd: Date) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return

        with(sharedPref.edit()) {
            putInt(DATE_RANGE_DETAIL, CUSTOM_DATE)
            commit()
        }

        startDate = dateStart
        with(sharedPref.edit()) {
            putString(START_DATE_DETAIL, outputFormat.format(startDate))
            commit()
        }
        endDate = dateEnd
        with(sharedPref.edit()) {
            putString(END_DATE_DETAIL, outputFormat.format(endDate))
            commit()
        }
        setDateRangeText(CUSTOM_DATE)
        loadStatisticsData()
    }

    private fun initInjector() {
        DaggerTopAdsDashboardComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent).build().inject(this)
    }

    fun setProductCount(size: Int) {
        detailPagerAdapter.setTitleProduct(String.format(getString(R.string.topads_dash_product_count), size))
    }

    fun setKeywordCount(size: Int) {
        detailPagerAdapter.setTitleKeyword(String.format(getString(R.string.topads_dash_keyword_count), size))
    }

    fun setNegKeywordCount(size: Int) {
        detailPagerAdapter.setTitleNegKeyword(String.format(getString(R.string.topads_dash_neg_key_count), size))
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        setResult(Activity.RESULT_OK)
        when {
            isChecked -> viewModel.setGroupAction(ACTION_ACTIVATE, listOf(groupId.toString()), resources)
            else -> viewModel.setGroupAction(ACTION_DEACTIVATE, listOf(groupId.toString()), resources)
        }
    }

    override fun getStartDate(): String {
        return format.format(startDate)
    }

    override fun getEndDate(): String {
        return format.format(endDate)
    }

    override fun onDestroy() {
        super.onDestroy()
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            clear()
            commit()
        }
    }
}



