package com.tokopedia.catalogcommon.viewholder.columnedinfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetItemColumnedInfoListItemBinding
import com.tokopedia.catalogcommon.uimodel.ColumnedInfoUiModel
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.utils.view.binding.viewBinding

class ColumnedInfoListItemViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        fun createRootView(parent: ViewGroup): View = LayoutInflater.from(parent.context)
            .inflate(R.layout.widget_item_columned_info_list_item, parent, false)
    }

    private val binding: WidgetItemColumnedInfoListItemBinding? by viewBinding()

    fun bind(
        columnData: ColumnedInfoUiModel.ColumnData,
        position: Int
    ) {
        val rowData = columnData.rowData.getOrNull(position) ?: return
        val rowColor = columnData.rowColor
        binding?.apply {
            tfTitle.text = rowData.first
            tfValue.setTextAndCheckShow(rowData.second)
            tfTitle.setTextColor(rowColor.first)
            tfValue.setTextColor(rowColor.second)
            divItem.isVisible = position < columnData.rowData.size.dec()
        }
    }
}
