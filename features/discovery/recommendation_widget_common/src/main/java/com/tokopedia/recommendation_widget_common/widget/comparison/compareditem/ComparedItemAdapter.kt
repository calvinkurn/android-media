package com.tokopedia.recommendation_widget_common.widget.comparison.compareditem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.listener.AdsItemClickListener
import com.tokopedia.recommendation_widget_common.listener.AdsViewListener
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonListModel
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonViewHolder
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonWidgetInterface
import com.tokopedia.recommendation_widget_common.widget.comparison.RecommendationTrackingModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface

class ComparedItemAdapter(
    var comparisonListModel: ComparisonListModel,
    val comparisonWidgetInterface: ComparisonWidgetInterface,
    val adsViewListener: AdsViewListener,
    val adsItemClickListener: AdsItemClickListener,
    val trackingQueue: TrackingQueue?,
    val recommendationTrackingModel: RecommendationTrackingModel,
    val userSessionInterface: UserSessionInterface,
    val shouldUseReimagineCard: Boolean
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (shouldUseReimagineCard) {
            val view = onCreateView(parent, R.layout.item_comparison_reimagine_compared_widget)
            ComparisonReimagineWidgetComparedItemViewHolder(view, adsViewListener, adsItemClickListener)
        } else {
            val view = onCreateView(parent, R.layout.item_comparison_compared_widget)
            ComparisonWidgetComparedItemViewHolder(view, adsViewListener, adsItemClickListener)
        }
    }

    override fun getItemCount(): Int {
        return comparisonListModel.comparisonData.size - 1
    }

    //viewType == position
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val nonAnchorPosition = position + 1
        if (nonAnchorPosition < comparisonListModel.comparisonData.size) {
            if (shouldUseReimagineCard) {
                bind(holder as ComparisonReimagineWidgetComparedItemViewHolder, nonAnchorPosition)
            } else {
                bind(holder as ComparisonWidgetComparedItemViewHolder, nonAnchorPosition)
            }
        }
    }

    private fun onCreateView(parent: ViewGroup, @LayoutRes layoutId: Int): View {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)

        val productCardView = view.findViewById<ProductCardGridView>(R.id.productCardView)

        val layoutParams = productCardView.layoutParams
        layoutParams.height = comparisonListModel.comparisonWidgetConfig.productCardHeight
        productCardView.layoutParams = layoutParams

        productCardView.applyCarousel()

        return view
    }

    private fun bind(holder: ComparisonViewHolder, nonAnchorPosition: Int) {
        holder.bind(
            comparisonListModel.comparisonData[nonAnchorPosition],
            comparisonListModel,
            comparisonWidgetInterface,
            recommendationTrackingModel,
            trackingQueue,
            userSessionInterface,
        )
    }
}
