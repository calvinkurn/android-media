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

    override fun bind(element: ProductRecommendationDataModel) {
        this.carouselModelId = element.name
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
            initAdapter(this, element.cardModel, getComponentTrackData(element))
        }
    }

    private fun initAdapter(product: RecommendationWidget, cardModel: List<ProductCardModel>?, componentTrackDataModel: ComponentTrackDataModel) {
        view.rvProductRecom.bindCarouselProductCardView(
                carouselCardSavedStatePosition = listener.getRecommendationCarouselSavedState(),
                viewHolderPosition = adapterPosition,
                parentView = view,
                isScrollable = true,
                carouselModelId = carouselModelId,
                recyclerViewPool = listener.getParentRecyclerViewPool(),
                carouselProductCardOnItemClickListener = object : CarouselProductCardListener.OnItemClickListener {
                    override fun onItemClick(productCardModel: ProductCardModel, adapterPosition: Int) {
                        val productRecommendation = product.recommendationItemList[adapterPosition]
                        val topAdsClickUrl = productRecommendation.clickUrl
                        if (productCardModel.isTopAds) {
                            ImpresionTask().execute(topAdsClickUrl)
                        }

                        listener.eventRecommendationClick(productRecommendation, adapterPosition, product.pageName, product.title, componentTrackDataModel)

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
                        if (product.recommendationItemList.size > adapterPosition) {
                            val productRecommendation = product.recommendationItemList[adapterPosition]
                            val topAdsImageUrl = productRecommendation.trackerImageUrl
                            if (productCardModel.isTopAds) {
                                ImpresionTask().execute(topAdsImageUrl)
                            }

                            listener.eventRecommendationImpression(productRecommendation,
                                    adapterPosition,
                                    product.pageName,
                                    product.title, componentTrackDataModel)
                        }
                    }
                },
                productCardModelList = cardModel?.toMutableList() ?: listOf())
    }

    private fun getComponentTrackData(element: ProductRecommendationDataModel?) = ComponentTrackDataModel(element?.type
            ?: "", element?.name ?: "", adapterPosition + 1)

    override fun onViewRecycled() {
        listener.getRecommendationCarouselSavedState().put(adapterPosition, view.rvProductRecom.getCurrentPosition())
        super.onViewRecycled()
    }
}