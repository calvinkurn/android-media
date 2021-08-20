package com.tokopedia.home_recom.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.model.datamodel.RecommendationCPMDataModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.topads.sdk.widget.TopAdsHeadlineView

/**
 * Created by yfsx on 18/08/21.
 */
class RecommendationCPMViewHolder(val view: View, val listener: RecommendationListener)
    : AbstractViewHolder<RecommendationCPMDataModel>(view) {

    var topadsCPM: TopAdsHeadlineView? = null

    companion object {
        val LAYOUT = R.layout.item_recommendation_cpm
    }

    override fun bind(element: RecommendationCPMDataModel) {
        bindView()
        bindItem(element)
    }

    override fun bind(element: RecommendationCPMDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun bindView() {
        topadsCPM = itemView.findViewById(R.id.item_topads_cpm)
    }

    private fun bindItem(element: RecommendationCPMDataModel) {
        topadsCPM?.displayAds(element.topAdsHeadlineResponse.displayAds)
        topadsCPM?.hideShimmerView()
    }
}