package com.tokopedia.statistic.presentation.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.statistic.R
import com.tokopedia.statistic.presentation.model.DateRangeItem
import kotlinx.android.synthetic.main.item_stc_date_range_default.view.*

/**
 * Created By @ilhamsuaib on 15/06/20
 */

class DateRangeDefaultViewHolder(
        itemView: View?,
        private val onClick: (DateRangeItem) -> Unit
) : AbstractViewHolder<DateRangeItem.Default>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_stc_date_range_default

        private const val PATTERN_DAY = "dd"
        private const val PATTERN_MONTH_MM = "MM"
        private const val PATTERN_MONTH_MMM = "MMM"
        private const val PATTERN_YEAR = "yyyy"
    }

    override fun bind(element: DateRangeItem.Default) {
        with(itemView) {
            setBackgroundColor(context.getResColor(R.color.Neutral_N0))

            tvStcDateRangeLabel.text = element.label
            tvStcDefaultDateRange.text = getDateRangeStr(element)
            radStcDefaultDateRange.isChecked = element.isSelected

            setOnClickListener {
                onClick(element)
                element.isSelected = true
            }
        }
    }

    private fun getDateRangeStr(element: DateRangeItem.Default): String {
        val startMonth = DateTimeUtil.format(element.startDate.time, PATTERN_MONTH_MM)
        val endMonth = DateTimeUtil.format(element.endDate.time, PATTERN_MONTH_MM)
        val startYear = DateTimeUtil.format(element.startDate.time, PATTERN_YEAR)
        val endYear = DateTimeUtil.format(element.endDate.time, PATTERN_YEAR)


        val startDatePattern: String
        val endDatePattern: String
        when {
            startMonth == endMonth && startYear == endYear -> {
                //ex : 12 - 15 Apr 2020
                startDatePattern = PATTERN_DAY
                endDatePattern = "$PATTERN_DAY $PATTERN_MONTH_MMM $PATTERN_YEAR"
            }
            startMonth != endMonth && startYear == endYear -> {
                //ex : 12 Jan - 15 Apr 2020
                startDatePattern = "$PATTERN_DAY $PATTERN_MONTH_MMM"
                endDatePattern = "$PATTERN_DAY $PATTERN_MONTH_MMM $PATTERN_YEAR"
            }
            else -> {
                //ex : 12 Jan 2020 - 15 Apr 2020
                startDatePattern = "$PATTERN_DAY $PATTERN_MONTH_MMM $PATTERN_YEAR"
                endDatePattern = "$PATTERN_DAY $PATTERN_MONTH_MMM $PATTERN_YEAR"
            }
        }

        val startDateStr = DateTimeUtil.format(element.startDate.time, pattern = startDatePattern)
        val endDateStr = DateTimeUtil.format(element.endDate.time, pattern = endDatePattern)

        return "$startDateStr - $endDateStr"
    }
}