package com.tokopedia.thankyou_native.recommendation.presentation.adapter.viewholder

import android.app.Activity
import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.recommendation.presentation.adapter.listener.MarketPlaceRecommendationViewListener
import com.tokopedia.thankyou_native.recommendation.model.MarketPlaceRecommendationModel
import kotlinx.android.synthetic.main.thank_item_recommendation.view.*

class MarketPlaceRecommendationViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT_ID = R.layout.thank_item_recommendation
    }

    private lateinit var marketPlaceRecommendationModel: MarketPlaceRecommendationModel

    fun bind(marketPlaceRecommendationModel: MarketPlaceRecommendationModel, blankSpaceConfig: BlankSpaceConfig,
             listener: MarketPlaceRecommendationViewListener?) {
        this.marketPlaceRecommendationModel = marketPlaceRecommendationModel
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
        }
    }


    fun getThankYouRecommendationModel(): MarketPlaceRecommendationModel? {
        if (::marketPlaceRecommendationModel.isInitialized)
            return marketPlaceRecommendationModel
        return null
    }

    private fun getString(@StringRes stringRes: Int): String {
        return itemView.context.getString(stringRes)
    }

    fun clearImage() {
        itemView.productCardView.setImageProductVisible(false)
    }
}

