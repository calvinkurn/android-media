package com.tokopedia.buyerorder.list.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.detail.view.OrderListAnalytics
import com.tokopedia.buyerorder.list.view.adapter.WishListResponseListener
import com.tokopedia.buyerorder.list.view.adapter.viewmodel.OrderListRecomUiModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter

class OrderListRecomListViewHolder(itemView: View?, var orderListAnalytics: OrderListAnalytics, private val actionListener: ActionListener?) : AbstractViewHolder<OrderListRecomUiModel>(itemView), WishListResponseListener {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.bom_item_recomnedation
        private const val className: String = "com.tokopedia.buyerorder.list.view.adapter.viewholder.OrderListRecomListViewHolder"
    }
    private var recommendationCardView = itemView?.findViewById<ProductCardGridView>(R.id.bomRecommendationCardView)
    private val wishlist = recommendationCardView?.findViewById<ImageView>(com.tokopedia.productcard.R.id.btn_wishlist)
    private var recomTitle : String = "none/other"
    private var isSelected: Boolean = false

    override fun bind(element: OrderListRecomUiModel) {
        recommendationCardView?.setProductModel(element.recommendationItem.toProductCardModel(hasAddToCartButton = true))
        recommendationCardView?.addOnImpressionListener(element.recommendationItem){
            if(element.recommendationItem.isTopAds){
                TopAdsUrlHitter(itemView.context).hitImpressionUrl(
                        className,
                        element.recommendationItem.trackerImageUrl,
                        element.recommendationItem.productId.toString(),
                        element.recommendationItem.name,
                        element.recommendationItem.imageUrl
                )
                orderListAnalytics.eventRecommendationProductImpression(element.recommendationItem, element.recommendationItem.position, recomTitle)
            } else {
                orderListAnalytics.eventRecommendationProductImpression(element.recommendationItem, element.recommendationItem.position, recomTitle)
            }
        }

        recommendationCardView?.setOnClickListener {
            if(element.recommendationItem.isTopAds){
                TopAdsUrlHitter(itemView.context).hitClickUrl(
                        className,
                        element.recommendationItem.clickUrl,
                        element.recommendationItem.productId.toString(),
                        element.recommendationItem.name,
                        element.recommendationItem.imageUrl
                )
                orderListAnalytics.eventEmptyBOMRecommendationProductClick(element.recommendationItem, element.recommendationItem.position, recomTitle)
            } else {
                orderListAnalytics.eventEmptyBOMRecommendationProductClick(element.recommendationItem, element.recommendationItem.position, recomTitle)
            }
        }
        recommendationCardView?.setAddToCartOnClickListener {
            actionListener?.onCartClicked(element)
        }
        isSelected = element.recommendationItem.isWishlist
        wishlist?.show()
        setWishlistDrawable()
        wishlist?.setOnClickListener {
            actionListener?.onWishListClicked(element, !isSelected, this)
        }
        recomTitle = element.recomTitle
    }

    private fun setWishlistDrawable() {
        if(isSelected){
            wishlist?.setImageDrawable(MethodChecker.getDrawable(itemView.context, com.tokopedia.productcard.R.drawable.product_card_ic_wishlist_red))
        } else {
            wishlist?.setImageDrawable(MethodChecker.getDrawable(itemView.context, com.tokopedia.productcard.R.drawable.product_card_ic_wishlist))
        }
    }

    override fun onWhishListSuccessResponse(isSelected: Boolean) {
        this.isSelected = isSelected
        setWishlistDrawable()
        orderListAnalytics.sendWishListClickEvent(isSelected)
    }

    interface ActionListener{
        fun onCartClicked(productModel: Any)
        fun onWishListClicked(productModel: Any, isSelected: Boolean, onWishListResponseListener: WishListResponseListener)
    }
}