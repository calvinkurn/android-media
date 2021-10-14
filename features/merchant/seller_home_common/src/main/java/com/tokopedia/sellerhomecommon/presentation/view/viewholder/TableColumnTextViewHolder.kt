package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.Gravity
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcItemTableColumnTextBinding
import com.tokopedia.sellerhomecommon.presentation.model.TableRowsUiModel

/**
 * Created By @ilhamsuaib on 01/07/20
 */

class TableColumnTextViewHolder(
    itemView: View
) : AbstractViewHolder<TableRowsUiModel.RowColumnText>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_item_table_column_text
    }

    private val binding by lazy {
        ShcItemTableColumnTextBinding.bind(itemView)
    }

    override fun bind(element: TableRowsUiModel.RowColumnText) {
        with(binding) {
            tvTableColumnText.text = element.valueStr
            if (element.isLeftAlign) {
                tvTableColumnText.gravity = Gravity.START
            } else {
                tvTableColumnText.gravity = Gravity.END
            }
        }
    }
}