package com.tokopedia.reviewseller.feature.reviewlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.util.roundDecimal
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductReviewUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class SellerReviewListViewHolder(itemView: View,
                                 private val listenerSellerReview: SellerReviewListListener): AbstractViewHolder<ProductReviewUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_rating_produk
    }

    private val ivItemProduct: ImageUnify = itemView.findViewById(R.id.ivItemProduct)
    private val tgTitleProduct: Typography = itemView.findViewById(R.id.tgTitleProduct)
    private val tgRatingCount: Typography = itemView.findViewById(R.id.tgRatingCount)
    private val tgReviewCount: Typography = itemView.findViewById(R.id.tgReviewCount)

    override fun bind(element: ProductReviewUiModel) {
        ivItemProduct.setImageUrl(element.productImageUrl.orEmpty())
        tgTitleProduct.text = element.productName

        tgRatingCount.text = element.rating?.roundDecimal()
        tgReviewCount.text = String.format(getString(R.string.period_seller_review), element.reviewCount.toString())

        itemView.setOnClickListener {
            listenerSellerReview.onItemProductReviewClicked(element.productID.orZero(), adapterPosition)
        }
    }

    interface SellerReviewListListener {
        fun onItemProductReviewClicked(productId: Int, position: Int)
    }
}