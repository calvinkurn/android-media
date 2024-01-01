package com.tokopedia.catalogcommon.viewholder.columnedinfo

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalogcommon.uimodel.ColumnedInfoUiModel
import com.tokopedia.kotlin.extensions.view.orZero

class ColumnedInfoThreeColumnListItemAdapter(
    private val items: List<ColumnedInfoUiModel.ColumnData> = listOf(),
    private val columnType: String = ""
) : RecyclerView.Adapter<ColumnedInfoThreeColumnListItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ColumnedInfoThreeColumnListItemViewHolder {
        val rootView = ColumnedInfoThreeColumnListItemViewHolder.createRootView(parent)
        return ColumnedInfoThreeColumnListItemViewHolder(rootView, columnType)
    }

    override fun onBindViewHolder(
        holder: ColumnedInfoThreeColumnListItemViewHolder,
        position: Int
    ) {
        holder.bind(items, position)
    }

    override fun getItemCount() = items.firstOrNull()?.rowData?.count().orZero()
}
