package com.tokopedia.recommendation_widget_common.widget.comparison

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.listener.AdsItemClickListener
import com.tokopedia.recommendation_widget_common.listener.AdsViewListener
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface

class ComparisonWidgetAdapter(
    var comparisonListModel: ComparisonListModel,
    val comparisonWidgetInterface: ComparisonWidgetInterface,
    val adsViewListener: AdsViewListener?,
    val adsItemClickListener: AdsItemClickListener?,
    val trackingQueue: TrackingQueue?,
    val recommendationTrackingModel: RecommendationTrackingModel,
    val userSessionInterface: UserSessionInterface,
    val shouldUseReimagineCard: Boolean
) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (shouldUseReimagineCard) {
            val view = onCreateView(parent, R.layout.item_comparison_reimagine_widget)
            ComparisonReimagineWidgetItemViewHolder(view, adsViewListener, adsItemClickListener)
        } else {
            val view = onCreateView(parent, R.layout.item_comparison_widget)
            ComparisonWidgetItemViewHolder(view, adsViewListener, adsItemClickListener,)
        }
    }

    override fun getItemCount(): Int {
        // for sticky product on the left
        return if (comparisonListModel.comparisonData.isNotEmpty()) 1 else 0
    }

    //viewType == position
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < comparisonListModel.comparisonData.size) {
            if (shouldUseReimagineCard) {
                bind(holder as ComparisonReimagineWidgetItemViewHolder, position)
            } else {
                bind(holder as ComparisonWidgetItemViewHolder, position)
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

    private fun bind(holder: ComparisonViewHolder, position: Int) {
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
