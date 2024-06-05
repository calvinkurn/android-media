package com.tokopedia.recommendation_widget_common.widget.carousel.global

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.SlideTrackObject
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation
import com.tokopedia.applink.RouteManager
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asProductTrackModel
import com.tokopedia.recommendation_widget_common.databinding.RecommendationWidgetCarouselLayoutBinding
import com.tokopedia.recommendation_widget_common.extension.toProductCardModels
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.viewutil.asLifecycleOwner
import com.tokopedia.recommendation_widget_common.widget.global.IRecommendationWidgetView
import com.tokopedia.recommendation_widget_common.widget.global.recommendationWidgetViewModel
import com.tokopedia.recommendation_widget_common.widget.header.RecommendationHeaderListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.trackingoptimizer.TrackingQueue

/**
 * Created by frenzel on 27/03/23
 */
class RecommendationCarouselWidgetView :
    ConstraintLayout,
    IRecommendationWidgetView<RecommendationCarouselModel>,
    DefaultLifecycleObserver {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val binding: RecommendationWidgetCarouselLayoutBinding by lazyThreadSafetyNone {
        RecommendationWidgetCarouselLayoutBinding.inflate(LayoutInflater.from(context), this)
    }
    private val trackingQueue: TrackingQueue = TrackingQueue(context)
    private val recommendationWidgetViewModel by recommendationWidgetViewModel()

    init {
        context.asLifecycleOwner()?.lifecycle?.addObserver(this)
    }

    override val layoutId: Int
        get() = LAYOUT

    override fun bind(model: RecommendationCarouselModel) {
        if (!model.hasData) {
            hide()
        } else {
            bindData(model)
        }
    }

    private fun bindData(model: RecommendationCarouselModel) {
        show()

        binding.recommendationHeaderView.bindData(
            data = model.widget,
            listener = headerViewListener(model = model)
        )

        if (!binding.recommendationCarouselProduct.isVisible) {
            binding.recommendationCarouselLoading.root.show()
        }

        trackHorizontalScroll(model)

        binding.recommendationCarouselProduct.bindCarouselProductCardViewGrid(
            productCardModelList = model.widget.recommendationItemList.toProductCardModels(),
            showSeeMoreCard = model.widget.seeMoreAppLink.isNotBlank(),
            carouselProductCardOnItemImpressedListener = itemImpressionListener(model),
            carouselProductCardOnItemClickListener = itemClickListener(model),
            carouselProductCardOnItemViewListener = itemViewListener(model),
            carouselSeeMoreClickListener = seeMoreClickListener(model),
            carouselProductCardOnItemATCNonVariantClickListener = itemAddToCartNonVariantListener(model),
            carouselProductCardOnItemAddToCartListener = itemAddToCartListener(model),
            finishCalculate = ::finishCalculateCarouselHeight
        )
    }

    private fun itemAddToCartListener(model: RecommendationCarouselModel) =
        object : CarouselProductCardListener.OnItemAddToCartListener {
            override fun onItemAddToCart(
                productCardModel: ProductCardModel,
                carouselProductCardPosition: Int
            ) {
                val productRecommendation = model.getItem(carouselProductCardPosition) ?: return
                if (model.listener?.onProductAddToCartClick(productRecommendation) == true) return
                recommendationWidgetViewModel?.onDirectAddToCart(
                    model,
                    productRecommendation,
                    productRecommendation.minOrder
                )
            }
        }

    private fun trackHorizontalScroll(model: RecommendationCarouselModel) {
        binding.recommendationCarouselProduct.addHorizontalTrackListener(
            SlideTrackObject(
                moduleName = model.widget.pageName,
                barName = model.widget.pageName
            )
        )
    }

    private fun itemImpressionListener(model: RecommendationCarouselModel) =
        object : CarouselProductCardListener.OnItemImpressedListener {
            override fun getImpressHolder(carouselProductCardPosition: Int): ImpressHolder? =
                model.widget.recommendationItemList.getOrNull(carouselProductCardPosition)

            override fun onItemImpressed(
                productCardModel: ProductCardModel,
                carouselProductCardPosition: Int
            ) {
                val productRecommendation = model.getItem(carouselProductCardPosition) ?: return

                if (model.listener?.onProductImpress(carouselProductCardPosition, productRecommendation) == true) return
                if (productCardModel.isTopAds) {
                    TopAdsUrlHitter(context).hitImpressionUrl(
                        this@RecommendationCarouselWidgetView::class.java.simpleName,
                        productRecommendation.trackerImageUrl,
                        productRecommendation.productId.toString(),
                        productRecommendation.name,
                        productRecommendation.imageUrl
                    )
                }

                if (model.widgetTracking != null) {
                    model.widgetTracking.sendEventItemImpression(
                        trackingQueue,
                        productRecommendation
                    )
                } else {
                    RecommendationCarouselTracking.sendEventItemImpression(
                        trackingQueue,
                        model.widget,
                        productRecommendation,
                        model.trackingModel
                    )
                }

                AppLogRecommendation.sendProductShowAppLog(
                    productRecommendation.asProductTrackModel(
                        entranceForm = EntranceForm.HORIZONTAL_GOODS_CARD,
                        additionalParam = model.appLogAdditionalParam
                    )
                )
            }
        }

    private fun itemViewListener(model: RecommendationCarouselModel) =
        object : CarouselProductCardListener.OnViewListener {
            override fun onViewAttachedToWindow(
                productCardModel: ProductCardModel,
                carouselProductCardPosition: Int
            ) {
                val productRecommendation = model.getItem(carouselProductCardPosition) ?: return
                model.listener?.onViewAttachedToWindow(carouselProductCardPosition, productRecommendation)
            }

            override fun onViewDetachedFromWindow(
                productCardModel: ProductCardModel,
                carouselProductCardPosition: Int,
                visiblePercentage: Int
            ) {
                val productRecommendation = model.getItem(carouselProductCardPosition) ?: return
                model.listener?.onViewDetachedFromWindow(carouselProductCardPosition, productRecommendation, visiblePercentage)
            }
        }

    private fun itemClickListener(model: RecommendationCarouselModel) =
        object : CarouselProductCardListener.OnItemClickListener {
            override fun onItemClick(
                productCardModel: ProductCardModel,
                carouselProductCardPosition: Int
            ) {
                val productRecommendation = model.getItem(carouselProductCardPosition) ?: return

                if (model.listener?.onProductClick(carouselProductCardPosition, productRecommendation) == true) return
                if (productCardModel.isTopAds) {
                    TopAdsUrlHitter(context).hitClickUrl(
                        this@RecommendationCarouselWidgetView::class.java.simpleName,
                        productRecommendation.clickUrl,
                        productRecommendation.productId.toString(),
                        productRecommendation.name,
                        productRecommendation.imageUrl
                    )
                }
                if (model.widgetTracking != null) {
                    model.widgetTracking.sendEventItemClick(productRecommendation)
                } else {
                    RecommendationCarouselTracking.sendEventItemClick(
                        model.widget,
                        productRecommendation,
                        model.trackingModel
                    )
                }

                AppLogRecommendation.sendProductClickAppLog(
                    productRecommendation.asProductTrackModel(
                        entranceForm = EntranceForm.HORIZONTAL_GOODS_CARD,
                        additionalParam = model.appLogAdditionalParam
                    )
                )

                RouteManager.route(context, productRecommendation.appUrl)
            }

            override fun onAreaClicked(
                productCardModel: ProductCardModel,
                carouselProductCardPosition: Int
            ) {
                val productRecommendation = model.getItem(carouselProductCardPosition) ?: return
                model.listener?.onAreaClicked(carouselProductCardPosition, productRecommendation)
            }

            override fun onProductImageClicked(
                productCardModel: ProductCardModel,
                carouselProductCardPosition: Int
            ) {
                val productRecommendation = model.getItem(carouselProductCardPosition) ?: return
                model.listener?.onProductImageClicked(carouselProductCardPosition, productRecommendation)
            }

            override fun onSellerInfoClicked(
                productCardModel: ProductCardModel,
                carouselProductCardPosition: Int
            ) {
                val productRecommendation = model.getItem(carouselProductCardPosition) ?: return
                model.listener?.onSellerInfoClicked(carouselProductCardPosition, productRecommendation)
            }
        }

    private fun seeMoreClickListener(model: RecommendationCarouselModel) =
        object : CarouselProductCardListener.OnSeeMoreClickListener {
            override fun onSeeMoreClick() {
                AppLogAnalytics.setGlobalParamOnClick(enterMethod = AppLogParam.ENTER_METHOD_FMT_PAGENAME.format(model.widget.pageName))
                model.widgetTracking?.sendEventSeeAll()
                RouteManager.route(context, model.widget.seeMoreAppLink)
            }
        }

    private fun itemAddToCartNonVariantListener(model: RecommendationCarouselModel) =
        object : CarouselProductCardListener.OnATCNonVariantClickListener {
            override fun onATCNonVariantClick(
                productCardModel: ProductCardModel,
                carouselProductCardPosition: Int,
                quantity: Int
            ) {
                val productRecommendation = model.getItem(carouselProductCardPosition) ?: return
                recommendationWidgetViewModel?.onAddToCartNonVariant(
                    model,
                    productRecommendation,
                    quantity
                )
            }
        }

    private fun headerViewListener(model: RecommendationCarouselModel) =
        object : RecommendationHeaderListener {
            override fun onSeeAllClick(link: String) {
                AppLogAnalytics.setGlobalParamOnClick(enterMethod = AppLogParam.ENTER_METHOD_FMT_PAGENAME.format(model.widget.pageName))
                model.widgetTracking?.sendEventSeeAll()
                RouteManager.route(context, link)
            }

            override fun onChannelExpired(widget: RecommendationWidget) {}
        }

    private fun finishCalculateCarouselHeight() {
        binding.recommendationCarouselProduct.show()
        binding.recommendationCarouselLoading.root.hide()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        trackingQueue.sendAll()
    }

    override fun recycle() {
        binding.recommendationCarouselProduct.recycle()
    }

    companion object {
        val LAYOUT = R.layout.recommendation_widget_carousel_layout
    }
}
