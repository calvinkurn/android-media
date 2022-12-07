package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationProductDataModel
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.productcard.v2.ProductCardView
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener

class AddToCartDoneRecommendationProductViewHolder(
        itemView: View,
        val recommendationListener: RecommendationListener
) : AbstractViewHolder<AddToCartDoneRecommendationProductDataModel>(itemView) {

    companion object {
        val LAYOUT_RES = R.layout.item_product_recommendation_add_to_cart
    }

    private val productCardView: ProductCardView by lazy { itemView.findViewById<ProductCardView>(R.id.productCardView) }

    override fun bind(element: AddToCartDoneRecommendationProductDataModel) {

        productCardView.run {
            setProductModel(
                    element.recommendationItem.toProductCardModel(),
                    BlankSpaceConfig(
                            ratingCount = true,
                            discountPercentage = true,
                            twoLinesProductName = true
                    )
            )
            setImageProductViewHintListener(element.recommendationItem, object : ViewHintListener {
                override fun onViewHint() {
                    recommendationListener.onProductImpression(element.recommendationItem)
                }
            })
            setOnClickListener {
                recommendationListener.onProductClick(
                        element.recommendationItem,
                        null,
                        element.parentAdapterPosition,
                        adapterPosition
                )
            }
            setButtonWishlistOnClickListener {
                recommendationListener.onWishlistV2Click(element.recommendationItem, !element.recommendationItem.isWishlist)
            }
        }
    }

}