package com.tokopedia.home_wishlist.view.viewholder

import com.google.android.material.snackbar.Snackbar
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselItemDataModel
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
                            slashedPrice = element.recommendationItem.slashedPrice,
                            productName = element.recommendationItem.name,
                            formattedPrice = element.recommendationItem.price,
                            productImageUrl = element.recommendationItem.imageUrl,
                            isTopAds = element.recommendationItem.isTopAds,
                            discountPercentage = element.recommendationItem.discountPercentage.toString(),
                            reviewCount = element.recommendationItem.countReview,
                            ratingCount = element.recommendationItem.rating,
                            shopLocation = element.recommendationItem.location,
                            isWishlistVisible = true,
                            isWishlisted = element.recommendationItem.isWishlist,
                            shopBadgeList = element.recommendationItem.badgesUrl.map {
                                ProductCardModel.ShopBadge(imageUrl = it?:"")
                            },
                            freeOngkir = ProductCardModel.FreeOngkir(
                                    isActive = element.recommendationItem.isFreeOngkirActive,
                                    imageUrl = element.recommendationItem.freeOngkirImageUrl
                            )
                    ),
                    BlankSpaceConfig(
                            ratingCount = true,
                            discountPercentage = true,
                            twoLinesProductName = true
                    )
            )

            setImageProductViewHintListener(element.recommendationItem, object: ViewHintListener{
                override fun onViewHint() {
                    if(element.recommendationItem.isTopAds){
                        ImpresionTask().execute(element.recommendationItem.trackerImageUrl)
                    }

                }
            })

            setOnClickListener {

                if (element.recommendationItem.isTopAds) {
                    ImpresionTask().execute(element.recommendationItem.clickUrl)
                }
            }

            setButtonWishlistOnClickListener {

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


    companion object{
        val LAYOUT = R.layout.layout_recommendation_carousel_item
    }

}