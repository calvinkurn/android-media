package com.tokopedia.catalogcommon.viewholder.columnedinfo

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalogcommon.uimodel.ColumnedInfoUiModel
import com.tokopedia.kotlin.extensions.view.orZero

class ColumnedInfoTwoColumnListItemAdapter(
    private val items: List<ColumnedInfoUiModel.ColumnData> = listOf()
) : RecyclerView.Adapter<ColumnedInfoTwoColumnListItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ColumnedInfoTwoColumnListItemViewHolder {
        val rootView = ColumnedInfoTwoColumnListItemViewHolder.createRootView(parent)
        return ColumnedInfoTwoColumnListItemViewHolder(rootView)
    }

    override fun onBindViewHolder(
        holder: ColumnedInfoTwoColumnListItemViewHolder,
        position: Int
    ) {
        holder.bind(items, position)
    }

    override fun getItemCount() = items.firstOrNull()?.rowData?.count().orZero()
}
