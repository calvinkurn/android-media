package com.tokopedia.recommendation_widget_common.widget.stealthelook

import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recommendation_widget_common.databinding.RecommendationImageReloadBinding
import com.tokopedia.recommendation_widget_common.R as recommendation_widget_commonR
import com.tokopedia.recommendation_widget_common.databinding.RecommendationWidgetStealTheLookPageBinding
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.stealthelook.tracking.StealTheLookTracking
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.unifyprinciples.Typography

class StealTheLookStyleViewHolder(
    itemView: View,
) : RecyclerView.ViewHolder(itemView) {
    companion object {
        val LAYOUT = recommendation_widget_commonR.layout.recommendation_widget_steal_the_look_page
        private const val CLASS_NAME = "com.tokopedia.recommendation_widget_common.widget.steaalthelook.StealTheLookPageViewHolder"
    }

    private val binding = RecommendationWidgetStealTheLookPageBinding.bind(itemView)

    fun bind(model: StealTheLookStyleModel) {
        renderLeftGrid(model)
        renderTopRightGrid(model)
        renderBottomRightGrid(model)
        sendViewportImpression(model)
    }

    private fun StealTheLookStyleModel.getGridAtPos(gridPosition: RecommendationItem.GridPosition): StealTheLookGridModel? {
        return grids.firstOrNull { it.recommendationItem.gridPosition == gridPosition }
    }

    private fun renderLeftGrid(model: StealTheLookStyleModel) {
        val grid = model.getGridAtPos(RecommendationItem.GridPosition.Left)
        val imageView = binding.stlItemLeftGrid.stlItemImage
        val ribbonArchView = binding.stlItemLeftGrid.stlItemRibbonArch
        val ribbonContentView = binding.stlItemLeftGrid.stlItemRibbonContent
        val ribbonTextView = binding.stlItemLeftGrid.stlItemRibbonText
        val reloadLayout = binding.stlItemLeftGrid.stlReload
        renderGrid(grid, imageView, ribbonArchView, ribbonContentView, ribbonTextView, reloadLayout)
        adjustLeftPadding()
        imageView.setGridClickListener(grid, model.tracking)
    }

    private fun renderTopRightGrid(model: StealTheLookStyleModel) {
        val grid = model.getGridAtPos(RecommendationItem.GridPosition.TopRight)
        val imageView = binding.stlItemTopRightGrid.stlItemImage
        val ribbonArchView = binding.stlItemTopRightGrid.stlItemRibbonArch
        val ribbonContentView = binding.stlItemTopRightGrid.stlItemRibbonContent
        val ribbonTextView = binding.stlItemTopRightGrid.stlItemRibbonText
        val reloadLayout = binding.stlItemTopRightGrid.stlReload
        renderGrid(grid, imageView, ribbonArchView, ribbonContentView, ribbonTextView, reloadLayout)
        imageView.setGridClickListener(grid, model.tracking)
    }

    private fun renderBottomRightGrid(model: StealTheLookStyleModel) {
        val grid = model.getGridAtPos(RecommendationItem.GridPosition.BottomRight)
        val imageView = binding.stlItemBottomRightGrid.stlItemImage
        val ribbonArchView = binding.stlItemBottomRightGrid.stlItemRibbonArch
        val ribbonContentView = binding.stlItemBottomRightGrid.stlItemRibbonContent
        val ribbonTextView = binding.stlItemBottomRightGrid.stlItemRibbonText
        val reloadLayout = binding.stlItemBottomRightGrid.stlReload
        renderGrid(grid, imageView, ribbonArchView, ribbonContentView, ribbonTextView, reloadLayout)
        imageView.setGridClickListener(grid, model.tracking)
    }

    private fun adjustLeftPadding() {
        val leftGridRatio: String
        val leftGridWeight: Int
        val outerLeftPadding: Int
        if(binding.stlItemLeftGrid.stlItemRibbonArch.isVisible) {
            leftGridRatio = itemView.context.resources.getString(recommendation_widget_commonR.string.steal_the_look_left_item_ratio)
            leftGridWeight = itemView.context.resources.getInteger(recommendation_widget_commonR.integer.steal_the_look_left_item_horizontal_weight)
            outerLeftPadding = itemView.context.resources.getDimensionPixelOffset(recommendation_widget_commonR.dimen.steal_the_look_outer_left_padding_ribbon)
        } else {
            leftGridRatio = itemView.context.resources.getString(recommendation_widget_commonR.string.steal_the_look_left_image_ratio)
            leftGridWeight = itemView.context.resources.getInteger(recommendation_widget_commonR.integer.steal_the_look_left_image_horizontal_weight)
            outerLeftPadding = itemView.context.resources.getDimensionPixelOffset(recommendation_widget_commonR.dimen.steal_the_look_outer_padding)
        }
        (binding.stlItemLeftGrid.root.layoutParams as? ConstraintLayout.LayoutParams)?.apply {
            dimensionRatio = leftGridRatio
            horizontalWeight = leftGridWeight.toFloat()
        }
        binding.stlLayout.updatePadding(left = outerLeftPadding)
    }

    private fun renderGrid(
        model: StealTheLookGridModel?,
        imageView: ImageView,
        ribbonArchView: View,
        ribbonContentView: View,
        ribbonTextView: Typography,
        reloadLayout: RecommendationImageReloadBinding,
    ) {
        val item = model?.recommendationItem
        renderImage(item?.imageUrl, imageView, reloadLayout)
        renderRibbon(item?.discountPercentage.orEmpty(), ribbonArchView, ribbonContentView, ribbonTextView)
    }

    private fun renderImage(
        imageUrl: String?,
        imageView: ImageView,
        reloadLayout: RecommendationImageReloadBinding,
    ) {
        imageView.loadImage(imageUrl) {
            listener(
                onSuccess = { _, _ ->
                    reloadLayout.recomReloadLoader.hide()
                    reloadLayout.recomReloadLayout.hide()
                },
                onError = {
                    if(imageUrl.isNullOrEmpty()) {
                        reloadLayout.recomReloadLayout.hide()
                    } else {
                        reloadLayout.recomReloadLayout.show()
                        reloadLayout.recomIcReload.setOnClickListener {
                            reloadLayout.recomReloadLayout.hide()
                            reloadLayout.recomReloadLoader.show()
                            renderImage(imageUrl, imageView, reloadLayout)
                        }
                    }
                }
            )
        }
    }

    private fun renderRibbon(
        ribbonText: String,
        ribbonArchView: View,
        ribbonContentView: View,
        ribbonTextView: Typography
    ) {
        if (ribbonText.isNotEmpty()) {
            ribbonArchView.show()
            ribbonContentView.show()
            ribbonTextView.show()
            ribbonTextView.text = ribbonText
        } else {
            ribbonArchView.hide()
            ribbonContentView.hide()
            ribbonTextView.hide()
        }
    }

    private fun View.setGridClickListener(
        model: StealTheLookGridModel?,
        tracking: StealTheLookTracking?
    ) {
        if(model == null) return
        if(model.recommendationItem.appUrl.isBlank()) return

        setOnClickListener {
            if(model.recommendationItem.appUrl.isNotEmpty()) {
                sendTopAdsClickTracker(model.recommendationItem)
                tracking?.sendEventItemClick(model)
                RouteManager.route(context, model.recommendationItem.appUrl)
            }
        }
    }

    private fun sendViewportImpression(model: StealTheLookStyleModel) {
        binding.stlLayout.addOnImpressionListener(model) {
            sendTopadsImpressionTracker(model)
            model.tracking?.sendEventViewportImpression(model)
        }
    }

    private fun sendTopadsImpressionTracker(model: StealTheLookStyleModel) {
        model.grids.forEach {
            val item = it.recommendationItem
            if (item.isTopAds && item.appUrl.isNotEmpty()) {
                TopAdsUrlHitter(itemView.context).hitImpressionUrl(
                    CLASS_NAME,
                    item.trackerImageUrl,
                    item.productId.toString(),
                    item.name,
                    item.imageUrl,
                )
            }
        }
    }

    private fun sendTopAdsClickTracker(recommendationItem: RecommendationItem) {
        if (recommendationItem.isTopAds && recommendationItem.appUrl.isNotEmpty()) {
            TopAdsUrlHitter(itemView.context).hitClickUrl(
                CLASS_NAME,
                recommendationItem.clickUrl,
                recommendationItem.productId.toString(),
                recommendationItem.name,
                recommendationItem.imageUrl
            )
        }
    }

    fun onViewRecycled() { }
}
