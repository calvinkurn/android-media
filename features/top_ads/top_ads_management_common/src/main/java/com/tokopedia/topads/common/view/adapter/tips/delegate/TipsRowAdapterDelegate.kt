package com.tokopedia.topads.common.view.adapter.tips.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.view.adapter.tips.viewholder.TipsUiViewHolder
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiModel
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiRowModel

class TipsRowAdapterDelegate : TypedAdapterDelegate<TipsUiRowModel, TipsUiModel, TipsUiViewHolder>(R.layout.item_topads_common_tips_row_layout) {

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): TipsUiViewHolder {
        return TipsUiViewHolder(basicView)
    }

    override fun onBindViewHolder(item: TipsUiRowModel, holder: TipsUiViewHolder) {
        holder.bindRow(item)
    }
}