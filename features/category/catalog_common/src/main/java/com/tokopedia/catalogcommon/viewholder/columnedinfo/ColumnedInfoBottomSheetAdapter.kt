package com.tokopedia.catalogcommon.viewholder.columnedinfo

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalogcommon.uimodel.ColumnedInfoUiModel

class ColumnedInfoBottomSheetAdapter(
    private val items: List<ColumnedInfoUiModel.ColumnData> = emptyList()
) : RecyclerView.Adapter<ColumnedInfoBottomSheetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColumnedInfoBottomSheetViewHolder {
        val rootView = ColumnedInfoBottomSheetViewHolder.createRootView(parent)
        return ColumnedInfoBottomSheetViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: ColumnedInfoBottomSheetViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
