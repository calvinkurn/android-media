package com.tokopedia.recommendation_widget_common.widget.comparison.compareditem

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonListModel
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonWidgetInterface
import com.tokopedia.recommendation_widget_common.widget.comparison.RecommendationTrackingModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface

class ComparedItemAdapter(
    var comparisonListModel: ComparisonListModel,
    val comparisonWidgetInterface: ComparisonWidgetInterface,
    val trackingQueue: TrackingQueue?,
    val recommendationTrackingModel: RecommendationTrackingModel,
    val userSessionInterface: UserSessionInterface
): RecyclerView.Adapter<ComparisonWidgetComparedItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComparisonWidgetComparedItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comparison_compared_widget, parent, false)

        val productCardView = view.findViewById<ProductCardGridView>(R.id.productCardView)
        val layoutParams = productCardView.layoutParams
        layoutParams.height = comparisonListModel.comparisonWidgetConfig.productCardHeight
        productCardView.layoutParams = layoutParams

        productCardView.applyCarousel()

        return ComparisonWidgetComparedItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return comparisonListModel.comparisonData.size
    }

    //viewType == position
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ComparisonWidgetComparedItemViewHolder, position: Int) {
        if (position < comparisonListModel.comparisonData.size) {
            holder.bind(
                comparisonListModel.comparisonData[position],
                comparisonListModel,
                comparisonWidgetInterface,
                recommendationTrackingModel,
                trackingQueue,
                userSessionInterface,
                position
            )
        }
    }
}