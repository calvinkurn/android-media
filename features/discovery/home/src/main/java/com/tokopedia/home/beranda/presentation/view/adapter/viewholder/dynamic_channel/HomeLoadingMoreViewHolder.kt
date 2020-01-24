package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.annotation.LayoutRes
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeLoadingMoreModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.HomeAbstractViewHolder

class HomeLoadingMoreViewHolder(itemView: View) : HomeAbstractViewHolder<HomeLoadingMoreModel>(itemView) {

    override fun bind(element: HomeLoadingMoreModel) {
        itemView.layoutParams = AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_loading_more_layout
    }

}
