package com.tokopedia.catalogcommon.viewholder.columnedinfo

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalogcommon.uimodel.ColumnedInfoUiModel

class ColumnedInfoListItemAdapter(
    private val items: ColumnedInfoUiModel.ColumnData = ColumnedInfoUiModel.ColumnData()
) : RecyclerView.Adapter<ColumnedInfoListItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColumnedInfoListItemViewHolder {
        val rootView = ColumnedInfoListItemViewHolder.createRootView(parent)
        return ColumnedInfoListItemViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: ColumnedInfoListItemViewHolder, position: Int) {
        holder.bind(items.rowData[position], items.rowColor, position < itemCount.dec())
    }

    override fun getItemCount() = items.rowData.size
}
