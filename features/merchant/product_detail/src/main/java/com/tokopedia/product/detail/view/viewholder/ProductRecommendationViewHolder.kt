package com.tokopedia.product.detail.view.viewholder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
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
import com.tokopedia.product.detail.view.util.AnnotationFilterDiffUtil
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.remoteconfig.RemoteConfigInstance
import kotlinx.android.synthetic.main.item_dynamic_recommendation.view.*

class ProductRecommendationViewHolder(
      private val view: View,
      private val listener: DynamicProductDetailListener)
: AbstractViewHolder<ProductRecommendationDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_recommendation
        private const val EXPERIMENT_NAME = "See more button card"
        private const val SEE_MORE_CARD_AB_VALUE = "See more card"
        const val KEY_UPDATE_FILTER_RECOM = "KEY_UPDATE_FILTER_RECOM"
    }

    private var annotationChipAdapter: AnnotationChipFilterAdapter? = null

    override fun bind(element: ProductRecommendationDataModel) {
        view.rvProductRecom.gone()
        view.visible()
        view.loadingRecom.visible()
        element.recomWidgetData?.run {
            view.addOnImpressionListener(element.impressHolder) {
                listener.onImpressComponent(getComponentTrackData(element))
            }
            if(annotationChipAdapter == null){
                annotationChipAdapter = AnnotationChipFilterAdapter(object : AnnotationChipListener{
                    override fun onFilterAnnotationClicked(annotationChip: AnnotationChip, position: Int) {
                        annotationChipAdapter?.submitList(
                            element.filterData?.map {
                                it.copy(
                                    recommendationFilterChip = it.recommendationFilterChip.copy(
                                            isActivated =
                                            annotationChip.recommendationFilterChip.name == it.recommendationFilterChip.name
                                                    && !annotationChip.recommendationFilterChip.isActivated
                                    )
                                )
                            } ?: listOf()
                        )
                        listener.onChipFilterClicked(element, annotationChip.copy(
                                recommendationFilterChip = annotationChip.recommendationFilterChip.copy(
                                        isActivated = !annotationChip.recommendationFilterChip.isActivated
                                )), adapterPosition, position
                        )
                        view.loadingRecom.show()
                        view.rvProductRecom.hide()
                        view.rvProductRecom.recycle()
                    }
                })
            }

            view.chip_filter_recyclerview?.adapter = annotationChipAdapter
            annotationChipAdapter?.submitList(element.filterData ?: listOf())
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

    override fun bind(element: ProductRecommendationDataModel?, payloads: MutableList<Any>) {
        if(payloads.isNotEmpty() && payloads.first() is Bundle && (payloads.first() as Bundle).containsKey(KEY_UPDATE_FILTER_RECOM)){
            element?.recomWidgetData?.let {
                initAdapter(element, it, element.cardModel, getComponentTrackData(element))
                annotationChipAdapter?.submitList(element.filterData ?: listOf())
                view.loadingRecom.gone()
                view.rvProductRecom.show()
            }
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

    internal class AnnotationChipFilterAdapter (private val listener: AnnotationChipListener): RecyclerView.Adapter<ProductRecommendationAnnotationChipViewHolder>(){
        private val annotationList = mutableListOf<AnnotationChip>()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductRecommendationAnnotationChipViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(ProductRecommendationAnnotationChipViewHolder.LAYOUT, parent, false)
            return ProductRecommendationAnnotationChipViewHolder(view, listener)
        }

        override fun getItemCount(): Int = annotationList.size

        override fun onBindViewHolder(holder: ProductRecommendationAnnotationChipViewHolder, position: Int) {
            holder.bind(annotationList[position])
        }

        override fun onBindViewHolder(holder: ProductRecommendationAnnotationChipViewHolder, position: Int, payloads: MutableList<Any>) {
            if(payloads.isNotEmpty()) holder.bind(annotationList[position], payloads)
            else super.onBindViewHolder(holder, position, payloads)
        }

        fun submitList(list: List<AnnotationChip>){
            val diffCallback = AnnotationFilterDiffUtil(annotationList.toMutableList(), list)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            annotationList.clear()
            annotationList.addAll(list)
            diffResult.dispatchUpdatesTo(this)
        }
    }
    interface AnnotationChipListener{
        fun onFilterAnnotationClicked(annotationChip: AnnotationChip, position: Int)
    }
}
