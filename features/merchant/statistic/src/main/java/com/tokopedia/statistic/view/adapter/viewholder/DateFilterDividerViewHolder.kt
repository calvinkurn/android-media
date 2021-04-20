package com.tokopedia.statistic.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.statistic.R
import com.tokopedia.statistic.view.model.DateFilterItem

/**
 * Created By @ilhamsuaib on 17/07/20
 */

class DateFilterDividerViewHolder(itemView: View?) : AbstractViewHolder<DateFilterItem.Divider>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_stc_date_range_divider
    }

    override fun bind(element: DateFilterItem.Divider?) {

    }
}