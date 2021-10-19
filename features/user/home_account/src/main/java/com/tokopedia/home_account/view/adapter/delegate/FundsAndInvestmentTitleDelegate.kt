package com.tokopedia.home_account.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.home_account.view.adapter.uimodel.TitleUiModel
import com.tokopedia.home_account.view.adapter.viewholder.TitleViewHolder

class FundsAndInvestmentTitleDelegate :
    TypedAdapterDelegate<TitleUiModel, Any, TitleViewHolder>(TitleViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: TitleUiModel, holder: TitleViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): TitleViewHolder {
        return TitleViewHolder(basicView)
    }
}