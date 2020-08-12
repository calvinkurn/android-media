package com.tokopedia.statistic.presentation.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.statistic.R
import com.tokopedia.statistic.presentation.view.adapter.DateFilterAdapter
import com.tokopedia.statistic.presentation.view.adapter.factory.DateFilterAdapterFactoryImpl
import com.tokopedia.statistic.presentation.view.model.DateFilterItem
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottomsheet_stc_select_date_range.view.*
import java.util.*

/**
 * Created By @ilhamsuaib on 15/06/20
 */

class DateFilterBottomSheet : BottomSheetUnify(), DateFilterAdapterFactoryImpl.Listener {

    companion object {
        const val TAG = "DateFilterBottomSheet"
        private const val DAYS_7 = 7
        private const val DAYS_30 = 30

        fun newInstance(): DateFilterBottomSheet {
            return DateFilterBottomSheet().apply {
                setStyle(DialogFragment.STYLE_NORMAL, R.style.StcDialogStyle)
                clearContentPadding = true
            }
        }
    }

    private var fm: FragmentManager? = null
    private var applyChangesCallback: ((DateFilterItem) -> Unit)? = null
    private val mAdapter: DateFilterAdapter? by lazy {
        DateFilterAdapter(this, fm ?: return@lazy null)
    }
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setChild(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onItemDateRangeClick(model: DateFilterItem) {
        items.forEach {
            if (it != model) {
                it.isSelected = false
            }
        }
        mAdapter?.notifyDataSetChanged()
    }

    override fun onApplyDateFilter() {
        val selectedItem = items.firstOrNull { it.isSelected } ?: return
        applyChangesCallback?.invoke(selectedItem)
        dismissAllowingStateLoss()
    }

    fun setFragmentManager(fm: FragmentManager): DateFilterBottomSheet {
        this.fm = fm
        return this
    }

    fun setOnApplyChanges(callback: (DateFilterItem) -> Unit): DateFilterBottomSheet {
        this.applyChangesCallback = callback
        return this
    }

    fun show() {
        fm?.let {
            show(it, TAG)
        }
    }

    private fun setupView() = view?.run {
        rvStcDateRage.run {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }

        mAdapter?.clearAllElements()
        mAdapter?.addElement(items)
    }

    private fun setChild(inflater: LayoutInflater, container: ViewGroup?) {
        val child: View = inflater.inflate(R.layout.bottomsheet_stc_select_date_range, container, false)
        setTitle(child.context.getString(R.string.stc_change_date_range))
        setChild(child)
    }

    private fun getFilterPerMonth(): DateFilterItem.MonthPickerItem {
        val perMonthLabel = context?.getString(R.string.stc_per_month) ?: "Per Bulan"
        return DateFilterItem.MonthPickerItem(perMonthLabel, startDate = Date(), endDate = Date())
    }

    private fun getDateFilterPerDay(): DateFilterItem.Pick {
        val label = view?.context?.getString(R.string.stc_per_day).orEmpty()
        val today = Date()
        return DateFilterItem.Pick(label, today, today, type = DateFilterItem.TYPE_PER_DAY)
    }

    private fun getDateFilterPerWeek(): DateFilterItem.Pick {
        val calendar: Calendar = Calendar.getInstance()
        with(calendar) {
            firstDayOfWeek = Calendar.MONDAY
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val firstDateOfWeek = calendar.time
        val lastDateOfWeek = Date()
        val label = context?.getString(R.string.stc_per_week).orEmpty()
        return DateFilterItem.Pick(label, firstDateOfWeek, lastDateOfWeek, type = DateFilterItem.TYPE_PER_WEEK)
    }

    private fun getDateFilterItemClick(nPastDays: Int, type: Int, isSelected: Boolean = false, showBottomBorder: Boolean = true): DateFilterItem.Click {
        val label: String = context?.getString(R.string.stc_last_n_days, nPastDays).orEmpty()
        val startDate = Date(DateTimeUtil.getNPastDaysTimestamp(nPastDays.minus(1).toLong()))
        val endDate = Date()
        return DateFilterItem.Click(label, startDate, endDate, isSelected, type, showBottomBorder)
    }

    private fun getDateRangeItemToday(): DateFilterItem {
        val label = context?.getString(R.string.stc_today_real_time).orEmpty()
        val today = Date()
        return DateFilterItem.Click(label, today, today, false, DateFilterItem.TYPE_TODAY)
    }
}