package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.remoteconfig.RemoteConfigInstance
import kotlinx.android.synthetic.main.item_dynamic_recommendation.view.*

class ProductRecommendationViewHolder(private val view: View,
                                      private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductRecommendationDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_recommendation
        private const val EXPERIMENT_NAME = "See more button card"
        private const val SEE_MORE_CARD_AB_VALUE = "See more card"
    }

    override fun bind(element: ProductRecommendationDataModel) {
        view.rvProductRecom.gone()
        view.visible()
        view.loadingRecom.visible()
        element.recomWidgetData?.run {

            view.addOnImpressionListener(element.impressHolder) {
                listener.onImpressComponent(getComponentTrackData(element))
            }

            view.loadingRecom.gone()
            view.titleRecom.text = title
            view.rvProductRecom.show()
            if (seeMoreAppLink.isNotEmpty()) {
                view.seeMoreRecom.show()
            } else {
                view.seeMoreRecom.hide()
            }
            view.seeMoreRecom.setOnClickListener {
                listener.onSeeAllRecomClicked(pageName, seeMoreAppLink, getComponentTrackData(element))
            }
            initAdapter(element, this, element.cardModel, getComponentTrackData(element))
        }
    }

    private fun initAdapter(element: ProductRecommendationDataModel, product:RecommendationWidget, cardModel: List<ProductCardModel>?, componentTrackDataModel: ComponentTrackDataModel) {

        view.rvProductRecom.bindCarouselProductCardViewGrid(
                scrollToPosition = listener.getRecommendationCarouselSavedState().get(adapterPosition),
                recyclerViewPool = listener.getParentRecyclerViewPool(),
                showSeeMoreCard = RemoteConfigInstance.getInstance().abTestPlatform.getString(EXPERIMENT_NAME) == SEE_MORE_CARD_AB_VALUE && product.seeMoreAppLink.isNotBlank(),
                carouselProductCardOnItemClickListener = object : CarouselProductCardListener.OnItemClickListener {
                    override fun onItemClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                        val productRecommendation = product.recommendationItemList.getOrNull(carouselProductCardPosition) ?: return
                        val topAdsClickUrl = productRecommendation.clickUrl
                        if (productCardModel.isTopAds) {
                            listener.sendTopAdsClick(topAdsClickUrl, productRecommendation.productId.toString(), productRecommendation.name, productRecommendation.imageUrl)
                        }

                        listener.eventRecommendationClick(productRecommendation, carouselProductCardPosition, product.pageName, product.title, componentTrackDataModel)

                        view.context?.run {
                            RouteManager.route(this,
                                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                                    productRecommendation.productId.toString())
                        }
                    }
                },
                carouselProductCardOnItemImpressedListener = object : CarouselProductCardListener.OnItemImpressedListener {
                    override fun getImpressHolder(carouselProductCardPosition: Int): ImpressHolder? {
                        return product.recommendationItemList.getOrNull(carouselProductCardPosition)
                    }

                    override fun onItemImpressed(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                        val productRecommendation = product.recommendationItemList.getOrNull(carouselProductCardPosition) ?: return
                        val topAdsImageUrl = productRecommendation.trackerImageUrl
                        if (productCardModel.isTopAds) {
                            listener.sendTopAdsImpression(topAdsImageUrl, productRecommendation.productId.toString(), productRecommendation.name, productRecommendation.imageUrl)
                        }

                        listener.eventRecommendationImpression(productRecommendation,
                                carouselProductCardPosition,
                                product.pageName,
                                product.title, componentTrackDataModel)
                    }
                },
                carouselSeeMoreClickListener = object : CarouselProductCardListener.OnSeeMoreClickListener{
                    override fun onSeeMoreClick() {
                        listener.onSeeAllRecomClicked(product.pageName, product.seeMoreAppLink, getComponentTrackData(element))
                    }
                },
                productCardModelList = cardModel?.toMutableList() ?: listOf())
    }

    private fun getComponentTrackData(element: ProductRecommendationDataModel?) = ComponentTrackDataModel(element?.type
            ?: "", element?.name ?: "", adapterPosition + 1)

    override fun onViewRecycled() {
        listener.getRecommendationCarouselSavedState().put(adapterPosition, view.rvProductRecom.getCurrentPosition())
        itemView.rvProductRecom?.recycle()
        super.onViewRecycled()
    }
}