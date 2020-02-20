package com.tokopedia.reviewseller.feature.reviewlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.ProductReviewModel
import com.tokopedia.unifyprinciples.Typography

class SellerReviewListViewHolder(itemView: View): AbstractViewHolder<ProductReviewModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_rating_produk
    }

    private val ivItemProduct: AppCompatImageView = itemView.findViewById(R.id.ivItemProduct)
    private val tgTitleProduct: Typography = itemView.findViewById(R.id.tgTitleProduct)
    private val tgCountRating: Typography = itemView.findViewById(R.id.tgCountRating)
    private val tgCountReview: Typography = itemView.findViewById(R.id.tgCountReview)

    override fun bind(element: ProductReviewModel?) {
        ImageHandler.LoadImage(ivItemProduct, element?.image)
        tgTitleProduct.text = element?.title
        tgCountRating.text = element?.rating
        tgCountReview.text = element?.review
    }
}