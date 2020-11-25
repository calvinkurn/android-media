package com.tokopedia.common_category.viewholders

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common_category.R

class HotlistLoadMoreViewHolder(itemView: View?) : AbstractViewHolder<LoadingMoreModel?>(itemView) {
    override fun bind(element: LoadingMoreModel?) {
    }

    companion object {
        @LayoutRes
        val LAYOUT = com.tokopedia.baselist.R.layout.loading_layout
    }
}