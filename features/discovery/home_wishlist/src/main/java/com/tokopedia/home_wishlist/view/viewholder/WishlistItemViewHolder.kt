package com.tokopedia.home_wishlist.view.viewholder

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.model.datamodel.WishlistItemDataModel
import com.tokopedia.home_wishlist.view.custom.WishlistCardView
import com.tokopedia.home_wishlist.view.ext.setSafeOnClickListener
import com.tokopedia.home_wishlist.view.listener.WishlistListener
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener

class WishlistItemViewHolder(
        private val view: View
) : SmartAbstractViewHolder<WishlistItemDataModel>(view){

    private val productCardView: WishlistCardView by lazy { view.findViewById<WishlistCardView>(R.id.wishlist_item) }
    private val checkBox: CheckBox by lazy { view.findViewById<CheckBox>(R.id.wishlist_checkbox) }

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
            productCardView.setAddToCartButtonVisible(!element.isOnBulkRemoveProgress)
            productCardView.setDeleteButtonVisible(!element.isOnBulkRemoveProgress)
            setImageProductViewHintListener(element, object: ViewHintListener {
                override fun onViewHint() {
//                    if(element.productItem.isTopAds){
//                        ImpresionTask().getData(element.productItem.trackerImageUrl)
//                    }
                    (listener as WishlistListener).onProductImpression(element, adapterPosition)
                }
            })
            if(!element.productItem.available){
                setOutOfStock()
            } else {
                if(element.isOnAddToCartProgress) disableAddToCartButton()
                else enableAddToCartButton()
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
                else (listener as WishlistListener).onProductClick(element, adapterPosition)
            }

            setAddToCartButtonOnClickListener {
                (listener as WishlistListener).onAddToCartClick(element, adapterPosition)
            }

            setDeleteButtonOnClickListener {
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
                    productCardView.disableAddToCartButton()
                } else {
                    productCardView.enableAddToCartButton()
                }

            }
            if(bundle.containsKey("isOnBulkRemoveProgress")){
                element.isOnBulkRemoveProgress = bundle.getBoolean("isOnBulkRemoveProgress")
                checkBox.visibility = if(bundle.getBoolean("isOnBulkRemoveProgress")) View.VISIBLE else View.GONE
            }

        }
    }
}
