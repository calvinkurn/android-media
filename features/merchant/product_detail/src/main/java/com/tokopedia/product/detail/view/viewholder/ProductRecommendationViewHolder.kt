package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.utils.ImpresionTask
import kotlinx.android.synthetic.main.partial_product_recom_1.view.*

class ProductRecommendationViewHolder(private val view: View,
                                      private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductRecommendationDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.partial_product_recom_1
    }

    override fun bind(element: ProductRecommendationDataModel) {
        element.recomWidgetData?.run {
            view.title_recom_1.text = title
            if(seeMoreAppLink.isNotEmpty()) {
                view.see_more_recom_1.show()
                view.see_more_recom_1.setOnClickListener {
//                    productDetailTracking.eventClickSeeMoreRecomWidget(pageName)
//                    RouteManager.route(context, product.seeMoreAppLink)
                }
            }
            initAdapter(this)
        }
    }

    private fun initAdapter(product: RecommendationWidget) {
//        val pageName = when (getView()) {
//            getView().base_recom_1 -> ProductTrackingConstant.PageNameRecommendation.PDP_1
//            getView().base_recom_2 -> ProductTrackingConstant.PageNameRecommendation.PDP_2
//            getView().base_recom_3 -> ProductTrackingConstant.PageNameRecommendation.PDP_3
//            getView().base_recom_4 -> ProductTrackingConstant.PageNameRecommendation.PDP_4
//            else -> ""
//        }
        view.product_recom_1.initCarouselProductCardView(
                parentView = view,
                isScrollable = true,
                carouselProductCardOnItemClickListener = object : CarouselProductCardListener.OnItemClickListener {
                    override fun onItemClick(productCardModel: ProductCardModel, adapterPosition: Int) {
                        val productRecommendation = product.recommendationItemList[adapterPosition]
                        val topAdsClickUrl = productRecommendation.clickUrl
                        if (productCardModel.isTopAds) {
                            ImpresionTask().execute(topAdsClickUrl)
                        }
//                        productDetailTracking.eventRecommendationClick(
//                                productRecommendation,
//                                adapterPosition,
//                                getListener().isUserSessionActive,
//                                pageName,
//                                product.title)
                        view.context?.run {
                            RouteManager.route(this,
                                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                                    productRecommendation.productId.toString())
                        }
                    }
                },
                carouselProductCardOnItemImpressedListener = object : CarouselProductCardListener.OnItemImpressedListener {
                    override fun getImpressHolder(adapterPosition: Int): ImpressHolder {
                        return product.recommendationItemList[adapterPosition]
                    }

                    override fun onItemImpressed(productCardModel: ProductCardModel, adapterPosition: Int) {
                        val productRecommendation = product.recommendationItemList[adapterPosition]
                        val topAdsImageUrl = productRecommendation.trackerImageUrl
                        if (productCardModel.isTopAds) {
                            ImpresionTask().execute(topAdsImageUrl)
                        }
//                        productDetailTracking.eventRecommendationImpression(
//                                adapterPosition,
//                                productRecommendation,
//                                getListener().isUserSessionActive,
//                                pageName,
//                                product.title)
                    }
                },
                productCardModelList = product.recommendationItemList.map {
                    ProductCardModel(
                            slashedPrice = it.slashedPrice,
                            productName = it.name,
                            formattedPrice = it.price,
                            productImageUrl = it.imageUrl,
                            isTopAds = it.isTopAds,
                            discountPercentage = it.discountPercentage.toString(),
                            reviewCount = it.countReview,
                            ratingCount = it.rating,
                            shopLocation = it.location,
                            isWishlistVisible = false,
                            isWishlisted = it.isWishlist,
                            shopBadgeList = it.badgesUrl.map {
                                ProductCardModel.ShopBadge(imageUrl = it?:"")
                            },
                            freeOngkir = ProductCardModel.FreeOngkir(
                                    isActive = it.isFreeOngkirActive,
                                    imageUrl = it.freeOngkirImageUrl
                            ),
                            labelPromo = ProductCardModel.Label(
                                    title = it.labelPromo.title,
                                    type = it.labelPromo.type
                            ),
                            labelCredibility = ProductCardModel.Label(
                                    title = it.labelCredibility.title,
                                    type = it.labelCredibility.type
                            ),
                            labelOffers = ProductCardModel.Label(
                                    title = it.labelOffers.title,
                                    type = it.labelOffers.type
                            )
                    )
                }

        )
    }
}