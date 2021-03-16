package com.tokopedia.topads.dashboard.view.activity

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.SEVEN_DAYS_RANGE_INDEX
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.view.adapter.TopAdsStatisticPagerAdapter
import com.tokopedia.topads.dashboard.view.adapter.TopAdsTabAdapter
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDashStatisticFragment
import com.tokopedia.topads.dashboard.view.fragment.TopAdsProductIklanFragment
import com.tokopedia.topads.dashboard.view.fragment.stats.*
import com.tokopedia.topads.dashboard.view.interfaces.FetchDate
import com.tokopedia.topads.dashboard.view.sheet.CustomDatePicker
import com.tokopedia.topads.dashboard.view.sheet.DatePickerSheet
import kotlinx.android.synthetic.main.topads_dash_layout_hari_ini.view.*
import java.util.*

abstract class TopAdsBaseDetailActivity : BaseActivity(), CustomDatePicker.ActionListener, FetchDate {

    internal var startDate: Date? = null
    internal var endDate: Date? = null
    lateinit var currentDate: TextView
    private var datePickerSheet: DatePickerSheet? = null
    lateinit var selectDate: ConstraintLayout
    lateinit var rvTab: RecyclerView
    lateinit var statsGraphPager: ViewPager
    val topAdsTabAdapter: TopAdsTabAdapter? by lazy {
        this.run { TopAdsTabAdapter(this) }
    }

    private fun initTabLayouTitles() {
        topAdsTabAdapter?.setSummary(null, resources.getStringArray(R.array.top_ads_tab_statistics_labels))
    }


    private val pagerAdapter: TopAdsStatisticPagerAdapter? by lazy {
        val fragmentList = listOf(
                TopAdsStatsImprFragment.createInstance(),
                TopAdsStatsKlikFragment.createInstance(),
                TopAdsStatsSpentFragment.createInstance(),
                TopAdsStatsIncomeFragment.createInstance(),
                TopAdsStatsCtrFragment.createInstance(),
                TopAdsStatsConversionFragment.createInstance(),
                TopAdsStatsAvgFragment.createInstance(),
                TopAdsStatsSoldFragment.createInstance())
        this.run { TopAdsStatisticPagerAdapter(this, supportFragmentManager, fragmentList) }
    }

    val currentStatisticsFragment: TopAdsDashStatisticFragment?
        get() = pagerAdapter?.instantiateItem(statsGraphPager, topAdsTabAdapter?.selectedTabPosition
                ?: 0) as? TopAdsDashStatisticFragment


    abstract fun getLayoutId(): Int

    abstract fun loadChildStatisticsData()

    abstract fun renderGraph()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startDate = Utils.getStartDate()
        endDate = Utils.getEndDate()
        setContentView(getLayoutId())
        currentDate = findViewById(R.id.current_date)
        selectDate = findViewById(R.id.hari_ini)
        rvTab = findViewById(R.id.recyclerview_tabLayout)
        statsGraphPager = findViewById(R.id.pager)
        initStatisticComponent()
        selectDate.date_image?.setImageDrawable(getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_calendar))
        selectDate.next_image?.setImageDrawable(getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_arrow))
        selectDate.setOnClickListener {
            showBottomSheet()
        }
        setDateRangeText(SEVEN_DAYS_RANGE_INDEX)
    }

    private fun showBottomSheet() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val index = sharedPref?.getInt(TopAdsDashboardConstant.DATE_RANGE_DETAIL, 2)
        val customStartDate = sharedPref?.getString(TopAdsDashboardConstant.START_DATE_DETAIL, "")
        val customEndDate = sharedPref?.getString(TopAdsDashboardConstant.END_DATE_DETAIL, "")
        val dateRange: String
        dateRange = if (customStartDate?.isNotEmpty()!!) {
            "$customStartDate - $customEndDate"
        } else
            getString(R.string.topads_dash_custom_date_desc)

        datePickerSheet = DatePickerSheet.newInstance(this, index ?: 2, dateRange)
        datePickerSheet?.show()

        datePickerSheet?.onItemClick = { date1, date2, position ->
            handleDate(date1, date2, position)
        }

        datePickerSheet?.customDatepicker = {
            startCustomDatePicker()
        }
    }

    private fun handleDate(date1: Long, date2: Long, position: Int) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt(TopAdsDashboardConstant.DATE_RANGE_DETAIL, position)
            commit()
        }
        startDate = Date(date1)
        endDate = Date(date2)
        setDateRangeText(position)
        loadChildStatisticsData()
    }

    private fun initStatisticComponent() {
        val tabLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvTab.layoutManager = tabLayoutManager
        topAdsTabAdapter?.setListener(object : TopAdsTabAdapter.OnRecyclerTabItemClick {
            override fun onTabItemClick(position: Int) {
                statsGraphPager.currentItem = position
            }
        })
        rvTab.adapter = topAdsTabAdapter
        val smoothScroller = object : LinearSmoothScroller(this) {
            override fun getHorizontalSnapPreference(): Int {
                return SNAP_TO_START
            }

            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return TopAdsProductIklanFragment.MILLISECONDS_PER_INCH / displayMetrics.densityDpi
            }
        }

        statsGraphPager.offscreenPageLimit = TopAdsDashboardConstant.OFFSCREEN_PAGE_LIMIT
        initTabLayouTitles()
        statsGraphPager.adapter = pagerAdapter
        statsGraphPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                smoothScroller.targetPosition = position
                tabLayoutManager.startSmoothScroll(smoothScroller)
                topAdsTabAdapter?.selected(position)
                renderGraph()
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }


    private fun setDateRangeText(position: Int) {
        when (position) {
            1 -> currentDate.text = getString(com.tokopedia.datepicker.range.R.string.yesterday)
            0 -> currentDate.text = getString(R.string.topads_dash_hari_ini)
            2 -> currentDate.text = getString(com.tokopedia.datepicker.range.R.string.seven_days_ago)
            else -> {
                val text = Utils.outputFormat.format(startDate
                        ?: Date()) + " - " + Utils.outputFormat.format(endDate ?: Date())
                currentDate.text = text
            }
        }
    }

    private fun startCustomDatePicker() {
        val sheet = CustomDatePicker.getInstance()
        sheet.setListener(this)
        sheet.show(supportFragmentManager, TopAdsDashboardConstant.DATE_PICKER_SHEET)
    }


    override fun onCustomDateSelected(dateStart: Date, dateEnd: Date) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return

        with(sharedPref.edit()) {
            putInt(TopAdsDashboardConstant.DATE_RANGE_DETAIL, TopAdsDashboardConstant.CUSTOM_DATE)
            commit()
        }

        startDate = dateStart
        with(sharedPref.edit()) {
            putString(TopAdsDashboardConstant.START_DATE_DETAIL, Utils.outputFormat.format(startDate))
            commit()
        }
        endDate = dateEnd
        with(sharedPref.edit()) {
            putString(TopAdsDashboardConstant.END_DATE_DETAIL, Utils.outputFormat.format(endDate))
            commit()
        }
        setDateRangeText(TopAdsDashboardConstant.CUSTOM_DATE)
        loadChildStatisticsData()
    }

    override fun getStartDate(): String {
        return Utils.format.format(startDate ?: Date())
    }

    override fun getEndDate(): String {
        return Utils.format.format(endDate ?: Date())
    }


}