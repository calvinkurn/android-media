package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.Gravity
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.TableRowsUiModel
import kotlinx.android.synthetic.main.shc_item_table_column_text.view.*

/**
 * Created By @ilhamsuaib on 01/07/20
 */

class TableColumnTextViewHolder(itemView: View?) : AbstractViewHolder<TableRowsUiModel.RowColumnText>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_item_table_column_text
    }

    override fun bind(element: TableRowsUiModel.RowColumnText) {
        with(itemView) {
            tvTableColumnText.text = element.valueStr
            if (element.isLeftAlign) {
                tvTableColumnText.gravity = Gravity.START
            } else {
                tvTableColumnText.gravity = Gravity.END
            }
        }
    }
}