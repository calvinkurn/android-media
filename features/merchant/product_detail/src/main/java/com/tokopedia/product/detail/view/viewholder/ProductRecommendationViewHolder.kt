package com.tokopedia.product.detail.view.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.SlideTrackObject
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation
import com.tokopedia.applink.RouteManager
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateCookieHelper
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.utils.extensions.addOnImpressionListener
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.databinding.ItemDynamicRecommendationBinding
import com.tokopedia.product.detail.view.listener.ProductDetailListener
import com.tokopedia.product.detail.view.util.AnnotationFilterDiffUtil
import com.tokopedia.productcard.ProductCardLifecycleObserver
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.extension.asProductTrackModel
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

class ProductRecommendationViewHolder(
    private val view: View,
    private val listener: ProductDetailListener,
    private val affiliateCookieHelper: AffiliateCookieHelper
) : AbstractViewHolder<ProductRecommendationDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_recommendation
    }

    private val binding = ItemDynamicRecommendationBinding.bind(view)

    private var annotationChipAdapter: AnnotationChipFilterAdapter? = null

    private val lifecycle = ProductCardLifecycleObserver()

    init {
        binding.rvProductRecom.productCardLifecycleObserver = lifecycle
        listener.getParentLifeCyclerOwner().lifecycle.addObserver(lifecycle)
    }

    override fun bind(element: ProductRecommendationDataModel) {
        if (element.recomWidgetData == null || element.recomWidgetData?.recommendationItemList?.isEmpty() == true) {
            binding.rvProductRecom.gone()
            view.visible()
            binding.loadingRecom.visible()
        } else {
            element.recomWidgetData?.run {
                view.addOnImpressionListener(
                    holder = element.impressHolder,
                    holders = listener.getImpressionHolders(),
                    name = element.name,
                    useHolders = listener.isRemoteCacheableActive()
                ) {
                    listener.onImpressComponent(getComponentTrackData(element))
                }
                if (annotationChipAdapter == null && element.filterData?.isNotEmpty() == true) {
                    annotationChipAdapter =
                        AnnotationChipFilterAdapter(object : AnnotationChipListener {
                            override fun onFilterAnnotationClicked(
                                annotationChip: AnnotationChip,
                                position: Int
                            ) {
                                annotationChipAdapter?.submitList(
                                    element.filterData?.map {
                                        it.copy(
                                            recommendationFilterChip = it.recommendationFilterChip.copy(
                                                isActivated =
                                                annotationChip.recommendationFilterChip.name == it.recommendationFilterChip.name &&
                                                    !annotationChip.recommendationFilterChip.isActivated
                                            )
                                        )
                                    } ?: listOf()
                                )
                                listener.onChipFilterClicked(
                                    element,
                                    annotationChip.copy(
                                        recommendationFilterChip = annotationChip.recommendationFilterChip.copy(
                                            isActivated = !annotationChip.recommendationFilterChip.isActivated
                                        )
                                    ),
                                    adapterPosition,
                                    position
                                )
                                binding.loadingRecom.show()
                                binding.rvProductRecom.hide()
                                binding.rvProductRecom.recycle()
                            }
                        })
                    binding.chipFilterRecyclerview.adapter = annotationChipAdapter
                }
                element.filterData?.let {
                    if (it.isNotEmpty()) {
                        annotationChipAdapter?.submitList(it)
                        binding.chipFilterRecyclerview.visible()
                    } else {
                        binding.chipFilterRecyclerview.gone()
                    }
                }

                initAdapter(element, this, element.cardModel, getComponentTrackData(element))

                binding.titleRecom.text = title
                handleSubtitlePosition(recomSubtitle = subtitle)
                if (seeMoreAppLink.isNotEmpty()) {
                    binding.seeMoreRecom.show()
                } else {
                    binding.seeMoreRecom.hide()
                }

                binding.seeMoreRecom.setOnClickListener {
                    element.recomWidgetData?.let {
                        listener.onSeeAllRecomClicked(
                            it,
                            pageName,
                            seeMoreAppLink + (
                                element.filterData?.find { it.recommendationFilterChip.isActivated }?.recommendationFilterChip?.value
                                    ?: ""
                                ),
                            getComponentTrackData(element)
                        )
                    }
                }
            }
            binding.baseRecom.show()
        }
    }

    override fun bind(element: ProductRecommendationDataModel?, payloads: MutableList<Any>) {
        when ((payloads.firstOrNull() as? Int)) {
            ProductDetailConstant.PAYLOAD_UPDATE_FILTER_RECOM -> {
                element?.recomWidgetData?.let {
                    initAdapter(element, it, element.cardModel, getComponentTrackData(element))
                    annotationChipAdapter?.submitList(element.filterData ?: listOf())
                    binding.loadingRecom.gone()
                    binding.rvProductRecom.show()
                }
            }
            ProductDetailConstant.PAYLOAD_UPDATE_QTY_RECOM_TOKONOW -> {
                element?.recomWidgetData?.let {
                    initAdapter(element, it, element.cardModel, getComponentTrackData(element))
                }
            }
        }
    }

    private fun initAdapter(
        element: ProductRecommendationDataModel,
        product: RecommendationWidget,
        cardModel: List<ProductCardModel>?,
        componentTrackDataModel: ComponentTrackDataModel
    ) {
        binding.rvProductRecom.bindCarouselProductCardViewGrid(
            scrollToPosition = listener.getRecommendationCarouselSavedState().get(adapterPosition),
            recyclerViewPool = listener.getParentRecyclerViewPool(),
            showSeeMoreCard = product.seeMoreAppLink.isNotBlank(),
            carouselProductCardOnItemClickListener = object :
                CarouselProductCardListener.OnItemClickListener {
                override fun onItemClick(
                    productCardModel: ProductCardModel,
                    carouselProductCardPosition: Int
                ) {
                    val productRecommendation =
                        product.recommendationItemList.getOrNull(carouselProductCardPosition)
                            ?: return
                    val topAdsClickUrl = productRecommendation.clickUrl
                    if (productCardModel.isTopAds) {
                        listener.sendTopAdsClick(
                            topAdsClickUrl,
                            productRecommendation.productId.toString(),
                            productRecommendation.name,
                            productRecommendation.imageUrl
                        )
                    }

                    listener.eventRecommendationClick(
                        productRecommendation,
                        annotationChipAdapter?.getSelectedChip()?.value ?: "",
                        carouselProductCardPosition,
                        product.pageName,
                        product.title,
                        componentTrackDataModel
                    )

                    AppLogRecommendation.sendProductClickAppLog(
                        productRecommendation.asProductTrackModel(entranceForm = EntranceForm.HORIZONTAL_GOODS_CARD)
                    )

                    view.context?.run {
                        RouteManager.route(
                            this,
                            productRecommendationApplink(product, productRecommendation)
                        )
                    }
                }
            },
            carouselProductCardOnItemImpressedListener = object :
                CarouselProductCardListener.OnItemImpressedListener {
                override fun getImpressHolder(carouselProductCardPosition: Int): ImpressHolder? {
                    return product.recommendationItemList.getOrNull(carouselProductCardPosition)
                }

                override fun onItemImpressed(
                    productCardModel: ProductCardModel,
                    carouselProductCardPosition: Int
                ) {
                    val productRecommendation =
                        product.recommendationItemList.getOrNull(carouselProductCardPosition)
                            ?: return
                    val topAdsImageUrl = productRecommendation.trackerImageUrl
                    if (productCardModel.isTopAds) {
                        listener.sendTopAdsImpression(
                            topAdsImageUrl,
                            productRecommendation.productId.toString(),
                            productRecommendation.name,
                            productRecommendation.imageUrl
                        )
                    }

                    listener.eventRecommendationImpression(
                        productRecommendation,
                        annotationChipAdapter?.getSelectedChip()?.value ?: "",
                        carouselProductCardPosition,
                        product.pageName,
                        product.title,
                        componentTrackDataModel
                    )

                    AppLogRecommendation.sendProductShowAppLog(
                        productRecommendation.asProductTrackModel(entranceForm = EntranceForm.HORIZONTAL_GOODS_CARD)
                    )
                }
            },
            carouselSeeMoreClickListener = object :
                CarouselProductCardListener.OnSeeMoreClickListener {
                override fun onSeeMoreClick() {
                    element.recomWidgetData?.let {
                        listener.onSeeAllRecomClicked(
                            it,
                            product.pageName,
                            product.seeMoreAppLink,
                            getComponentTrackData(element)
                        )
                    }
                }
            },
            productCardModelList = cardModel?.toMutableList() ?: listOf(),
            carouselProductCardOnItemATCNonVariantClickListener = object :
                CarouselProductCardListener.OnATCNonVariantClickListener {
                override fun onATCNonVariantClick(
                    productCardModel: ProductCardModel,
                    carouselProductCardPosition: Int,
                    quantity: Int
                ) {
                    listener.getRecommendationCarouselSavedState()
                        .put(adapterPosition, binding.rvProductRecom.getCurrentPosition())

                    val productRecommendation =
                        product.recommendationItemList.getOrNull(carouselProductCardPosition)
                            ?: return
                    productRecommendation.onCardQuantityChanged(quantity)
                    listener.onRecomAddToCartNonVariantQuantityChangedClick(
                        recommendationWidget = product,
                        recomItem = productRecommendation,
                        quantity = quantity,
                        adapterPosition = adapterPosition,
                        itemPosition = carouselProductCardPosition
                    )
                }
            },
            carouselProductCardOnItemAddVariantClickListener = object :
                CarouselProductCardListener.OnAddVariantClickListener {
                override fun onAddVariantClick(
                    productCardModel: ProductCardModel,
                    carouselProductCardPosition: Int
                ) {
                    listener.getRecommendationCarouselSavedState()
                        .put(adapterPosition, binding.rvProductRecom.getCurrentPosition())

                    val productRecommendation =
                        product.recommendationItemList.getOrNull(carouselProductCardPosition)
                            ?: return
                    listener.onRecomAddVariantClick(
                        recomItem = productRecommendation,
                        adapterPosition = adapterPosition,
                        itemPosition = carouselProductCardPosition
                    )
                }
            },
            carouselProductCardOnItemAddToCartListener = object : CarouselProductCardListener.OnItemAddToCartListener {
                override fun onItemAddToCart(
                    productCardModel: ProductCardModel,
                    carouselProductCardPosition: Int
                ) {
                    val productRecommendation =
                        product.recommendationItemList.getOrNull(carouselProductCardPosition)
                            ?: return
                    listener.onRecomAddToCartClick(
                        recommendationWidget = product,
                        recomItem = productRecommendation,
                        adapterPosition = absoluteAdapterPosition,
                        itemPosition = carouselProductCardPosition
                    )
                }
            },
            finishCalculate = {
                binding.rvProductRecom.show()
                binding.loadingRecom.gone()
            }
        )
        trackHorizontalScroll(element)
    }

    private fun trackHorizontalScroll(model: ProductRecommendationDataModel) {
        binding.rvProductRecom.addHorizontalTrackListener(
            SlideTrackObject(
                moduleName = model.recomWidgetData?.pageName.orEmpty(),
                barName = model.recomWidgetData?.pageName.orEmpty(),
            )
        )
    }

    private fun productRecommendationApplink(
        recommendationWidget: RecommendationWidget,
        productRecommendation: RecommendationItem
    ) = if (recommendationWidget.isTokonow) {
        affiliateCookieHelper.createAffiliateLink(
            productRecommendation.appUrl,
            recommendationWidget.affiliateTrackerId
        )
    } else {
        productRecommendation.appUrl
    }

    private fun getComponentTrackData(element: ProductRecommendationDataModel?) =
        ComponentTrackDataModel(
            element?.type
                ?: "",
            element?.name ?: "",
            adapterPosition + 1
        )

    private fun handleSubtitlePosition(recomSubtitle: String?) = with(binding) {
        if (recomSubtitle?.isNotEmpty() == true) {
            subtitleRecom.text = recomSubtitle
            subtitleRecom.visible()
        } else {
            subtitleRecom.text = ""
            subtitleRecom.gone()
        }

        if (recomSubtitle?.isEmpty() == true) {
            val constraintSet = ConstraintSet()
            constraintSet.clone(pdpRecomHeaderContainer)
            constraintSet.connect(
                R.id.seeMoreRecom,
                ConstraintSet.TOP,
                R.id.titleRecom,
                ConstraintSet.TOP,
                0
            )
            constraintSet.connect(
                R.id.seeMoreRecom,
                ConstraintSet.BOTTOM,
                R.id.titleRecom,
                ConstraintSet.BOTTOM,
                0
            )
            constraintSet.applyTo(pdpRecomHeaderContainer)
        } else {
            val constraintSet = ConstraintSet()
            constraintSet.clone(pdpRecomHeaderContainer)
            constraintSet.connect(
                R.id.seeMoreRecom,
                ConstraintSet.TOP,
                R.id.subtitleRecom,
                ConstraintSet.TOP,
                0
            )
            constraintSet.connect(
                R.id.seeMoreRecom,
                ConstraintSet.BOTTOM,
                R.id.subtitleRecom,
                ConstraintSet.BOTTOM,
                0
            )
            constraintSet.applyTo(pdpRecomHeaderContainer)
        }
    }

    override fun onViewRecycled() {
        listener.getRecommendationCarouselSavedState()
            .put(adapterPosition, binding.rvProductRecom.getCurrentPosition())
        binding.rvProductRecom.recycle()
        super.onViewRecycled()
    }

    internal class AnnotationChipFilterAdapter(private val listener: AnnotationChipListener) :
        RecyclerView.Adapter<ProductRecommendationAnnotationChipViewHolder>() {
        private val annotationList = mutableListOf<AnnotationChip>()
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ProductRecommendationAnnotationChipViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(ProductRecommendationAnnotationChipViewHolder.LAYOUT, parent, false)
            return ProductRecommendationAnnotationChipViewHolder(view, listener)
        }

        override fun getItemCount(): Int = annotationList.size

        override fun onBindViewHolder(
            holder: ProductRecommendationAnnotationChipViewHolder,
            position: Int
        ) {
            holder.bind(annotationList[position])
        }

        override fun onBindViewHolder(
            holder: ProductRecommendationAnnotationChipViewHolder,
            position: Int,
            payloads: MutableList<Any>
        ) {
            if (payloads.isNotEmpty()) {
                holder.bind(annotationList[position], payloads)
            } else {
                super.onBindViewHolder(holder, position, payloads)
            }
        }

        fun submitList(list: List<AnnotationChip>) {
            val diffCallback = AnnotationFilterDiffUtil(annotationList.toMutableList(), list)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            annotationList.clear()
            annotationList.addAll(list)
            diffResult.dispatchUpdatesTo(this)
        }

        fun getSelectedChip() =
            annotationList.find { it.recommendationFilterChip.isActivated }?.recommendationFilterChip
    }

    interface AnnotationChipListener {
        fun onFilterAnnotationClicked(annotationChip: AnnotationChip, position: Int)
    }
}
