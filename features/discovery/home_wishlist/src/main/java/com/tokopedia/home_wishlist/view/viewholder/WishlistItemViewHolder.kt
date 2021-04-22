package com.tokopedia.home_wishlist.view.viewholder

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.model.datamodel.WishlistItemDataModel
import com.tokopedia.home_wishlist.view.ext.setSafeOnClickListener
import com.tokopedia.home_wishlist.view.listener.WishlistListener
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener

class WishlistItemViewHolder(
        private val view: View
) : SmartAbstractViewHolder<WishlistItemDataModel>(view){

    private val parentPositionDefault: Int = -1

    private val productCardView: ProductCardListView by lazy { view.findViewById<ProductCardListView>(R.id.wishlist_item) }
    private val checkBox: CheckBox by lazy { view.findViewById<CheckBox>(R.id.wishlist_checkbox) }

    override fun bind(element: WishlistItemDataModel, listener: SmartListener) {
        productCardView.run {
            val productFreeOngkir = when {
                element.productItem.freeOngkirExtra.isActive -> element.productItem.freeOngkirExtra
                else -> element.productItem.freeOngkir
            }
            setProductModel(
                    ProductCardModel(
                            productName = element.productItem.name,
                            discountPercentage = if (element.productItem.discountPercentage > 0) "${element.productItem.discountPercentage}%" else "",
                            slashedPrice = element.productItem.slashPrice,
                            formattedPrice = element.productItem.price,
                            productImageUrl = element.productItem.imageUrl,
                            shopLocation = element.productItem.shop.location,
                            shopBadgeList = element.productItem.badges.map {
                                ProductCardModel.ShopBadge(imageUrl = it.imageUrl)
                            },
                            ratingCount = element.productItem.rating,
                            reviewCount = element.productItem.reviewCount,
                            freeOngkir = ProductCardModel.FreeOngkir(
                                    isActive = productFreeOngkir.isActive,
                                    imageUrl = productFreeOngkir.imageUrl
                            ),
                            labelGroupList = element.productItem.labels.map { labelGroup ->
                                ProductCardModel.LabelGroup(
                                        position = labelGroup.position,
                                        title = labelGroup.title,
                                        type = labelGroup.type
                                )
                            },
                            hasAddToCartButton = !element.isOnBulkRemoveProgress,
                            hasRemoveFromWishlistButton = !element.isOnBulkRemoveProgress
                    )
            )
            setImageProductViewHintListener(element, object: ViewHintListener {
                override fun onViewHint() {
                    (listener as WishlistListener).onProductImpression(element, adapterPosition)
                }
            })
            if(!element.productItem.available){
                wishlistPage_setOutOfStock()
            } else {
                if(element.isOnAddToCartProgress) wishlistPage_disableButtonAddToCart()
                else wishlistPage_enableButtonAddToCart()
            }
            checkBox.isChecked = element.isOnChecked
            checkBox.visibility = if(element.isOnBulkRemoveProgress) View.VISIBLE else View.GONE
            checkBox.setOnClickListener {
                (listener as WishlistListener).onClickCheckboxDeleteWishlist(adapterPosition, checkBox.isChecked)
            }

            view.setSafeOnClickListener {
                if(element.isOnBulkRemoveProgress) {
                    (listener as WishlistListener).onClickCheckboxDeleteWishlist(adapterPosition, !checkBox.isChecked)
                }
            }

            setOnClickListener {
                if(element.isOnBulkRemoveProgress) (listener as WishlistListener).onClickCheckboxDeleteWishlist(adapterPosition, !checkBox.isChecked)
                else (listener as WishlistListener).onProductClick(element, parentPositionDefault, adapterPosition)
            }

            setAddToCartOnClickListener {
                (listener as WishlistListener).onAddToCartClick(element, adapterPosition)
            }

            setRemoveWishlistOnClickListener {
                (listener as WishlistListener).onDeleteClick(element, adapterPosition)
            }
        }
    }

    override fun bind(element: WishlistItemDataModel, listener: SmartListener, payloads: List<Any>) {
        if(payloads.isNotEmpty()){
            val bundle = payloads[0] as Bundle
            if(bundle.containsKey("isOnChecked")){
                checkBox.isChecked = bundle.getBoolean("isOnChecked")
            }
            if(bundle.containsKey("isOnAddToCartProgress")){
                element.isOnAddToCartProgress = bundle.getBoolean("isOnAddToCartProgress")
                if(bundle.getBoolean("isOnAddToCartProgress")){
                    productCardView.wishlistPage_disableButtonAddToCart()
                } else {
                    productCardView.wishlistPage_enableButtonAddToCart()
                }

            }
            if(bundle.containsKey("isOnBulkRemoveProgress")){
                element.isOnBulkRemoveProgress = bundle.getBoolean("isOnBulkRemoveProgress")
                checkBox.visibility = if(bundle.getBoolean("isOnBulkRemoveProgress")) View.VISIBLE else View.GONE
                productCardView.wishlistPage_hideCTAButton(!element.isOnBulkRemoveProgress)
                productCardView.setOnClickListener {
                    if(element.isOnBulkRemoveProgress) (listener as WishlistListener).onClickCheckboxDeleteWishlist(adapterPosition, !checkBox.isChecked)
                    else (listener as WishlistListener).onProductClick(element, parentPositionDefault, adapterPosition)
                }
            }

        }
    }
}
