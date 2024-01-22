package com.tokopedia.catalogcommon.viewholder.columnedinfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetItemColumnedInfoListItemTwoColumnBinding
import com.tokopedia.catalogcommon.uimodel.ColumnedInfoUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ColumnedInfoTwoColumnListItemViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        private const val FIRST_INDEX_COLUMN = 0
        private const val SECOND_INDEX_COLUMN = 1

        fun createRootView(parent: ViewGroup): View = LayoutInflater.from(parent.context)
            .inflate(R.layout.widget_item_columned_info_list_item_two_column, parent, false)
    }

    private val binding: WidgetItemColumnedInfoListItemTwoColumnBinding? by viewBinding()

    fun bind(
        columnData: List<ColumnedInfoUiModel.ColumnData>,
        position: Int
    ) {
        val rowData1 = columnData.getOrNull(FIRST_INDEX_COLUMN)?.rowData?.getOrNull(position)
        val rowData2 = columnData.getOrNull(SECOND_INDEX_COLUMN)?.rowData?.getOrNull(position)
        val rowColor = columnData.firstOrNull()?.rowColor

        binding?.apply {
            tfValue1.text = rowData1?.second.orEmpty()
            tfValue2.text = rowData2?.second.orEmpty()
            tfValue1.setTextColor(rowColor?.second ?: return)
            tfValue2.setTextColor(rowColor?.second ?: return)
        }
    }
}
