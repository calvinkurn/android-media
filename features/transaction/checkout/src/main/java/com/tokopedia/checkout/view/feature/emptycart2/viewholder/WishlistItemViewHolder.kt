package com.tokopedia.checkout.view.feature.emptycart2.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.emptycart2.ActionListener
import com.tokopedia.checkout.view.feature.emptycart2.uimodel.WishlistItemUiModel
import kotlinx.android.synthetic.main.item_empty_cart_wishlist_inner.view.*

/**
 * Created by Irfan Khoirul on 2019-05-20.
 */

class WishlistItemViewHolder(val view: View, val listener: ActionListener, val itemWidth: Int) : RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_empty_cart_wishlist_inner
    }

    fun bind(element: WishlistItemUiModel) {
        itemView.tv_product_name.text = element.wishlist.name
        itemView.tv_product_price.text = element.wishlist.priceFmt
        ImageHandler.loadImage(itemView.img_product.context, itemView.img_product,
                element.wishlist.imageUrl, R.drawable.loading_page
        )

        itemView.img_product.layoutParams.width = itemWidth
        itemView.img_product.layoutParams.height = itemWidth
        itemView.img_product.requestLayout()

        itemView.setOnClickListener {
            listener.onItemWishListClicked(element.wishlist, adapterPosition + 1)
        }
    }

}