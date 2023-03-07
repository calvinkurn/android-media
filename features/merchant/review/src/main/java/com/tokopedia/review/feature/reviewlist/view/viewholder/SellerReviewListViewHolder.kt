package com.tokopedia.review.feature.reviewlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.common.util.roundDecimal
import com.tokopedia.review.databinding.ItemRatingProdukBinding
import com.tokopedia.review.feature.reviewlist.view.model.ProductReviewUiModel

open class SellerReviewListViewHolder(val view: View,
                                 private val sellerReviewListener: SellerReviewListListener): AbstractViewHolder<ProductReviewUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_rating_produk
        private const val RATING_STAR_YELLOW_VALUE = 3.5
    }

    private val binding = ItemRatingProdukBinding.bind(view)

    override fun bind(element: ProductReviewUiModel) {
        binding.itemRatingProduct.setBackgroundColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N0))

        bindProductImage(element.productImageUrl.orEmpty())
        binding.tgTitleProduct.text = element.productName

        binding.tgRatingCount.text = element.rating?.roundDecimal()
        binding.tgReviewCount.text = String.format(getString(R.string.period_seller_review), element.reviewCount.toString())

        element.rating?.roundDecimal()?.toFloatOrNull().let {
            if (it != null) {
                if(it >= RATING_STAR_YELLOW_VALUE) {
                    binding.ivRating.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_rating_star_yellow))
                } else {
                    binding.ivRating.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_rating_star_red))
                }
            }
        }

        binding.kejarUlasanLabel.showWithCondition(element.isKejarUlasan)

        itemView.setOnClickListener {
            sellerReviewListener.onItemProductReviewClicked(element.productID, adapterPosition, element.productImageUrl.orEmpty())
        }

        if(adapterPosition == 1) {
            sellerReviewListener.onAddedCoachMarkItemProduct(itemView)
        }
    }

    protected open fun bindProductImage(imageUrl: String) {
        binding.ivItemProduct.loadImage(imageUrl)
    }

    interface SellerReviewListListener {
        fun onItemProductReviewClicked(productId: String, position: Int, imageUrl: String)
        fun onAddedCoachMarkItemProduct(view: View)
    }
}
