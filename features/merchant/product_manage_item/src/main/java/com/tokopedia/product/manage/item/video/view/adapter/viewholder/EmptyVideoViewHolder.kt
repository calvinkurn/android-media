package com.tokopedia.product.manage.item.video.view.adapter.viewholder

import androidx.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.video.view.model.EmptyVideoViewModel

class EmptyVideoViewHolder(itemView: View) : AbstractViewHolder<EmptyVideoViewModel>(itemView) {

    override fun bind(emptyVideoViewModel: EmptyVideoViewModel) {
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_add_video_empty
    }
}