package com.tokopedia.recommendation_widget_common.widget.carousel.global

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.applink.RouteManager
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.RecommendationWidgetCarouselLayoutBinding
import com.tokopedia.recommendation_widget_common.extension.toProductCardModels
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.global.IRecommendationWidgetView
import com.tokopedia.recommendation_widget_common.widget.header.RecommendationHeaderListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.trackingoptimizer.TrackingQueue

/**
 * Created by frenzel on 27/03/23
 */
class RecommendationCarouselWidgetView :
    ConstraintLayout,
    IRecommendationWidgetView<RecommendationCarouselModel>,
    RecommendationHeaderListener,
    DefaultLifecycleObserver {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var binding: RecommendationWidgetCarouselLayoutBinding? = null
    private val trackingQueue: TrackingQueue = TrackingQueue(context)

    init {
        binding = RecommendationWidgetCarouselLayoutBinding.inflate(
            LayoutInflater.from(context),
            this
        )

        (context as? LifecycleOwner)?.lifecycle?.addObserver(this)
    }

    override fun bind(model: RecommendationCarouselModel) {
        binding?.recommendationHeaderView?.bindData(model.widget, this)

        binding?.recommendationCarouselLoading?.root?.show()
        binding?.recommendationCarouselProduct?.hide()

        binding?.recommendationCarouselProduct?.bindCarouselProductCardViewGrid(
            productCardModelList = model.widget.recommendationItemList.toProductCardModels(),
            showSeeMoreCard = model.widget.seeMoreAppLink.isNotBlank(),
            carouselProductCardOnItemImpressedListener = itemImpressionListener(model),
            carouselProductCardOnItemClickListener = itemClickListener(model),
            carouselSeeMoreClickListener = seeMoreClickListener(model),
            finishCalculate = ::finishCalculateCarouselHeight,
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
                val productRecommendation =
                    model.widget.recommendationItemList.getOrNull(carouselProductCardPosition)
                        ?: return

                if (productCardModel.isTopAds)
                    TopAdsUrlHitter(context).hitImpressionUrl(
                        this@RecommendationCarouselWidgetView::class.java.simpleName,
                        productRecommendation.trackerImageUrl,
                        productRecommendation.productId.toString(),
                        productRecommendation.name,
                        productRecommendation.imageUrl,
                    )

                RecommendationCarouselTracking.sendEventItemImpression(
                    trackingQueue,
                    model.widget,
                    productRecommendation,
                    model.trackingModel
                )
            }
        }

    private fun itemClickListener(model: RecommendationCarouselModel) =
        object : CarouselProductCardListener.OnItemClickListener {
            override fun onItemClick(
                productCardModel: ProductCardModel,
                carouselProductCardPosition: Int,
            ) {
                val productRecommendation =
                    model.widget.recommendationItemList.getOrNull(carouselProductCardPosition)
                        ?: return

                if (productCardModel.isTopAds)
                    TopAdsUrlHitter(context).hitClickUrl(
                        this@RecommendationCarouselWidgetView::class.java.simpleName,
                        productRecommendation.clickUrl,
                        productRecommendation.productId.toString(),
                        productRecommendation.name,
                        productRecommendation.imageUrl
                    )

                RecommendationCarouselTracking.sendEventItemClick(
                    model.widget,
                    productRecommendation,
                    model.trackingModel
                )

                RouteManager.route(
                    context,
                    productRecommendation.appUrl,
                )
            }
        }

    private fun seeMoreClickListener(model: RecommendationCarouselModel) =
        object : CarouselProductCardListener.OnSeeMoreClickListener {
            override fun onSeeMoreClick() {
                RecommendationCarouselTracking.sendEventSeeMoreClick()
                RouteManager.route(context, model.widget.seeMoreAppLink)
            }
        }

    private fun finishCalculateCarouselHeight() {
        binding?.recommendationCarouselProduct?.show()
        binding?.recommendationCarouselLoading?.root?.hide()
    }

    override fun onSeeAllClick(link: String) {
        RouteManager.route(context, link)
    }

    override fun onChannelExpired(widget: RecommendationWidget) {

    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        trackingQueue.sendAll()
    }

    companion object {
        val LAYOUT = R.layout.recommendation_widget_carousel_layout
    }
}
