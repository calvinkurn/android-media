package com.tokopedia.recommendation_widget_common.widget.comparison2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonListModel
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonWidgetInterface
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonWidgetItemViewHolder
import com.tokopedia.recommendation_widget_common.widget.comparison.RecommendationTrackingModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.item_comparison_widget.view.*

class ComparedItemAdapter(
    var comparisonListModel: ComparisonListModel,
    val comparisonWidgetInterface: ComparisonWidgetInterface,
    val trackingQueue: TrackingQueue?,
    val recommendationTrackingModel: RecommendationTrackingModel,
    val userSessionInterface: UserSessionInterface,
    private val isComparedItem: Boolean = false
): RecyclerView.Adapter<ComparisonWidget2ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComparisonWidget2ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comparison_widget2, parent, false)
        val productCardView = view.productCardView
        val layoutParams = productCardView.layoutParams
        layoutParams.height = comparisonListModel.comparisonWidgetConfig.productCardHeight
        productCardView.layoutParams = layoutParams

        view.productCardView.applyCarousel()

        return ComparisonWidget2ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if(isComparedItem) 1 else comparisonListModel.comparisonData.size
    }

    //viewType == position
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ComparisonWidget2ItemViewHolder, position: Int) {
        if (position < comparisonListModel.comparisonData.size) {
            holder.bind(
                comparisonListModel.comparisonData[position],
                comparisonListModel,
                comparisonWidgetInterface,
                recommendationTrackingModel,
                trackingQueue,
                userSessionInterface,
                position,
                isComparedItem
            )
        }
    }
}