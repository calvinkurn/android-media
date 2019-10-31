package com.tokopedia.home_wishlist.view.viewholder

import android.view.View
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.base.SmartAbstractViewHolder
import com.tokopedia.home_wishlist.base.SmartListener
import com.tokopedia.home_wishlist.model.datamodel.WishlistItemDataModel
import com.tokopedia.home_wishlist.view.custom.WishlistCardView
import com.tokopedia.home_wishlist.view.listener.WishlistListener
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

class WishlistItemViewHolder(
        private val view: View
) : SmartAbstractViewHolder<WishlistItemDataModel>(view){

    private val productCardView: WishlistCardView by lazy { view.findViewById<WishlistCardView>(R.id.wishlist_item) }
    private val checkBox: CheckboxUnify by lazy { view.findViewById<CheckboxUnify>(R.id.wishlist_checkbox) }

    override fun bind(element: WishlistItemDataModel, listener: SmartListener) {
        productCardView.run {
            setProductModel(
                    ProductCardModel(
                            productName = element.productItem.name,
                            formattedPrice = element.productItem.price,
                            productImageUrl = element.productItem.imageUrl,
                            shopLocation = element.productItem.shop.location,
                            shopBadgeList = element.productItem.badges.map {
                                ProductCardModel.ShopBadge(imageUrl = it.imageUrl)
                            },
                            ratingCount = element.productItem.rating,
                            reviewCount = element.productItem.reviewCount,
                            freeOngkir = ProductCardModel.FreeOngkir(
                                    isActive = element.productItem.freeOngkir.isActive,
                                    imageUrl = element.productItem.freeOngkir.imageUrl
                            )
                    )
            )
            productCardView.setAddToCartButtonVisible(true)
            productCardView.setDeleteButtonVisible(true)

            if(!element.productItem.available){
                setOutOfStock()
            } else {
                if(element.isOnAddToCartProgress) disableAddToCartButton()
                else enableAddToCartButton()
            }

            checkBox.setOnClickListener {
                (listener as WishlistListener).onClickCheckboxDeleteWishlist(adapterPosition, checkBox.isChecked)
            }
            checkBox.visibility = if(element.isOnBulkRemoveProgress) View.VISIBLE else View.GONE

            view.setOnClickListener {
                if(element.isOnBulkRemoveProgress) {
                    (listener as WishlistListener).onClickCheckboxDeleteWishlist(adapterPosition, checkBox.isChecked)
                }
            }

            setOnClickListener {
                if(element.isOnBulkRemoveProgress) {
                    (listener as WishlistListener).onClickCheckboxDeleteWishlist(adapterPosition, checkBox.isChecked)
                }
                else (listener as WishlistListener).onProductClick(element, adapterPosition)
            }

            setAddToCartButtonOnClickListener(View.OnClickListener {
                (listener as WishlistListener).onAddToCartClick(element, adapterPosition)
            })

            setDeleteButtonOnClickListener(View.OnClickListener {
                (listener as WishlistListener).onDeleteClick(element, adapterPosition)
            })
        }
    }
}
