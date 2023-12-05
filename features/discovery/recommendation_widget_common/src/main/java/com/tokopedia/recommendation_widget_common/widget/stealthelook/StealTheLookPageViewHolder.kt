package com.tokopedia.recommendation_widget_common.widget.stealthelook

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recommendation_widget_common.R as recommendation_widget_commonR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.recommendation_widget_common.databinding.RecommendationWidgetStealTheLookPageBinding
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.stealthelook.tracking.StealTheLookTracking
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

    fun bind(model: StealTheLookStyleModel) {
        renderLeftGrid(model)
        renderTopRightGrid(model)
        renderBottomRightGrid(model)
        sendViewportImpression(model)
    }

    private fun StealTheLookStyleModel.getGridAtPos(gridPosition: RecommendationItem.GridPosition): StealTheLookGridModel? {
        return gridPositionMap[gridPosition]
    }

    private fun renderLeftGrid(model: StealTheLookStyleModel) {
        val grid = model.getGridAtPos(RecommendationItem.GridPosition.Left)
        val imageView = binding.stlItemLeftGrid.stlItemImage
        val ribbonArchView = binding.stlItemLeftGrid.stlItemRibbonArch
        val ribbonContentView = binding.stlItemLeftGrid.stlItemRibbonContent
        val ribbonTextView = binding.stlItemLeftGrid.stlItemRibbonText
        val reloadView = binding.stlItemLeftGrid.stlReload.root
        renderGrid(grid, imageView, ribbonArchView, ribbonContentView, ribbonTextView, reloadView)
        imageView.setGridClickListener(grid)
    }

    private fun renderTopRightGrid(model: StealTheLookStyleModel) {
        val grid = model.getGridAtPos(RecommendationItem.GridPosition.TopRight)
        val imageView = binding.stlItemTopRightGrid.stlItemImage
        val ribbonArchView = binding.stlItemTopRightGrid.stlItemRibbonArch
        val ribbonContentView = binding.stlItemTopRightGrid.stlItemRibbonContent
        val ribbonTextView = binding.stlItemTopRightGrid.stlItemRibbonText
        val reloadView = binding.stlItemTopRightGrid.stlReload.root
        renderGrid(grid, imageView, ribbonArchView, ribbonContentView, ribbonTextView, reloadView)
        imageView.setGridClickListener(grid)
    }

    private fun renderBottomRightGrid(model: StealTheLookStyleModel) {
        val grid = model.getGridAtPos(RecommendationItem.GridPosition.BottomRight)
        val imageView = binding.stlItemBottomRightGrid.stlItemImage
        val ribbonArchView = binding.stlItemBottomRightGrid.stlItemRibbonArch
        val ribbonContentView = binding.stlItemBottomRightGrid.stlItemRibbonContent
        val ribbonTextView = binding.stlItemBottomRightGrid.stlItemRibbonText
        val reloadView = binding.stlItemBottomRightGrid.stlReload.root
        renderGrid(grid, imageView, ribbonArchView, ribbonContentView, ribbonTextView, reloadView)
        imageView.setGridClickListener(grid)
    }

    private fun renderGrid(
        model: StealTheLookGridModel?,
        imageView: ImageView,
        ribbonArchView: View,
        ribbonContentView: View,
        ribbonTextView: Typography,
        reloadView: ViewGroup,
    ) {
        val item = model?.recommendationItem
        renderImage(item?.imageUrl.orEmpty(), imageView, reloadView)
        renderRibbon(item?.discountPercentage.orEmpty(), ribbonArchView, ribbonContentView, ribbonTextView)
    }

    private fun renderImage(
        imageUrl: String,
        imageView: ImageView,
        reloadView: ViewGroup,
    ) {
        imageView.loadImage(imageUrl) {
            listener(
                onSuccess = { bitmap, mediaDataSource ->
                    reloadView.hide()
                },
                onError = {
                    imageView.loadImage(
                        ContextCompat.getDrawable(
                            imageView.context,
                            unifyprinciplesR.color.Unify_NN100
                        )
                    )
                    if(imageUrl.isEmpty()) {
                        reloadView.hide()
                    } else {
                        reloadView.show()
                        reloadView.setOnClickListener {
                            reloadView.hide()
                            renderImage(imageUrl, imageView, reloadView)
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

    private fun View.setGridClickListener(model: StealTheLookGridModel?) {
        if(model == null) return
        if(model.recommendationItem.appUrl.isBlank()) return

        setOnClickListener {
            model.tracking?.sendEventItemClick(model)
        }
    }

    private fun sendViewportImpression(model: StealTheLookStyleModel) {
        binding.root.addOnImpressionListener(model) {
            model.tracking?.sendEventViewportImpression(trackingQueue, model)
        }
    }

    fun onViewRecycled() { }
}
