package com.tokopedia.recommendation_widget_common.widget.stealthelook

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recommendation_widget_common.R as recommendation_widget_commonR
import com.tokopedia.recommendation_widget_common.databinding.RecommendationWidgetStealTheLookPageBinding
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifyprinciples.Typography

class StealTheLookPageViewHolder(
    itemView: View,
    private val trackingQueue: TrackingQueue
) : RecyclerView.ViewHolder(itemView) {
    companion object {
        val LAYOUT = recommendation_widget_commonR.layout.recommendation_widget_steal_the_look_page
        private const val CLASS_NAME = "com.tokopedia.recommendation_widget_common.widget.steaalthelook.StealTheLookPageViewHolder"
    }

    private val binding = RecommendationWidgetStealTheLookPageBinding.bind(itemView)

    fun bind(
        model: StealTheLookPageModel
    ) {
        renderLeftGrid(model)
        renderTopRightGrid(model)
        renderBottomRightGrid(model)
    }

    private fun StealTheLookPageModel.getGridAtPos(gridPosition: RecommendationItem.GridPosition): RecommendationItem? {
        return recomItemList.firstOrNull { it.gridPosition == gridPosition }
    }

    private fun renderLeftGrid(model: StealTheLookPageModel) {
        val item = model.getGridAtPos(RecommendationItem.GridPosition.Left)
        val imageView = binding.stlItemLeftGrid.stlItemImage
        val ribbonArchView = binding.stlItemLeftGrid.stlItemRibbonArch
        val ribbonContentView = binding.stlItemLeftGrid.stlItemRibbonContent
        val ribbonTextView = binding.stlItemLeftGrid.stlItemRibbonText
        item?.renderGrid(imageView, ribbonArchView, ribbonContentView, ribbonTextView)
    }

    private fun renderTopRightGrid(model: StealTheLookPageModel) {
        val item = model.getGridAtPos(RecommendationItem.GridPosition.TopRight)
        val imageView = binding.stlItemTopRightGrid.stlItemImage
        val ribbonArchView = binding.stlItemTopRightGrid.stlItemRibbonArch
        val ribbonContentView = binding.stlItemTopRightGrid.stlItemRibbonContent
        val ribbonTextView = binding.stlItemTopRightGrid.stlItemRibbonText
        item?.renderGrid(imageView, ribbonArchView, ribbonContentView, ribbonTextView)
    }

    private fun renderBottomRightGrid(model: StealTheLookPageModel) {
        val item = model.getGridAtPos(RecommendationItem.GridPosition.BottomRight)
        val imageView = binding.stlItemBottomRightGrid.stlItemImage
        val ribbonArchView = binding.stlItemBottomRightGrid.stlItemRibbonArch
        val ribbonContentView = binding.stlItemBottomRightGrid.stlItemRibbonContent
        val ribbonTextView = binding.stlItemBottomRightGrid.stlItemRibbonText
        item?.renderGrid(imageView, ribbonArchView, ribbonContentView, ribbonTextView)
    }

    private fun RecommendationItem?.renderGrid(
        imageView: ImageView,
        ribbonArchView: View,
        ribbonContentView: View,
        ribbonTextView: Typography,
    ) {
        if(this == null) return
        imageUrl.renderImage(imageView)
        discountPercentage.renderRibbon(ribbonArchView, ribbonContentView, ribbonTextView)
    }

    private fun String.renderImage(
        imageView: ImageView
    ) {
        imageView.loadImage(this) {
            listener(
                onSuccess = { bitmap, mediaDataSource ->

                },
                onError = {

                }
            )
        }
    }

    private fun String.renderRibbon(
        ribbonArchView: View,
        ribbonContentView: View,
        ribbonTextView: Typography
    ) {
        if (isNotEmpty()) {
            ribbonArchView.show()
            ribbonContentView.show()
            ribbonTextView.show()
            ribbonTextView.text = this
        } else {
            ribbonArchView.hide()
            ribbonContentView.hide()
            ribbonTextView.hide()
        }
    }

    fun onViewRecycled() { }
}
