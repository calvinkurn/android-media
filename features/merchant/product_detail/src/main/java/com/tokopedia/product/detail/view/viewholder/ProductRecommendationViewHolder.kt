package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.carouselproductcard.CarouselProductCardListener
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
            } else {
                view.see_more_recom_1.hide()
            }
            view.see_more_recom_1.setOnClickListener {
                listener.onSeeAllRecomClicked(pageName, seeMoreAppLink)
            }
            initAdapter(this, element.cardModel)
        }
    }

    private fun initAdapter(product: RecommendationWidget, cardModel: List<ProductCardModel>?) {
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
                productCardModelList = cardModel ?: listOf())
    }
}