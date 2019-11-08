package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import androidx.annotation.LayoutRes
import android.view.View

import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R

class HomeFeedLoadingMoreViewHolder(itemView: View) : AbstractViewHolder<LoadingMoreModel>(itemView) {

    override fun bind(element: LoadingMoreModel) {

    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.loading_layout
    }
}