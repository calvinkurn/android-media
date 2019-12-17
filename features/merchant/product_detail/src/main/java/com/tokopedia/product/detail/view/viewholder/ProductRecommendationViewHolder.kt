package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.carouselproductcard.common.CarouselProductPool
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.utils.ImpresionTask
import kotlinx.android.synthetic.main.item_dynamic_recommendation.view.*

class ProductRecommendationViewHolder(private val view: View,
                                      private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductRecommendationDataModel>(view) {

    var carouselModelId: String? = null

    companion object {
        val LAYOUT = R.layout.item_dynamic_recommendation
    }

    init {
        view.product_recom_1.carouselProductPool = listener.getPdpCarouselPool()
    }

    override fun bind(element: ProductRecommendationDataModel) {
        this.carouselModelId = element.name
        view.rvProductRecom.gone()
        view.visible()
        view.loadingRecom.visible()
        element.recomWidgetData?.run {
            view.loadingRecom.gone()
            view.titleRecom.text = title
            view.rvProductRecom.show()
            if (seeMoreAppLink.isNotEmpty()) {
                view.seeMoreRecom.show()
            } else {
                view.seeMoreRecom.hide()
            }
            view.seeMoreRecom.setOnClickListener {
                listener.onSeeAllRecomClicked(pageName, seeMoreAppLink)
            }
            initAdapter(this, element.cardModel)
        }
    }

    private fun initAdapter(product: RecommendationWidget, cardModel: List<ProductCardModel>?) {
        view.rvProductRecom.bindCarouselProductCardView(
                parentView = view,
                isScrollable = true,
                carouselModelId = carouselModelId,
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
                productCardModelList = cardModel?.toMutableList() ?: listOf())
    }

    override fun onViewRecycled() {
        view.product_recom_1.onViewRecycled(carouselModelId?:"")
        super.onViewRecycled()
    }
}