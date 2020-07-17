package com.tokopedia.statistic.presentation.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.statistic.R
import com.tokopedia.statistic.presentation.model.DateRangeItem

/**
 * Created By @ilhamsuaib on 17/07/20
 */

class DateRangeDividerViewHolder(itemView: View?) : AbstractViewHolder<DateRangeItem.Divider>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_stc_date_range_divider
    }

    override fun bind(element: DateRangeItem.Divider?) {

    }
}