package com.tokopedia.checkout.view.feature.cartlist.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.cartlist.ActionListener
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartRecentViewItemHolderData
import kotlinx.android.synthetic.main.item_cart_recent_view_item.view.*

/**
 * Created by Irfan Khoirul on 2019-06-15.
 */

class CartRecentViewItemViewHolder(val view: View, val actionListener: ActionListener, val itemWidth: Int) : RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_cart_recent_view_item
    }

    fun bind(element: CartRecentViewItemHolderData) {
        itemView.tv_product_name.text = element.name
        itemView.tv_product_price.text = element.price
        ImageHandler.loadImage(itemView.context, itemView.img_product,
                element.imageUrl, R.drawable.loading_page
        )

        itemView.img_product.layoutParams.width = itemWidth
        itemView.img_product.layoutParams.height = itemWidth
        itemView.img_product.requestLayout()

        itemView.setOnClickListener { v ->
//            actionListener.onItemRecentViewClicked(
//                    recentViewViewModel.getRecentView(), adapterPosition + 1)
        }

    }

}