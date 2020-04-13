package com.tokopedia.reviewseller.feature.reviewlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductReviewUiModel
import com.tokopedia.unifyprinciples.Typography

class SellerReviewListViewHolder(itemView: View): AbstractViewHolder<ProductReviewUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_rating_produk
    }

    private val ivItemProduct: AppCompatImageView = itemView.findViewById(R.id.ivItemProduct)
    private val tgTitleProduct: Typography = itemView.findViewById(R.id.tgTitleProduct)
    private val tgRatingCount: Typography = itemView.findViewById(R.id.tgRatingCount)
    private val tgReviewCount: Typography = itemView.findViewById(R.id.tgReviewCount)

    override fun bind(element: ProductReviewUiModel?) {
        ImageHandler.LoadImage(ivItemProduct, element?.productImageUrl)
        tgTitleProduct.text = element?.productName
        tgRatingCount.text = element?.rating?.toString()
        tgReviewCount.text = String.format(getString(R.string.period_seller_review), element?.reviewCount.toString())
    }
}