package com.tokopedia.topads.common.view.adapter.tips.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.view.adapter.tips.viewholder.TipsUiViewHolder
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiHeaderModel
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiModel

class TipsHeaderAdapterDelegate : TypedAdapterDelegate<TipsUiHeaderModel, TipsUiModel, TipsUiViewHolder>(R.layout.item_topads_common_tips_header_layout) {
    override fun onBindViewHolder(item: TipsUiHeaderModel, holder: TipsUiViewHolder) {
        holder.bindHeader(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): TipsUiViewHolder {
        return TipsUiViewHolder(basicView)
    }
}