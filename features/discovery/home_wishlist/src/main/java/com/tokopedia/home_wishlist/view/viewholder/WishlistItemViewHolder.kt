package com.tokopedia.home_wishlist.view.viewholder

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.model.datamodel.WishlistItemDataModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardView
import com.tokopedia.unifycomponents.Toaster

class WishlistItemViewHolder(
        private val view: View
) : AbstractViewHolder<WishlistItemDataModel>(view){

    private val productCardView: ProductCardView by lazy { view.findViewById<ProductCardView>(R.id.wishlist_item) }

    override fun bind(element: WishlistItemDataModel) {
        productCardView.run {
            setProductModel(
                    ProductCardModel(
                            productName = element.productItem.name,
                            formattedPrice = element.productItem.price,
                            productImageUrl = element.productItem.imageUrl,
                            shopName = element.productItem.shop.name,
                            shopLocation = element.productItem.shop.location,
                            isWishlistVisible = true,
                            isWishlisted = true,
                            shopBadgeList = element.productItem.badges.map {
                                ProductCardModel.ShopBadge(imageUrl = it.imageUrl)
                            },
                            freeOngkir = ProductCardModel.FreeOngkir(
                                    isActive = element.productItem.freeOngkir.isActive,
                                    imageUrl = element.productItem.freeOngkir.imageUrl
                            )
                    )
            )

//            setImageProductViewHintListener(element.productItem, object: ViewHintListener {
//                override fun onViewHint() {
//                    if(element.productItem.isTopAds){
//                        ImpresionTask().execute(element.productItem.trackerImageUrl)
//                    }
//                    element.listener.onProductImpression(element.productItem)
//                }
//            })
//
//            setOnClickListener {
//                element.listener.onProductClick(element.productItem, element.productItem.type, adapterPosition)
//                if (element.productItem.isTopAds) ImpresionTask().execute(element.productItem.clickUrl)
//            }
//
//            setButtonWishlistOnClickListener {
//                element.listener.onWishlistClick(element.productItem, !element.productItem.isWishlist){ success, throwable ->
//                    if(success){
//                        element.productItem.isWishlist = !element.productItem.isWishlist
//                        setButtonWishlistImage(element.productItem.isWishlist)
//                        if(element.productItem.isWishlist){
//                            showSuccessAddWishlist((context as Activity).findViewById(android.R.id.content), getString(R.string.msg_success_add_wishlist))
//                        } else {
//                            showSuccessRemoveWishlist((context as Activity).findViewById(android.R.id.content), getString(R.string.msg_success_remove_wishlist))
//                        }
//                    }else {
//                        showError(rootView, throwable)
//                    }
//                }
//            }
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
