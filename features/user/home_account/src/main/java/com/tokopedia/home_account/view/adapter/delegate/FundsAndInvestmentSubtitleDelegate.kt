package com.tokopedia.home_account.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.home_account.view.adapter.uimodel.SubtitleUiModel
import com.tokopedia.home_account.view.adapter.viewholder.SubtitleViewHolder

class FundsAndInvestmentSubtitleDelegate :
    TypedAdapterDelegate<SubtitleUiModel, Any, SubtitleViewHolder>(SubtitleViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: SubtitleUiModel, holder: SubtitleViewHolder) {
        val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = true
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): SubtitleViewHolder {
        return SubtitleViewHolder(basicView)
    }
}