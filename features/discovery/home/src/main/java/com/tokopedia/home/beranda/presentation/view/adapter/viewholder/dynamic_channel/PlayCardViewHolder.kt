package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCardViewModel

class PlayCardViewHolder(view: View, val listener: HomeCategoryListener): AbstractViewHolder<PlayCardViewModel>(view) {

    override fun bind(element: PlayCardViewModel?) {
        listener.onGetPlayBanner(adapterPosition)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_home_play_card
    }

}