package com.tokopedia.home_recom.view.viewholder

import android.app.Activity
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.model.datamodel.RecommendationCarouselItemDataModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardView
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.unifycomponents.Toaster

/**
 * Created by lukas on 21/05/2019
 *
 * A class for holder view Recommendation Carousel Item
 */
class RecommendationCarouselItemViewHolder (
        private val view: View
) : AbstractViewHolder<RecommendationCarouselItemDataModel>(view){

    private val productCardView: ProductCardView by lazy { view.findViewById<ProductCardView>(R.id.product_item) }

    override fun bind(element: RecommendationCarouselItemDataModel) {
        productCardView.run {
            setProductModel(
                    ProductCardModel(
                            slashedPrice = element.productItem.slashedPrice,
                            productName = element.productItem.name,
                            formattedPrice = element.productItem.price,
                            productImageUrl = element.productItem.imageUrl,
                            isTopAds = element.productItem.isTopAds,
                            discountPercentage = element.productItem.discountPercentage.toString(),
                            reviewCount = element.productItem.countReview,
                            ratingCount = element.productItem.rating,
                            shopLocation = element.productItem.location,
                            isWishlistVisible = true,
                            isWishlisted = element.productItem.isWishlist,
                            shopBadgeList = element.productItem.badgesUrl.map {
                                ProductCardModel.ShopBadge(imageUrl = it?:"")
                            },
                            freeOngkir = ProductCardModel.FreeOngkir(
                                    isActive = element.productItem.isFreeOngkirActive,
                                    imageUrl = element.productItem.freeOngkirImageUrl
                            )
                    ),
                    BlankSpaceConfig(
                            ratingCount = true,
                            discountPercentage = true
                    )
            )

            setImageProductViewHintListener(element.productItem, object: ViewHintListener{
                override fun onViewHint() {
                    if(element.productItem.isTopAds){
                        ImpresionTask().execute(element.productItem.trackerImageUrl)
                    }
                    element.listener.onProductImpression(element.productItem)
                }
            })

            setOnClickListener {
                element.listener.onProductClick(element.productItem, element.productItem.type, element.parentPosition, adapterPosition)
                if (element.productItem.isTopAds) {
                    ImpresionTask().execute(element.productItem.clickUrl)
                }
            }

            setButtonWishlistOnClickListener {
                element.listener.onWishlistClick(element.productItem, !element.productItem.isWishlist){ success, throwable ->
                    if(success){
                        element.productItem.isWishlist = !element.productItem.isWishlist
                        setButtonWishlistImage(element.productItem.isWishlist)
                        if(element.productItem.isWishlist){
                            showSuccessAddWishlist((context as Activity).findViewById(android.R.id.content), getString(R.string.msg_success_add_wishlist))
                        } else {
                            showSuccessRemoveWishlist((context as Activity).findViewById(android.R.id.content), getString(R.string.msg_success_remove_wishlist))
                        }
                    }else {
                        showError(rootView, throwable)
                    }
                }
            }
        }
    }

    private fun mapBadges(badges: List<String?>){
        for (badge in badges) {
            val view = LayoutInflater.from(productCardView.context).inflate(com.tokopedia.productcard.R.layout.layout_badge, null)
            ImageHandler.loadImageFitCenter(productCardView.context, view.findViewById(com.tokopedia.productcard.R.id.badge), badge)
            productCardView.addShopBadge(view)
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
        Toaster.showError(view, ErrorHandler.getErrorMessage(view.context, throwable), Snackbar.LENGTH_LONG)
    }

}