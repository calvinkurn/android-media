package com.tokopedia.statistic.presentation.view.bottomsheet

import android.content.Context
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.statistic.R
import com.tokopedia.statistic.presentation.model.DateFilterItem
import com.tokopedia.statistic.presentation.view.adapter.DateFilterAdapter
import com.tokopedia.statistic.presentation.view.adapter.factory.DateFilterAdapterFactoryImpl
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottomsheet_stc_select_date_range.view.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created By @ilhamsuaib on 15/06/20
 */

class DateFilterBottomSheet(
        private val mContext: Context,
        private val fm: FragmentManager
) : BottomSheetUnify(), DateFilterAdapterFactoryImpl.Listener {

    companion object {
        private const val DAYS_7 = 7
        private const val DAYS_30 = 30
    }

    private var applyChangesCallback: (DateFilterItem) -> Unit = {}
    private val mAdapter by lazy { DateFilterAdapter(this, fm) }
    private val items: MutableList<DateFilterItem> by lazy {
        mutableListOf(
                getDateRangeItemToday(),
                getDateFilterItemClick(DAYS_7, DateFilterItem.TYPE_LAST_7_DAYS, true),
                getDateFilterItemClick(DAYS_30, DateFilterItem.TYPE_LAST_30_DAYS, showBottomBorder = false),
                DateFilterItem.Divider,
                getDateFilterPerDay(),
                getDateFilterPerWeek(),
                getFilterPerMonth(),
                DateFilterItem.ApplyButton
        )
    }

    private fun getFilterPerMonth(): DateFilterItem.MonthPickerItem {
        val perMonthLabel = context?.getString(R.string.stc_per_month) ?: "Per Bulan"
        return DateFilterItem.MonthPickerItem(perMonthLabel, Date())
    }

    init {
        val child: View = View.inflate(mContext, R.layout.bottomsheet_stc_select_date_range, null)
        setTitle(mContext.getString(R.string.stc_change_date_range))
        setChild(child)

        clearContentPadding = true
        setupView(child)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onItemDateRangeClick(model: DateFilterItem) {
        items.forEach {
            if (it != model) {
                it.isSelected = false
            }
        }
        mAdapter.notifyDataSetChanged()
    }

    override fun onApplyDateFilter() {
        val selectedItem = items.firstOrNull { it.isSelected } ?: return
        applyChangesCallback(selectedItem)
        dismissAllowingStateLoss()
    }

    fun setOnApplyChanges(callback: (DateFilterItem) -> Unit): DateFilterBottomSheet {
        this.applyChangesCallback = callback
        return this
    }

    fun show() {
        show(fm, this::class.java.simpleName)
    }

    private fun setupView(child: View) = with(child) {
        rvStcDateRage.run {
            layoutManager = LinearLayoutManager(mContext)
            adapter = mAdapter
        }

        mAdapter.clearAllElements()
        mAdapter.addElement(items)
    }

    private fun getDateFilterPerDay(): DateFilterItem.Pick {
        val label = mContext.getString(R.string.stc_per_day)
        val today = Date()
        return DateFilterItem.Pick(label, today, today, type = DateFilterItem.TYPE_PER_DAY)
    }

    private fun getDateFilterPerWeek(): DateFilterItem.Pick {
        val calendar: Calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        var firstDateOfWeek = calendar.time
        val m7Day = TimeUnit.DAYS.toMillis(7)
        val m6Day = TimeUnit.DAYS.toMillis(6)
        val now = Calendar.getInstance()
        if (now.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            firstDateOfWeek = Date(firstDateOfWeek.time - m7Day)
        }
        val lastDateOfWeek = Date(firstDateOfWeek.time + m6Day)
        val label = mContext.getString(R.string.stc_per_week)
        return DateFilterItem.Pick(label, firstDateOfWeek, lastDateOfWeek, type = DateFilterItem.TYPE_PER_WEEK)
    }

    private fun getDateFilterItemClick(nPastDays: Int, type: Int, isSelected: Boolean = false, showBottomBorder: Boolean = true): DateFilterItem.Click {
        val label: String = mContext.getString(R.string.stc_last_n_days, nPastDays)
        val startDate = Date(DateTimeUtil.getNPastDaysTimestamp(nPastDays.minus(1).toLong()))
        val endDate = Date()
        return DateFilterItem.Click(label, startDate, endDate, isSelected, type, showBottomBorder)
    }

    private fun getDateRangeItemToday(): DateFilterItem {
        val label = mContext.getString(R.string.stc_today_real_time)
        val today = Date()
        return DateFilterItem.Click(label, today, today, false, DateFilterItem.TYPE_TODAY)
    }
}