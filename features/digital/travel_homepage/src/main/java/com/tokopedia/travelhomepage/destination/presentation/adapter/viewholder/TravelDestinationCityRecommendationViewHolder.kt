package com.tokopedia.travelhomepage.destination.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.destination.listener.OnViewHolderBindListener
import com.tokopedia.travelhomepage.destination.model.TravelDestinationSectionViewModel
import com.tokopedia.travelhomepage.destination.presentation.adapter.TravelDestinationCityRecommendationAdapter
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemBindListener
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemClickListener
import kotlinx.android.synthetic.main.layout_travel_destination_recommendation.view.*

/**
 * @author by jessicasean on 02/02/2020
 */

class TravelDestinationCityRecommendationViewHolder(itemView: View, val onViewHolderBindListener: OnViewHolderBindListener)
    : AbstractViewHolder<TravelDestinationSectionViewModel>(itemView) {

    lateinit var recommendationAdapter: TravelDestinationCityRecommendationAdapter

    override fun bind(element: TravelDestinationSectionViewModel) {
        if (element.isLoaded) {
            if (element.list.isNotEmpty()) {
                with(itemView) {
                    destination_recommendation_title.text = element.title
                    if (!::recommendationAdapter.isInitialized) {
                        recommendationAdapter = TravelDestinationCityRecommendationAdapter(element.list)
                        rv_destination_recommendation.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
                        rv_destination_recommendation.adapter = recommendationAdapter
                    } else {
                        recommendationAdapter.updateList(element.list)
                    }
                }
            }
        } else {
            onViewHolderBindListener.onCityRecommendationVHBind()
        }
    }

    companion object {
        val LAYOUT = R.layout.layout_travel_destination_recommendation
    }
}