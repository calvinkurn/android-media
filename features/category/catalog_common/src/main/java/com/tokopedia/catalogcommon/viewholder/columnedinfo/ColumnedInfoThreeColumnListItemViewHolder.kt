package com.tokopedia.catalogcommon.viewholder.columnedinfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetItemColumnedInfoListItemThreeColumnBinding
import com.tokopedia.catalogcommon.uimodel.ColumnedInfoUiModel
import com.tokopedia.catalogcommon.uimodel.ColumnedInfoUiModel.Companion.CELL_TITLE_ON_3_COLUMN_TYPE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.utils.view.binding.viewBinding

class ColumnedInfoThreeColumnListItemViewHolder(
    itemView: View,
    private val columnType: String
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        private const val FIRST_INDEX_COLUMN = 0
        private const val SECOND_INDEX_COLUMN = 1
        private const val THIRD_INDEX_COLUMN = 2

        fun createRootView(parent: ViewGroup): View = LayoutInflater.from(parent.context)
            .inflate(R.layout.widget_item_columned_info_list_item_three_column, parent, false)
    }

    private val binding: WidgetItemColumnedInfoListItemThreeColumnBinding? by viewBinding()

    fun bind(
        columnData: List<ColumnedInfoUiModel.ColumnData>,
        position: Int
    ) {
        val isFirstRow = position == Int.ZERO
        val rowData1 = columnData.getOrNull(FIRST_INDEX_COLUMN)?.rowData?.getOrNull(position)
        val rowData2 = columnData.getOrNull(SECOND_INDEX_COLUMN)?.rowData?.getOrNull(position)
        val rowData3 = columnData.getOrNull(THIRD_INDEX_COLUMN)?.rowData?.getOrNull(position)
        val rowColor = columnData.firstOrNull()?.rowColor

        binding?.apply {
            tfTitle1.text = rowData1?.first.orEmpty()
            tfTitle2.text = rowData2?.first.orEmpty()
            tfTitle3.text = rowData3?.first.orEmpty()

            tfValue1.text = rowData1?.second.orEmpty()
            tfValue2.text = rowData2?.second.orEmpty()
            tfValue3.text = rowData3?.second.orEmpty()

            tfTitle1.isVisible = isFirstRow || columnType == CELL_TITLE_ON_3_COLUMN_TYPE
            tfTitle2.isVisible = isFirstRow || columnType == CELL_TITLE_ON_3_COLUMN_TYPE
            tfTitle3.isVisible = isFirstRow || columnType == CELL_TITLE_ON_3_COLUMN_TYPE

            tfTitle1.setTextColor(rowColor?.first ?: return)
            tfTitle2.setTextColor(rowColor.first)
            tfTitle3.setTextColor(rowColor.first)

            tfValue1.setTextColor(rowColor.second)
            tfValue2.setTextColor(rowColor.second)
            tfValue3.setTextColor(rowColor.second)
        }
    }
}
