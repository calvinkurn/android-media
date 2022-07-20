package com.tokopedia.play.broadcaster.ui.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel

/**
 * @author by furqan on 07/06/2020
 */
class PlayCoverProductViewHolder(
        itemView: View,
        private val listener: Listener
) : BaseViewHolder(itemView) {

    private val ivThumbnail = itemView.findViewById<ImageView>(R.id.iv_thumbnail)

    fun bind(item: ProductUiModel) {
        ivThumbnail.loadImage(item.imageUrl)
        ivThumbnail.setOnClickListener {
            listener.onProductCoverClicked(item.id, item.imageUrl)
        }
    }

    interface Listener {
        fun onProductCoverClicked(productId: String, imageUrl: String)
    }

    companion object {
        val LAYOUT = R.layout.item_play_cover_from_product
    }
}