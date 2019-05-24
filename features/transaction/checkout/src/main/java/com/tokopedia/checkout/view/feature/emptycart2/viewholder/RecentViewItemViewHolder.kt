package com.tokopedia.checkout.view.feature.emptycart2.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.emptycart2.ActionListener
import com.tokopedia.checkout.view.feature.emptycart2.uimodel.RecentViewItemUiModel
import kotlinx.android.synthetic.main.item_empty_cart_recent_view_inner.view.*

/**
 * Created by Irfan Khoirul on 2019-05-20.
 */

class RecentViewItemViewHolder(val view: View, val listener: ActionListener, val itemWidth: Int) : RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_empty_cart_recent_view_inner
    }

    fun bind(element: RecentViewItemUiModel) {
        itemView.tv_product_name.text = element.recentView.productName
        itemView.tv_product_price.text = element.recentView.productPrice
        ImageHandler.loadImage(itemView.img_product.context, itemView.img_product,
                element.recentView.productImage, R.drawable.loading_page
        )

        itemView.img_product.layoutParams.width = itemWidth
        itemView.img_product.layoutParams.height = itemWidth
        itemView.img_product.requestLayout()

        itemView.setOnClickListener {
            listener.onItemRecentViewClicked(element.recentView, adapterPosition + 1)
        }
    }

}