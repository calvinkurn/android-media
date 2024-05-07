package com.tokopedia.recommendation_widget_common.widget.comparison.compareditem

import android.content.Context
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.listener.IAdsViewHolderTrackListener
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.layout.ProductConstraintLayout
import com.tokopedia.recommendation_widget_common.byteio.sendShowAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowOverAdsByteIo
import com.tokopedia.recommendation_widget_common.databinding.ItemComparisonReimagineComparedWidgetBinding
import com.tokopedia.recommendation_widget_common.listener.AdsItemClickListener
import com.tokopedia.recommendation_widget_common.listener.AdsViewListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.ProductRecommendationTracking
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonListModel
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonModel
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonViewHolder
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonWidgetInterface
import com.tokopedia.recommendation_widget_common.widget.comparison.RecommendationTrackingModel
import com.tokopedia.recommendation_widget_common.widget.comparison.tracking.ComparisonWidgetTracking
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.viewBinding

class ComparisonReimagineWidgetComparedItemViewHolder(
    val view: View,
    private val adsViewListener: AdsViewListener?,
    private val adsItemClickListener: AdsItemClickListener?,
) : RecyclerView.ViewHolder(view), ComparisonViewHolder, IAdsViewHolderTrackListener {

    private var binding: ItemComparisonReimagineComparedWidgetBinding? by viewBinding()

    private var viewVisiblePercentage = 0
    private var recommendationItem: RecommendationItem? = null

    companion object {
        private const val CLASS_NAME = "com.tokopedia.recommendation_widget_common.widget.comparison.caompareditem.ComparisonReimagineWidgetComparedItemViewHolder.kt"
    }

    val context: Context = view.context

    init {
//        itemView.addOnAttachStateChangeListener(
//            onViewAttachedToWindow = { onViewAttachedToWindow() },
//            onViewDetachedFromWindow = { onViewDetachedFromWindow(visiblePercentage) }
//        )

//        binding?.productCardView?.apply {
//            setVisiblePercent(
//                this,
//                onVisibilityChange = { v, h ->
//                    Log.i("VIEW_PERCENT", "${recommendationItem?.name} v:${v} h:${h}")
//                },
//                onShow = {
//                    onViewAttachedToWindow()
//                },
//                onShowOver = { percent ->
//                    onViewDetachedFromWindow(percent)
//                }
//            )
//        }
    }

    override fun bind(
        comparisonModel: ComparisonModel,
        comparisonListModel: ComparisonListModel,
        comparisonWidgetInterface: ComparisonWidgetInterface,
        recommendationTrackingModel: RecommendationTrackingModel,
        trackingQueue: TrackingQueue?,
        userSession: UserSessionInterface,
    ) {
        recommendationItem = comparisonModel.recommendationItem
        binding?.specsView?.setSpecsInfo(comparisonModel.specsModel)
        binding?.productCardView?.setProductModel(comparisonModel.productCardModel)
        binding?.productCardView?.setVisibilityPercentListener(object: ProductConstraintLayout.OnVisibilityPercentChanged{
            override fun onShow() {
                comparisonModel.recommendationItem.sendShowAdsByteIo(context)
            }

            override fun onShowOver(maxPercentage: Int) {
                comparisonModel.recommendationItem.sendShowOverAdsByteIo(context, maxPercentage)
            }
        })
        if (comparisonModel.isClickable) {
            binding?.productCardView?.setOnClickListener(object : ProductCardClickListener {
                override fun onClick(v: View) {
                    if (comparisonModel.recommendationItem.isTopAds) {
                        val product = comparisonModel.recommendationItem
                        TopAdsUrlHitter(context).hitClickUrl(
                            CLASS_NAME,
                            product.clickUrl,
                            product.productId.toString(),
                            product.name,
                            product.imageUrl
                        )
                    }
                    TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                        ProductRecommendationTracking.getClickProductTracking(
                            recommendationItem = comparisonModel.recommendationItem,
                            androidPageName = recommendationTrackingModel.androidPageName,
                            headerTitle = recommendationTrackingModel.headerTitle,
                            position = adapterPosition,
                            isLoggedIn = userSession.isLoggedIn,
                            anchorProductId = comparisonListModel.getAnchorProduct()?.recommendationItem?.productId.toString(),
                            userId = userSession.userId,
                            widgetType = ProductRecommendationTracking.COMPARISON_WIDGET
                        )
                    )
                    comparisonWidgetInterface.onProductCardClicked(comparisonModel.recommendationItem, comparisonListModel, adapterPosition)
                }

                override fun onAreaClicked(v: View) {
                    adsItemClickListener?.onAreaClicked(comparisonModel.recommendationItem, bindingAdapterPosition)
                }

                override fun onProductImageClicked(v: View) {
                    adsItemClickListener?.onProductImageClicked(comparisonModel.recommendationItem, bindingAdapterPosition)
                }

                override fun onSellerInfoClicked(v: View) {
                    adsItemClickListener?.onSellerInfoClicked(comparisonModel.recommendationItem, bindingAdapterPosition)
                }
            })
        }
        binding?.productCardView?.addOnImpressionListener(comparisonModel) {
            if (comparisonModel.recommendationItem.isTopAds) {
                val product = comparisonModel.recommendationItem
                TopAdsUrlHitter(context).hitImpressionUrl(
                    CLASS_NAME,
                    product.trackerImageUrl,
                    product.productId.toString(),
                    product.name,
                    product.imageUrl
                )
            }
            trackingQueue?.putEETracking(
                ComparisonWidgetTracking.getImpressionProductTrackingComparisonWidget(
                    recommendationItem = comparisonModel.recommendationItem,
                    androidPageName = recommendationTrackingModel.androidPageName,
                    headerTitle = recommendationTrackingModel.headerTitle,
                    position = adapterPosition,
                    userId = userSession.userId
                )
            )
            comparisonWidgetInterface.onProductCardImpressed(comparisonModel.recommendationItem, comparisonListModel, adapterPosition)
        }
    }

    override fun onViewAttachedToWindow() {
        recommendationItem?.let { adsViewListener?.onViewAttachedToWindow(it, bindingAdapterPosition) }
    }

    override fun onViewDetachedFromWindow(visiblePercentage: Int) {
        recommendationItem?.let { adsViewListener?.onViewDetachedFromWindow(it, bindingAdapterPosition, visiblePercentage) }
        setVisiblePercentage(Int.ZERO)
    }

    override fun setVisiblePercentage(visiblePercentage: Int) {
        this.viewVisiblePercentage = visiblePercentage
    }

    override val visiblePercentage: Int
        get() = viewVisiblePercentage
}
