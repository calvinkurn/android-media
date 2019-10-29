package com.tokopedia.home_wishlist.view.viewholder

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.base.SmartAbstractViewHolder
import com.tokopedia.home_wishlist.base.SmartListener
import com.tokopedia.home_wishlist.model.datamodel.WishlistItemDataModel
import com.tokopedia.home_wishlist.view.custom.WishlistCardView
import com.tokopedia.home_wishlist.view.listener.WishlistListener
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.unifycomponents.Toaster
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
                            freeOngkir = ProductCardModel.FreeOngkir(
                                    isActive = element.productItem.freeOngkir.isActive,
                                    imageUrl = element.productItem.freeOngkir.imageUrl
                            )
                    )
            )

            productCardView.setAddToCardButtonVisible(true)
            productCardView.setDeleteButtonVisible(true)
            checkBox.isChecked = element.isChecked
            checkBox.setOnClickListener {
                element.isChecked = !element.isChecked
            }
            checkBox.visibility = if(element.isBulkMode) View.VISIBLE else View.GONE
            view.setOnClickListener {
                if(element.isBulkMode) {
                    element.isChecked = !element.isChecked
                    checkBox.isChecked = element.isChecked
                }
            }
            setOnClickListener {
                if(element.isBulkMode) {
                    element.isChecked = !element.isChecked
                    checkBox.isChecked = element.isChecked
                }
                else (listener as WishlistListener).onProductClick(element, adapterPosition)
//                if (element.productItem.isTopAds) ImpresionTask().execute(element.productItem.clickUrl)
            }

            setAddToCardButtonOnClickListener(View.OnClickListener {
                (listener as WishlistListener).onAddToCartClick(element)
            })

            setDeleteButtonOnClickListener(View.OnClickListener {
                (listener as WishlistListener).onDeleteClick(element)
            })
        }
    }

    private fun showSuccessAddWishlist(view: View, message: String){
        Toaster.showNormalWithAction(view, message, Snackbar.LENGTH_LONG,
                view.context.getString(R.string.recom_go_to_wishlist), View.OnClickListener {
            RouteManager.route(view.context, ApplinkConst.WISHLIST)
        })
    }

    private fun showSuccessRemoveWishlist(view: View, message: String){
        Toaster.showNormal(view, message, Snackbar.LENGTH_LONG)
    }

    private fun showError(view: View, throwable: Throwable?){
        Toaster.showError(view,
                ErrorHandler.getErrorMessage(view.context, throwable), Snackbar.LENGTH_LONG)
    }

}
