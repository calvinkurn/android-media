package com.tokopedia.product.edit.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.viewmodel.EmptyVideoViewModel

class EmptyVideoViewHolder(itemView: View) : AbstractViewHolder<EmptyVideoViewModel>(itemView) {

    init {
        findViews(itemView)
    }

    private fun findViews(view: View) {
    }

    override fun bind(emptyVideoViewModel: EmptyVideoViewModel) {
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_add_video_empty
    }
}