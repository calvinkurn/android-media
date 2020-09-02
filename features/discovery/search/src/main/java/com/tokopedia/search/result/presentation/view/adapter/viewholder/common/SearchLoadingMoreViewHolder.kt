package com.tokopedia.search.result.presentation.view.adapter.viewholder.common

import androidx.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R

class SearchLoadingMoreViewHolder(itemView: View) : AbstractViewHolder<LoadingMoreModel>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_loading_layout
    }

    override fun bind(element: LoadingMoreModel?) {
        // Not binding anything, just show the loading layout
    }
}