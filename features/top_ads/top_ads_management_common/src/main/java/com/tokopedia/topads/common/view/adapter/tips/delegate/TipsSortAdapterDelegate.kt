package com.tokopedia.topads.common.view.adapter.tips.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.view.adapter.tips.viewholder.TipsUiSortViewHolder
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiModel
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiSortModel

class TipsSortAdapterDelegate(var onItemClick:TipsUiSortViewHolder.OnUiSortItemClick? = null)
    : TypedAdapterDelegate<TipsUiSortModel, TipsUiModel, TipsUiSortViewHolder>(R.layout.item_topads_common_tips_sort_layout) {
    override fun onBindViewHolder(item: TipsUiSortModel, holder: TipsUiSortViewHolder) {
        holder.bindSortItem(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): TipsUiSortViewHolder {
        return TipsUiSortViewHolder(basicView, onItemClick)
    }
}