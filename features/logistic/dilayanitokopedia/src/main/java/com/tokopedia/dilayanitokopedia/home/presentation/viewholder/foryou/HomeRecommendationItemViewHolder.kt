package com.tokopedia.dilayanitokopedia.home.presentation.viewholder.foryou

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.dilayanitokopedia.R
import com.tokopedia.dilayanitokopedia.home.presentation.adapter.HomeRecommendationListener
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationItemDataModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener

class HomeRecommendationItemViewHolder(itemView: View, private val cardInteraction: Boolean = false) :
    SmartAbstractViewHolder<HomeRecommendationItemDataModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.dt_home_feed_item
        const val PM_PRO_TITLE = "Power Merchant Pro"
    }

    private val productCardView by lazy { itemView.findViewById<ProductCardGridView>(R.id.productCardView) }

    private fun setLayout(element: HomeRecommendationItemDataModel, listener: HomeRecommendationListener) {
        val productCardModelLabelGroupList = element.product.labelGroup.map {
            ProductCardModel.LabelGroup(position = it.position, type = it.type, title = it.title, imageUrl = it.url)
        }

        productCardView?.run {
            setProductModel(
                ProductCardModel(
                    slashedPrice = element.product.slashedPrice,
                    productName = element.product.name,
                    formattedPrice = element.product.price,
                    productImageUrl = element.product.imageUrl,
                    isTopAds = element.product.isTopads,
                    discountPercentage = if (element.product.discountPercentage > 0) "${element.product.discountPercentage}%" else "",
                    ratingCount = element.product.rating,
                    reviewCount = element.product.countReview,
                    countSoldRating = element.product.ratingAverage,
                    shopLocation = element.product.shop.city,
                    isWishlistVisible = true,
                    isWishlisted = element.product.isWishlist,
                    shopBadgeList = element.product.badges.map {
                        ProductCardModel.ShopBadge(imageUrl = it.imageUrl)
                    },
                    freeOngkir = ProductCardModel.FreeOngkir(
                        isActive = element.product.freeOngkir.isActive,
                        imageUrl = element.product.freeOngkir.imageUrl
                    ),
                    labelGroupList = productCardModelLabelGroupList,
                    hasThreeDots = true,
                    cardInteraction = cardInteraction
                )
            )
            setImageProductViewHintListener(
                element,
                object : ViewHintListener {
                    override fun onViewHint() {
                        listener.onProductImpression(element, adapterPosition)
                    }
                }
            )
            setOnClickListener { listener.onProductClick(element, adapterPosition) }

            setThreeDotsOnClickListener {
                listener.onProductThreeDotsClick(element, adapterPosition)
            }
        }
    }

    override fun bind(element: HomeRecommendationItemDataModel, listener: SmartListener) {
        setLayout(element, listener as HomeRecommendationListener)
    }

    override fun bind(element: HomeRecommendationItemDataModel, listener: SmartListener, payloads: List<Any>) {
        productCardView?.setThreeDotsOnClickListener {
            (listener as HomeRecommendationListener).onProductThreeDotsClick(element, adapterPosition)
        }
    }
}
