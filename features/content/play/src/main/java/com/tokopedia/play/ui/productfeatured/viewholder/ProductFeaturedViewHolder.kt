package com.tokopedia.play.ui.productfeatured.viewholder

import android.view.View
import com.tokopedia.play.R
import com.tokopedia.play.ui.product.ProductBasicViewHolder

/**
 * Created by jegul on 23/02/21
 */
class ProductFeaturedViewHolder(
        itemView: View,
        listener: Listener,
) : ProductBasicViewHolder(itemView, listener) {

    companion object {
        val LAYOUT = R.layout.item_play_product_featured
    }
}