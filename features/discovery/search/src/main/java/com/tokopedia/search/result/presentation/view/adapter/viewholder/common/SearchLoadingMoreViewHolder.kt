package com.tokopedia.search.result.presentation.view.adapter.viewholder.common

import androidx.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

class SearchLoadingMoreViewHolder(itemView: View) : AbstractViewHolder<LoadingMoreModel>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = com.tokopedia.baselist.R.layout.loading_layout
    }

    override fun bind(element: LoadingMoreModel?) {
        // Not binding anything, just show the loading layout
    }
}