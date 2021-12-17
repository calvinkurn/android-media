package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.TableItemDivider

/**
 * Created By @ilhamsuaib on 01/07/20
 */

class TableItemDividerViewHolder(itemView: View?) : AbstractViewHolder<TableItemDivider>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_item_table_item_divider
    }

    override fun bind(element: TableItemDivider) {}
}