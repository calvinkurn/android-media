package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
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
        view.product_recom_1.gone()
        view.visible()
        view.loading_recom_1.visible()
        element.recomWidgetData?.run {
            view.loading_recom_1.gone()
            view.title_recom_1.text = title
            view.product_recom_1.show()
            if (seeMoreAppLink.isNotEmpty()) {
                view.see_more_recom_1.show()
                view.see_more_recom_1.setOnClickListener {
                    listener.onSeeAllRecomClicked(pageName, seeMoreAppLink)
                }
            }
            initAdapter(this)
        }
    }

    private fun initAdapter(product: RecommendationWidget) {
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

                        listener.eventRecommendationClick(productRecommendation, adapterPosition, product.pageName, product.title)

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

                        listener.eventRecommendationImpression(productRecommendation,
                                adapterPosition,
                                product.pageName,
                                product.title)
                    }
                },
                productCardModelList = product.recommendationItemList.map {
                    ProductCardModel(
                            slashedPrice = it.slashedPrice,
                            productName = it.name,
                            formattedPrice = it.price,
                            productImageUrl = it.imageUrl,
                            isTopAds = it.isTopAds,
                            discountPercentage = it.discountPercentage,
                            reviewCount = it.countReview,
                            ratingCount = it.rating,
                            shopLocation = it.location,
                            isWishlistVisible = false,
                            isWishlisted = it.isWishlist,
                            shopBadgeList = it.badgesUrl.map {
                                ProductCardModel.ShopBadge(imageUrl = it ?: "")
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
                })
    }

    override fun bind(element: ProductRecommendationDataModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty()) {
            return
        }
    }
}