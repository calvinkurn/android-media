package com.tokopedia.statistic.presentation.view.bottomsheet

import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.statistic.R
import com.tokopedia.statistic.presentation.model.DateRangeItem
import com.tokopedia.statistic.presentation.view.adapter.DateRangeAdapter
import com.tokopedia.statistic.presentation.view.adapter.factory.DateRangeAdapterFactoryImpl
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottomsheet_stc_select_date_range.view.*
import java.util.*

/**
 * Created By @ilhamsuaib on 15/06/20
 */

class SelectDateRageBottomSheet(
        private val mContext: Context,
        private val fm: FragmentManager
) : BottomSheetUnify(), DateRangeAdapterFactoryImpl.Listener {

    companion object {
        private const val DAYS_7 = 7
        private const val DAYS_30 = 30
    }

    private var applyChangesCallback: (DateRangeItem) -> Unit = {}
    private val mAdapter by lazy { DateRangeAdapter(this, fm) }
    private val items: MutableList<DateRangeItem> by lazy {
        mutableListOf(
                getDateRangeItemToday(),
                getDateRangeItemClick(DAYS_7, DateRangeItem.TYPE_LAST_7_DAYS, true),
                getDateRangeItemClick(DAYS_30, DateRangeItem.TYPE_LAST_30_DAYS, showBottomBorder = false),
                DateRangeItem.Divider,
                getDateRangeItemPick(DateRangeItem.TYPE_PER_DAY),
                getDateRangeItemPick(DateRangeItem.TYPE_PER_WEEK),
                getDateRangeItemPick(DateRangeItem.TYPE_PER_MONTH),
                DateRangeItem.ApplyButton
        )
    }

    init {
        val child: View = View.inflate(mContext, R.layout.bottomsheet_stc_select_date_range, null)
        setTitle(mContext.getString(R.string.stc_change_date_range))
        setChild(child)

        clearContentPadding = true
        setupView(child)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onItemDateRangeClick(model: DateRangeItem) {
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

    fun setOnApplyChanges(callback: (DateRangeItem) -> Unit): SelectDateRageBottomSheet {
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

    private fun getDateRangeItemPick(type: Int): DateRangeItem.Pick {
        @StringRes val labelId = when (type) {
            DateRangeItem.TYPE_PER_DAY -> R.string.stc_per_day
            DateRangeItem.TYPE_PER_WEEK -> R.string.stc_per_week
            else -> R.string.stc_per_month
        }
        val label = mContext.getString(labelId)
        return DateRangeItem.Pick(label = label, type = type)
    }

    private fun getDateRangeItemClick(nPastDays: Int, type: Int, isSelected: Boolean = false, showBottomBorder: Boolean = true): DateRangeItem.Click {
        val label: String = mContext.getString(R.string.stc_last_n_days, nPastDays)
        val startDate = Date(DateTimeUtil.getNPastDaysTimestamp(nPastDays.minus(1).toLong()))
        val endDate = Date()
        return DateRangeItem.Click(label, startDate, endDate, isSelected, type, showBottomBorder)
    }

    private fun getDateRangeItemToday(): DateRangeItem {
        val label = mContext.getString(R.string.stc_today_real_time)
        val today = Date()
        return DateRangeItem.Click(label, today, today, false, DateRangeItem.TYPE_TODAY)
    }
}