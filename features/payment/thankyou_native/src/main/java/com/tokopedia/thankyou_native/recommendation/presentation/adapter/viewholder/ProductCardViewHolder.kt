package com.tokopedia.thankyou_native.recommendation.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.recommendation.model.ThankYouProductCardModel
import com.tokopedia.thankyou_native.recommendation.presentation.adapter.listener.ProductCardViewListener

class ProductCardViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val productCardView = itemView.findViewById<ProductCardGridView>(R.id.productCardView)


    private lateinit var thankYouProductCardModel: ThankYouProductCardModel

    fun bind(thankYouProductCardModel: ThankYouProductCardModel,
             listener: ProductCardViewListener?) {
        thankYouProductCardModel.productCardModel?.let { productCardModel ->
            productCardView.run {
                applyCarousel()
                setProductModel(productCardModel)
                setImageProductViewHintListener(thankYouProductCardModel
                        .recommendationItem, object : ViewHintListener {
                    override fun onViewHint() {
                        listener?.onProductImpression(thankYouProductCardModel
                                .recommendationItem, adapterPosition)
                    }
                })

                setOnClickListener {
                    listener?.onProductClick(thankYouProductCardModel
                            .recommendationItem, null, adapterPosition)
                }
            }
        }

    }


    fun getThankYouRecommendationModel(): ThankYouProductCardModel? {
        if (::thankYouProductCardModel.isInitialized)
            return thankYouProductCardModel
        return null
    }


    companion object {
        val LAYOUT_ID = R.layout.thank_item_recommendation
    }
}


/*

    private fun getString(@StringRes stringRes: Int): String {
        return itemView.context.getString(stringRes)
    }

    fun clearImage() {
       */
/* itemView.productCardView.setImageProductVisible(false)*//*

    }
*/


/*this.marketPlaceRecommendationModel = marketPlaceRecommendationModel
itemView.tag = marketPlaceRecommendationModel
itemView.productCardView.apply {
    val recommendationItem = marketPlaceRecommendationModel.recommendationItem
    setProductModel(marketPlaceRecommendationModel.productCardModel, blankSpaceConfig)
    setImageProductViewHintListener(recommendationItem, object : ViewHintListener {
        override fun onViewHint() {
            listener?.onProductImpression(marketPlaceRecommendationModel.recommendationItem, adapterPosition)
        }
    })
    setAddToCartOnClickListener { listener?.onProductAddToCartClick(recommendationItem, adapterPosition) }
    setButtonWishlistOnClickListener {
        listener?.onWishlistClick(recommendationItem, recommendationItem.isWishlist) { success, throwable ->
            val activity = (context as Activity)
            if (!activity.isFinishing) {
                if (success) {
                    recommendationItem.isWishlist = !recommendationItem.isWishlist
                    setButtonWishlistImage(recommendationItem.isWishlist)
                    if (recommendationItem.isWishlist) {
                        listener.onWishListedSuccessfully(getString(R.string.msg_success_add_wishlist))
                    } else {
                        listener.onRemoveFromWishList(getString(R.string.msg_success_remove_wishlist))
                    }
                } else {
                    listener.onShowError(throwable)
                }
            }

        }
    }
    setOnClickListener { listener?.onProductClick(marketPlaceRecommendationModel.recommendationItem, position = *intArrayOf(adapterPosition)) }
    this.setAddToCartVisible(false)
}*/

