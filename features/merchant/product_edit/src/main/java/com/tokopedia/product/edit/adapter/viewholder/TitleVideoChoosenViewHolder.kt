package com.tokopedia.product.edit.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.viewmodel.TitleVideoChoosenViewModel

class TitleVideoChoosenViewHolder(itemView: View) : AbstractViewHolder<TitleVideoChoosenViewModel>(itemView) {

    init {
        findViews(itemView)
    }

    private fun findViews(view: View) {
    }

    override fun bind(titleVideoChoosenViewModel: TitleVideoChoosenViewModel) {
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_add_video_choosen_title
    }
}