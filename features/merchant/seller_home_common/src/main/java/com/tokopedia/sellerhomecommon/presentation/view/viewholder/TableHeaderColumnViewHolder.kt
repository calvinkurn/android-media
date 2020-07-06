package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.Gravity
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.TableHeaderUiModel
import kotlinx.android.synthetic.main.shc_item_table_column_header.view.*

/**
 * Created By @ilhamsuaib on 01/07/20
 */

class TableHeaderColumnViewHolder(itemView: View?) : AbstractViewHolder<TableHeaderUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_item_table_column_header
    }

    override fun bind(element: TableHeaderUiModel) {
        with(itemView) {
            tvTableHeader.text = element.title
            if (element.isLeftAlign) {
                tvTableHeader.gravity = Gravity.START
            } else {
                tvTableHeader.gravity = Gravity.END
            }
        }
    }
}