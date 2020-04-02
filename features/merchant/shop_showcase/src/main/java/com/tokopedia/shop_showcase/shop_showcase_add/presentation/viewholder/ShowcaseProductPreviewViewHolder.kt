package com.tokopedia.shop_showcase.shop_showcase_add.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.common.util.getCurrencyFormatted
import com.tokopedia.shop_showcase.shop_showcase_add.presentation.listener.ShopShowcasePreviewListener
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.BaseShowcaseProduct
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.ShowcaseProduct
import kotlinx.android.synthetic.main.item_add_product_showcase_grid.view.*

class ShowcaseProductPreviewViewHolder(itemView: View, private val previewListener: ShopShowcasePreviewListener): RecyclerView.ViewHolder(itemView) {

    fun bind(element: BaseShowcaseProduct) {
        if (element is ShowcaseProduct) {
            if(element.isCloseable) renderDeleteButton()
            renderProductCard(element)
        }
    }

    private fun renderProductCard(element: ShowcaseProduct) {
        itemView.product_name.text = element.productName
        itemView.product_price.text = element.productPrice.getCurrencyFormatted()
        itemView.ratingBar.rating = element.ratingStarAvg
        itemView.total_review.text = itemView.resources.getString(R.string.product_total_review, element.totalReview.toString())
        itemView.product_image.setImageUrl(element.productImageUrl)
    }

    private fun renderDeleteButton() {
        itemView.delete_product_showcase.setImageDrawable(itemView.context.getResDrawable(R.drawable.ic_delete_card))
        itemView.delete_product_showcase.visibility = View.VISIBLE
        itemView.delete_product_showcase.setOnClickListener {
            previewListener.deleteSelectedProduct(adapterPosition)
        }
    }

}