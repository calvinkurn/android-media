package com.tokopedia.cart.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartRecentViewItemHolderData
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.item_product_recent_view.view.*

/**
 * Created by Irfan Khoirul on 2019-06-15.
 */

class CartRecentViewItemViewHolder(val view: View, val actionListener: ActionListener?) : RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_product_recent_view
    }

    internal var isTopAds = false

    fun bind(element: CartRecentViewItemHolderData) {
        itemView.productCardView?.apply {
            setProductModel(
                    ProductCardModel(
                            slashedPrice = element.slashedPrice,
                            productName = element.name,
                            formattedPrice = element.price,
                            productImageUrl = element.imageUrl,
                            isTopAds = element.isTopAds,
                            discountPercentage = element.discountPercentage.toString(),
                            reviewCount = element.reviewCount,
                            ratingCount = element.rating,
                            shopLocation = element.shopLocation,
                            shopBadgeList = element.badgesUrl.map {
                                ProductCardModel.ShopBadge(imageUrl = it
                                        ?: "")
                            },
                            freeOngkir = ProductCardModel.FreeOngkir(
                                    isActive = element.isFreeOngkirActive,
                                    imageUrl = element.freeOngkirImageUrl
                            ),
                            labelGroupList = element.labelGroupList.map { recommendationLabel ->
                                ProductCardModel.LabelGroup(
                                        position = recommendationLabel.position,
                                        title = recommendationLabel.title,
                                        type = recommendationLabel.type
                                )
                            },
                            hasAddToCartButton = true,
                            addToCartButtonType = UnifyButton.Type.MAIN
                    )
            )
            setImageProductViewHintListener(element, object : ViewHintListener {
                override fun onViewHint() {
                    actionListener?.onRecentViewProductImpression(element)
                }
            })

            setOnClickListener {
                actionListener?.onRecentViewProductClicked(element.id)
            }
            setAddToCartOnClickListener {
                actionListener?.onButtonAddToCartClicked(element)
            }
        }

        isTopAds = element.isTopAds
    }
}