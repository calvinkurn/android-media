package com.tokopedia.play.ui.productfeatured.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.R

/**
 * Created by jegul on 24/02/21
 */
class ProductFeaturedSeeMoreViewHolder(
        itemView: View,
        listener: Listener
) : BaseViewHolder(itemView) {

    init {
        itemView.setOnClickListener { listener.onSeeMoreClicked() }
    }

    companion object {
        val LAYOUT = R.layout.item_play_product_featured_more_action
    }

    interface Listener {

        fun onSeeMoreClicked()
    }
}