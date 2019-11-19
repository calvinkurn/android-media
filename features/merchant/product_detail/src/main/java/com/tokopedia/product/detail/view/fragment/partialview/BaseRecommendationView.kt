package com.tokopedia.product.detail.view.fragment.partialview

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.carouselproductcard.CarouselProductCardView
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.util.ProductDetailTracking
import com.tokopedia.product.detail.data.util.ProductTrackingConstant.PageNameRecommendation.PDP_1
import com.tokopedia.product.detail.data.util.ProductTrackingConstant.PageNameRecommendation.PDP_2
import com.tokopedia.product.detail.data.util.ProductTrackingConstant.PageNameRecommendation.PDP_3
import com.tokopedia.product.detail.data.util.ProductTrackingConstant.PageNameRecommendation.PDP_4
import com.tokopedia.product.detail.view.adapter.RecommendationProductAdapter
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.partial_product_recom_1.view.*
import kotlinx.android.synthetic.main.partial_product_recom_2.view.*
import kotlinx.android.synthetic.main.partial_product_recom_3.view.*
import kotlinx.android.synthetic.main.partial_product_recom_4.view.*

abstract class BaseRecommendationView(context: Context,
                                      private val productDetailTracking: ProductDetailTracking,
                                      private val activity: Activity?) : View(context) {

    fun hideView() {
        getView().gone()
    }

    fun renderData(product: RecommendationWidget?) {
        with(getView()) {
            if (product == null ) {
                gone()
            } else if (product.recommendationItemList.isEmpty()){
                gone()
            } else {
                getLayoutTitle().text = product.title
                if(product.seeMoreAppLink.isNotEmpty()) {
                    getSeeMore().show()
                    getSeeMore().setOnClickListener {
                        productDetailTracking.eventClickSeeMoreRecomWidget(product.pageName)
                        RouteManager.route(context, product.seeMoreAppLink)
                    }
                }
                initAdapter(product)
                visible()
            }
        }
    }

    private fun initAdapter(product: RecommendationWidget) {
        val pageName = when (getView()) {
            getView().base_recom_1 -> PDP_1
            getView().base_recom_2 -> PDP_2
            getView().base_recom_3 -> PDP_3
            getView().base_recom_4 -> PDP_4
            else -> ""
        }
        getRecyclerView().initCarouselProductCardView(
                activity = activity,
                parentView = getView(),
                isScrollable = true,
                carouselProductCardOnItemClickListener = object : CarouselProductCardListener.OnItemClickListener {
                    override fun onItemClick(productCardModel: ProductCardModel, adapterPosition: Int) {
                        val productRecommendation = product.recommendationItemList[adapterPosition]
                        val topAdsClickUrl = productRecommendation.clickUrl
                        if (productCardModel.isTopAds) {
                            ImpresionTask().execute(topAdsClickUrl)
                        }
                        productDetailTracking.eventRecommendationClick(
                                productRecommendation,
                                adapterPosition,
                                getListener().isUserSessionActive,
                                pageName,
                                product.title)
                        context?.run {
                            RouteManager.route(context,
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
                        productDetailTracking.eventRecommendationImpression(
                                adapterPosition,
                                productRecommendation,
                                getListener().isUserSessionActive,
                                pageName,
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
        getRecyclerView().visible()
        getLayoutProgress().gone()
    }

    fun startLoading() {
        with(getView()) {
            visible()
            getLayoutProgress().visible()
            getRecyclerView().gone()
        }
    }

    abstract fun getListener(): RecommendationProductAdapter.UserActiveListener
    abstract fun getLayoutTitle(): TextView
    abstract fun getSeeMore(): Typography
    abstract fun getView(): View
    abstract fun getRecyclerView(): CarouselProductCardView
    abstract fun getLayoutProgress(): View
}