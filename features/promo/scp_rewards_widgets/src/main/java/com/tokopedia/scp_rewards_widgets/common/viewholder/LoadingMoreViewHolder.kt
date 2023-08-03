package com.tokopedia.scp_rewards_widgets.common.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.scp_rewards_widgets.R
import com.tokopedia.scp_rewards_widgets.common.model.LoadingMoreModel

class LoadingMoreViewHolder(itemView:View) : AbstractViewHolder<LoadingMoreModel>(itemView) {
    companion object{
        @LayoutRes
        val LAYOUT = R.layout.infinite_loading_layout
    }

    override fun bind(element: LoadingMoreModel?) {}
}
