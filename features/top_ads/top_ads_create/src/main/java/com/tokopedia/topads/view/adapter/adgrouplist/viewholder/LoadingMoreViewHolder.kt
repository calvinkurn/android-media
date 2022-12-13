package com.tokopedia.topads.view.adapter.adgrouplist.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.view.adapter.adgrouplist.model.LoadingMoreUiModel
import com.tokopedia.topads.create.R

class LoadingMoreViewHolder(itemView:View) : AbstractViewHolder<LoadingMoreUiModel>(itemView) {
    companion object{
        val LAYOUT = R.layout.load_more_viewholder_layout
    }
    override fun bind(element: LoadingMoreUiModel?) {}
}
