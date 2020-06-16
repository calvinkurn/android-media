package com.tokopedia.statistic.presentation.view.bottomsheet

import android.content.Context
import android.view.View
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
        private const val DAY_1 = 1
        private const val DAYS_7 = 7
        private const val DAYS_30 = 30
        private const val DAYS_90 = 90
    }

    private var applyChangesCallback: (DateRangeItem) -> Unit = {}
    private val mAdapter by lazy { DateRangeAdapter(this, fm) }
    private val items: List<DateRangeItem> by lazy {
        listOf(getDateRangeItem(DAYS_7, true),
                getDateRangeItem(DAYS_30), getDateRangeItem(DAYS_90),
                DateRangeItem.Custom(mContext.getString(R.string.stc_custom))
        )
    }

    init {
        val child: View = View.inflate(mContext, R.layout.bottomsheet_stc_select_date_range, null)
        setTitle(mContext.getString(R.string.stc_change_date_range))
        setChild(child)

        setupView(child)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onApplyDateFilter(model: DateRangeItem) {
        applyChangesCallback(model)
        dismissAllowingStateLoss()
    }

    override fun onItemDateRangeClick(model: DateRangeItem) {
        items.forEach {
            if (it != model) {
                it.isSelected = false
            }
        }
        mAdapter.notifyDataSetChanged()
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

    private fun getDateRangeItem(nPastDays: Int, isSelected: Boolean = false): DateRangeItem.Default {
        val label: String = mContext.getString(R.string.stc_last_n_days, nPastDays)
        val startDate = Date(DateTimeUtil.getNPastDaysTimestamp(nPastDays.toLong()))
        val endDate = Date(DateTimeUtil.getNPastDaysTimestamp(DAY_1.toLong()))
        return DateRangeItem.Default(label, startDate, endDate, isSelected)
    }
}