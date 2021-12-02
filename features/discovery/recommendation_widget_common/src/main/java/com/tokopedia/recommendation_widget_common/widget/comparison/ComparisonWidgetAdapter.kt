package com.tokopedia.recommendation_widget_common.widget.comparison

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface

class ComparisonWidgetAdapter(
        var comparisonListModel: ComparisonListModel,
        val comparisonWidgetInterface: ComparisonWidgetInterface,
        val trackingQueue: TrackingQueue?,
        val recommendationTrackingModel: RecommendationTrackingModel,
        val userSessionInterface: UserSessionInterface
): RecyclerView.Adapter<ComparisonWidgetItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComparisonWidgetItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comparison_widget, parent, false)

        val productCardView = view.findViewById<ProductCardGridView>(R.id.productCardView)
        val layoutParams = productCardView.layoutParams
        layoutParams.height = comparisonListModel.comparisonWidgetConfig.productCardHeight
        productCardView.layoutParams = layoutParams

        productCardView.applyCarousel()

        return ComparisonWidgetItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        // for sticky product on the left
        return if(comparisonListModel.comparisonData.isNotEmpty()) 1 else 0
    }

    //viewType == position
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ComparisonWidgetItemViewHolder, position: Int) {
        if (position < comparisonListModel.comparisonData.size) {
            holder.bind(
                    comparisonListModel.comparisonData[position],
                    comparisonListModel,
                    comparisonWidgetInterface,
                    recommendationTrackingModel,
                    trackingQueue,
                    userSessionInterface
            )
        }
    }
}