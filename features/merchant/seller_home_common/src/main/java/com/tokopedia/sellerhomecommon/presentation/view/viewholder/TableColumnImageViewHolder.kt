package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.TableRowsUiModel
import kotlinx.android.synthetic.main.shc_item_table_column_image.view.*

/**
 * Created By @ilhamsuaib on 01/07/20
 */

class TableColumnImageViewHolder(itemView: View?) : AbstractViewHolder<TableRowsUiModel.RowColumnImage>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_item_table_column_image
    }

    override fun bind(element: TableRowsUiModel.RowColumnImage) {
        with(itemView) {
            val dp4 = context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)
            ImageHandler.loadImageRounded(context, imgTableColumn, element.valueStr, dp4)
        }
    }
}