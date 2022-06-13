package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.DateFilterItem

/**
 * Created by @ilhamsuaib on 09/02/22.
 */

class DateFilterDividerViewHolder(itemView: View) :
    AbstractViewHolder<DateFilterItem.Divider>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_shc_date_range_divider
    }

    override fun bind(element: DateFilterItem.Divider?) {

    }
}