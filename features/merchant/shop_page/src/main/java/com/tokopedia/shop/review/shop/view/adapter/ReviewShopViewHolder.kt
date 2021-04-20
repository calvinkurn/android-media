package com.tokopedia.shop.review.shop.view.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.shop.R
import com.tokopedia.shop.review.product.view.adapter.ReviewProductContentViewHolder
import com.tokopedia.shop.review.product.view.adapter.ReviewProductModelContent

/**
 * Created by zulfikarrahman on 1/19/18.
 */
class ReviewShopViewHolder(itemView: View, viewListener: ListenerReviewHolder, shopReviewHolderListener: ShopReviewHolderListener) : ReviewProductContentViewHolder(itemView, viewListener) {
    var shopReviewHolderListener: ShopReviewHolderListener
    private val productName: TextView
    private val productImage: ImageView
    override fun bind(element: ReviewProductModelContent) {
        super.bind(element)
        if (element is ReviewShopModelContent) {
            val shopReviewModelContent = element
            productName.text = shopReviewModelContent.productName
            productImage.loadImageRounded(shopReviewModelContent.productImageUrl, convertDpToPx(productImage.context, RADIUS_IMAGE.toFloat()))
            productName.setOnClickListener { goToProductDetail(shopReviewModelContent) }
            productImage.setOnClickListener { goToProductDetail(shopReviewModelContent) }
        }
    }

    fun convertDpToPx(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }

    private fun goToProductDetail(shopReviewModelContent: ReviewShopModelContent) {
        shopReviewHolderListener.onGoToDetailProduct(shopReviewModelContent.productId, adapterPosition)
    }

    interface ShopReviewHolderListener {
        fun onGoToDetailProduct(productUrl: String?, adapterPosition: Int)
    }

    companion object {
        val LAYOUT = R.layout.item_shop_review_shop_page
        const val RADIUS_IMAGE = 4
    }

    init {
        productName = itemView.findViewById(R.id.product_name)
        productImage = itemView.findViewById(R.id.product_image)
        this.shopReviewHolderListener = shopReviewHolderListener
    }
}