package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener

/**
 * Created by Lukas on 2019-07-15
 */

class HomeRecommendationItemViewHolder(itemView: View) : SmartAbstractViewHolder<HomeRecommendationItemDataModel>(itemView) {

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.home_feed_item
        const val PM_PRO_TITLE = "Power Merchant Pro"
    }

    private val productCardView by lazy { itemView.findViewById<ProductCardGridView>(R.id.productCardView) }

    private fun setLayout(element: HomeRecommendationItemDataModel, listener: HomeRecommendationListener){
        val productCardModelLabelGroupList = element.product.labelGroup.map {
            ProductCardModel.LabelGroup(position = it.position, type = it.type, title = it.title, imageUrl = it.imageUrl)
        }

        productCardView?.run{
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
                            countSoldRating = element.product.ratingFloat,
                            shopLocation = element.product.shop.city,
                            isWishlistVisible = true,
                            isWishlisted = element.product.isWishlist,
                            shopBadgeList = element.product.badges.map {
                                ProductCardModel.ShopBadge(imageUrl = it.imageUrl)
                            },
                            freeOngkir = ProductCardModel.FreeOngkir(
                                    isActive = element.product.freeOngkirInformation.isActive,
                                    imageUrl = element.product.freeOngkirInformation.imageUrl
                            ),
                            labelGroupList = productCardModelLabelGroupList,
                            hasThreeDots = true
                    )
            )
            setImageProductViewHintListener(element, object: ViewHintListener {
                override fun onViewHint() {
                    //if pm pro
                    val isPmPro = element.product.badges.find { it.title == PM_PRO_TITLE } != null
                    if (isPmPro) {
                        listener.onProductWithPmProImpressed(productCardView.getShopBadgeView(), adapterPosition)
                    }
                    listener.onProductImpression(element, adapterPosition)
                }
            })
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
        if (payloads.getOrNull(0) !is Boolean) return

        productCardView?.setThreeDotsOnClickListener {
            (listener as HomeRecommendationListener).onProductThreeDotsClick(element, adapterPosition)
        }
    }
}

