package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import androidx.annotation.LayoutRes
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.search.result.presentation.model.RecommendationTitleDataView
import com.tokopedia.search.R
import com.tokopedia.search.analytics.RecommendationTracking

class RecommendationTitleViewHolder(
        itemView: View
) :  AbstractViewHolder<RecommendationTitleDataView>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.recommendation_title_layout
    }

    private val title: TextView by lazy { itemView.findViewById<TextView>(R.id.title) }
    private val seeMore: TextView by lazy { itemView.findViewById<TextView>(R.id.see_more) }

    override fun bind(element: RecommendationTitleDataView?) {
        element?.let { viewModel ->
            title.text = viewModel.title
            seeMore.visibility = if(viewModel.seeMoreUrl.isNotEmpty()) View.VISIBLE else View.GONE
            seeMore.setOnClickListener {
                RecommendationTracking.eventUserClickSeeMore(viewModel.pageName)
                RouteManager.route(title.context, viewModel.seeMoreUrl)
            }
        }
    }


}