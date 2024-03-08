package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcItemTableColumnImageBinding
import com.tokopedia.sellerhomecommon.presentation.model.TableRowsUiModel

/**
 * Created By @ilhamsuaib on 01/07/20
 */

class TableColumnImageViewHolder(
    itemView: View
) : AbstractViewHolder<TableRowsUiModel.RowColumnImage>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_item_table_column_image
    }

    private val binding by lazy { ShcItemTableColumnImageBinding.bind(itemView) }

    override fun bind(element: TableRowsUiModel.RowColumnImage) {
        with(binding) {
            val dp4 = root.context.resources.getDimension(
                com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2
            )
            imgTableColumn?.loadImageRounded(element.valueStr, dp4)
        }
    }
}