package com.tokopedia.review.feature.reviewlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.review.R
import com.tokopedia.review.common.util.roundDecimal
import com.tokopedia.review.feature.reviewlist.view.model.ProductReviewUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_rating_produk.view.*

class SellerReviewListViewHolder(val view: View,
                                 private val sellerReviewListener: SellerReviewListListener): AbstractViewHolder<ProductReviewUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_rating_produk
        private const val RATING_STAR_YELLOW_VALUE = 3.5
    }

    private val ivItemProduct: ImageUnify = view.findViewById(R.id.ivItemProduct)
    private val tgTitleProduct: Typography = view.findViewById(R.id.tgTitleProduct)
    private val tgRatingCount: Typography = view.findViewById(R.id.tgRatingCount)
    private val tgReviewCount: Typography = view.findViewById(R.id.tgReviewCount)
    private val ivRating: AppCompatImageView = view.findViewById(R.id.ivRating)
    private val labelKejarUlasan: Label = view.findViewById(R.id.kejarUlasanLabel)

    override fun bind(element: ProductReviewUiModel) {
        view.itemRatingProduct.setBackgroundColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N0))

        ivItemProduct.loadImage(element.productImageUrl.orEmpty())
        tgTitleProduct.text = element.productName

        tgRatingCount.text = element.rating?.roundDecimal()
        tgReviewCount.text = String.format(getString(R.string.period_seller_review), element.reviewCount.toString())

        element.rating?.roundDecimal()?.toFloatOrNull().let {
            if (it != null) {
                if(it >= RATING_STAR_YELLOW_VALUE) {
                    ivRating.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_rating_star_yellow))
                } else {
                    ivRating.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_rating_star_red))
                }
            }
        }

        labelKejarUlasan.showWithCondition(element.isKejarUlasan)

        itemView.setOnClickListener {
            sellerReviewListener.onItemProductReviewClicked(element.productID.orZero(), adapterPosition, element.productImageUrl.orEmpty())
        }

        if(adapterPosition == 1) {
            sellerReviewListener.onAddedCoachMarkItemProduct(itemView)
        }
    }

    interface SellerReviewListListener {
        fun onItemProductReviewClicked(productId: Int, position: Int, imageUrl: String)
        fun onAddedCoachMarkItemProduct(view: View)
    }
}