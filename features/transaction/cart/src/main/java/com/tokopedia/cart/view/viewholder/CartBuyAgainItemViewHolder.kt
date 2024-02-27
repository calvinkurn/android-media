package com.tokopedia.cart.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemProductBuyAgainBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartBuyAgainItemHolderData
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.unifycomponents.UnifyButton

class CartBuyAgainItemViewHolder(
    private val binding: ItemProductBuyAgainBinding,
    private val listener: ActionListener?
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val LAYOUT = R.layout.item_product_buy_again
    }

    fun bind(element: CartBuyAgainItemHolderData) {
        binding.productCardView.apply {
            setProductModel(
                ProductCardModel(
                    slashedPrice = element.slashedPrice,
                    productName = element.name,
                    formattedPrice = element.price,
                    productImageUrl = element.imageUrl,
                    isTopAds = element.isTopAds,
                    discountPercentage = element.discountPercentage,
                    reviewCount = element.reviewCount,
                    ratingCount = element.rating,
                    shopLocation = element.shopLocation,
                    shopBadgeList = element.badgesUrl.map {
                        ProductCardModel.ShopBadge(
                            imageUrl = it
                        )
                    },
                    freeOngkir = ProductCardModel.FreeOngkir(
                        isActive = element.isFreeOngkirActive,
                        imageUrl = element.freeOngkirImageUrl
                    ),
                    labelGroupList = element.labelGroupList.map { recommendationLabel ->
                        ProductCardModel.LabelGroup(
                            position = recommendationLabel.position,
                            title = recommendationLabel.title,
                            type = recommendationLabel.type,
                            imageUrl = recommendationLabel.imageUrl
                        )
                    },
                    hasAddToCartButton = true,
                    addToCartButtonType = UnifyButton.Type.MAIN
                )
            )
            setOnClickListener {
                listener?.onBuyAgainProductClicked(element)
            }
            setAddToCartOnClickListener {
                listener?.onBuyAgainButtonAddToCartClicked(element)
            }
        }
    }
}
