package com.tokopedia.feedcomponent.view.adapter.viewholder.shimmer

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.viewmodel.shimmer.ShimmerUiModel

class ShimmerViewHolder(view: View) : AbstractViewHolder<ShimmerUiModel>(view) {

    override fun bind(element: ShimmerUiModel?) {

    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_post_shimmer
    }

}