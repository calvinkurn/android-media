package com.tokopedia.topads.dashboard.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_0
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_1
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_2
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CUSTOM_DATE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.DATE_PICKER_SHEET
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.adapter.TopAdsStatisticPagerAdapter
import com.tokopedia.topads.dashboard.view.adapter.TopAdsTabAdapter
import com.tokopedia.topads.dashboard.view.fragment.stats.*
import com.tokopedia.topads.dashboard.view.sheet.CustomDatePicker
import com.tokopedia.topads.dashboard.view.sheet.DatePickerSheet
import kotlinx.android.synthetic.main.topads_dash_layout_hari_ini.view.*
import java.util.*

/**
 * Created by Pika on 4/11/20.
 */

abstract class TopAdsBaseTabFragment : BaseDaggerFragment(), CustomDatePicker.ActionListener {

    internal var startDate: Date? = null
    internal var endDate: Date? = null
    lateinit var currentDate: TextView
    private var datePickerSheet: DatePickerSheet? = null
    lateinit var selectDate: ConstraintLayout
    lateinit var rvTab: RecyclerView
    lateinit var statsGraphPager: ViewPager

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
        context?.run { TopAdsStatisticPagerAdapter(this, childFragmentManager, fragmentList) }
    }

    val topAdsTabAdapter: TopAdsTabAdapter? by lazy {
        context?.run { TopAdsTabAdapter(this) }
    }

    protected val currentStatisticsFragment: TopAdsDashStatisticFragment?
        get() = pagerAdapter?.instantiateItem(statsGraphPager, topAdsTabAdapter?.selectedTabPosition
                ?: 0) as? TopAdsDashStatisticFragment


    abstract fun getLayoutId(): Int

    abstract fun setUpView(view: View)

    abstract fun getChildScreenName(): String

    abstract fun loadChildStatisticsData()

    abstract fun renderGraph()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getLayoutId(), container, false)
        currentDate = view.findViewById(R.id.current_date)
        selectDate = view.findViewById(R.id.hari_ini)
        rvTab = view.findViewById(R.id.recyclerview_tabLayout)
        statsGraphPager = view.findViewById(R.id.pager)
        setUpView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startDate = Utils.getStartDate()
        endDate = Utils.getEndDate()
        setDateRangeText(TopAdsDashboardConstant.SEVEN_DAYS_RANGE_INDEX)
        initStatisticComponent()
        selectDate.date_image?.setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_calendar))
        selectDate.next_image?.setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_arrow))
        selectDate.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun setDateRangeText(position: Int) {
        when (position) {
            CONST_1 -> currentDate.text = context?.getString(com.tokopedia.datepicker.range.R.string.yesterday)
            CONST_0 -> currentDate.text = context?.getString(R.string.topads_dash_hari_ini)
            CONST_2 -> currentDate.text = context?.getString(com.tokopedia.datepicker.range.R.string.seven_days_ago)
            else -> {
                val text = Utils.outputFormat.format(startDate
                        ?: Date()) + " - " + Utils.outputFormat.format(endDate ?: Date())
                currentDate.text = text
            }
        }
    }

    private fun getCurrentSelected(): Int {
        return when (currentDate.text) {
            context?.getString(com.tokopedia.datepicker.range.R.string.yesterday) -> CONST_1
            context?.getString(R.string.topads_dash_hari_ini) -> CONST_0
            context?.getString(com.tokopedia.datepicker.range.R.string.seven_days_ago) -> CONST_2
            "" -> CONST_0
            else -> {
                CUSTOM_DATE
            }
        }
    }

    override fun getScreenName(): String {
        return getChildScreenName()
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun onCustomDateSelected(dateSelected: Date, dateEnd: Date) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        startDate = dateSelected
        with(sharedPref.edit()) {
            putString(TopAdsDashboardConstant.START_DATE_BERANDA, Utils.outputFormat.format(startDate))
            commit()
        }
        endDate = dateEnd
        with(sharedPref.edit()) {
            putString(TopAdsDashboardConstant.END_DATE_BERANDA, Utils.outputFormat.format(endDate))
            commit()
        }
        setDateRangeText(TopAdsDashboardConstant.CUSTOM_DATE)
        loadChildStatisticsData()
    }


    private fun showBottomSheet() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val index = getCurrentSelected()
        val customStartDate = sharedPref?.getString(TopAdsDashboardConstant.START_DATE_BERANDA, "")
        val customEndDate = sharedPref?.getString(TopAdsDashboardConstant.END_DATE_BERANDA, "")
        val dateRange: String
        dateRange = if (customStartDate?.isNotEmpty() == true) {
            "$customStartDate - $customEndDate"
        } else
            context?.getString(R.string.topads_dash_custom_date_desc) ?: ""
        context?.let {
            datePickerSheet = DatePickerSheet.newInstance(it, index ?: CONST_2, dateRange)
            datePickerSheet?.show()
            datePickerSheet?.onItemClick = { date1, date2, position ->
                handleDate(date1, date2, position)
            }
            datePickerSheet?.customDatepicker = {
                startCustomDatePicker()
            }
        }
    }

    private fun handleDate(date1: Long, date2: Long, position: Int) {
        startDate = Date(date1)
        endDate = Date(date2)
        setDateRangeText(position)
        loadChildStatisticsData()
    }

    private fun initStatisticComponent() {
        val tabLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rvTab.layoutManager = tabLayoutManager
        topAdsTabAdapter?.setListener(object : TopAdsTabAdapter.OnRecyclerTabItemClick {
            override fun onTabItemClick(position: Int) {
                statsGraphPager.currentItem = position
            }
        })
        rvTab.adapter = topAdsTabAdapter
        val smoothScroller = object : LinearSmoothScroller(activity!!) {
            override fun getHorizontalSnapPreference(): Int {
                return SNAP_TO_START
            }

            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return TopAdsProductIklanFragment.MILLISECONDS_PER_INCH / displayMetrics.densityDpi
            }
        }

        statsGraphPager.offscreenPageLimit = TopAdsDashboardConstant.OFFSCREEN_PAGE_LIMIT
        statsGraphPager.adapter = pagerAdapter
        initTabLayouTitles()
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

    private fun initTabLayouTitles() {
        topAdsTabAdapter?.setSummary(null, resources.getStringArray(R.array.top_ads_tab_statistics_labels))
    }

    private fun startCustomDatePicker() {
        val sheet = CustomDatePicker.getInstance()
        sheet.setListener(this)
        sheet.show(childFragmentManager, DATE_PICKER_SHEET)
    }

    override fun onDestroy() {
        super.onDestroy()
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            remove(TopAdsDashboardConstant.END_DATE_BERANDA)
            remove(TopAdsDashboardConstant.START_DATE_BERANDA)
            commit()
        }
    }
}
