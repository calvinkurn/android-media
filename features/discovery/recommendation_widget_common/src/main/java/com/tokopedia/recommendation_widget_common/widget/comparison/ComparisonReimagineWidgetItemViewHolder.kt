package com.tokopedia.recommendation_widget_common.widget.comparison

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.listener.IAdsViewHolderTrackListener
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnAttachStateChangeListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.layout.ProductConstraintLayout
import com.tokopedia.recommendation_widget_common.databinding.ItemComparisonReimagineWidgetBinding
import com.tokopedia.recommendation_widget_common.listener.AdsItemClickListener
import com.tokopedia.recommendation_widget_common.listener.AdsViewListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.ProductRecommendationTracking
import com.tokopedia.recommendation_widget_common.widget.comparison.tracking.ComparisonWidgetTracking
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.viewBinding

class ComparisonReimagineWidgetItemViewHolder(
    val view: View,
    private val adsViewListener: AdsViewListener?,
    private val adsItemClickListener: AdsItemClickListener?,
): RecyclerView.ViewHolder(view), ComparisonViewHolder {

    private var binding: ItemComparisonReimagineWidgetBinding? by viewBinding()

    companion object {
        private const val CLASS_NAME = "com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonReimagineWidgetItemViewHolder.kt"
    }
    val context: Context = view.context

    override fun bind(
        comparisonModel: ComparisonModel,
        comparisonListModel: ComparisonListModel,
        comparisonWidgetInterface: ComparisonWidgetInterface,
        recommendationTrackingModel: RecommendationTrackingModel,
        trackingQueue: TrackingQueue?,
        userSession: UserSessionInterface
    ) {
        binding?.specsView?.setSpecsInfo(comparisonModel.specsModel)
        binding?.productCardView?.setProductModel(comparisonModel.productCardModel)
        if (comparisonModel.isClickable) {
            binding?.productCardView?.setOnClickListener(object: ProductCardClickListener {
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
        binding?.productCardView?.setVisibilityPercentListener(object : ProductConstraintLayout.OnVisibilityPercentChanged {
            override fun onShow() {
                adsViewListener?.onViewAttachedToWindow(comparisonModel.recommendationItem, bindingAdapterPosition)
            }

            override fun onShowOver(maxPercentage: Int) {
                adsViewListener?.onViewDetachedFromWindow(comparisonModel.recommendationItem, bindingAdapterPosition, maxPercentage)
            }
        })
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
}

