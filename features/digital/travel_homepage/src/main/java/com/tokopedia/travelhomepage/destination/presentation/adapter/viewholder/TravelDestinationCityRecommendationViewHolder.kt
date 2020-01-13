package com.tokopedia.travelhomepage.destination.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.destination.listener.OnClickListener
import com.tokopedia.travelhomepage.destination.listener.OnViewHolderBindListener
import com.tokopedia.travelhomepage.destination.model.TravelDestinationSectionViewModel
import com.tokopedia.travelhomepage.destination.presentation.adapter.TravelDestinationCityRecommendationAdapter
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemBindListener
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemClickListener
import kotlinx.android.synthetic.main.layout_travel_destination_recommendation.view.*

/**
 * @author by jessicasean on 02/02/2020
 */

class TravelDestinationCityRecommendationViewHolder(itemView: View, private val onViewHolderBindListener: OnViewHolderBindListener,
                                                    private val onClickListener: OnClickListener)
    : AbstractViewHolder<TravelDestinationSectionViewModel>(itemView) {

    lateinit var recommendationAdapter: TravelDestinationCityRecommendationAdapter

    override fun bind(element: TravelDestinationSectionViewModel) {
        if (element.isLoaded) {
            if (element.isSuccess && element.list.isNotEmpty()) {
                with(itemView) {
                    layout_content.show()
                    layout_shimmering.hide()
                    destination_recommendation_title.text = element.title
                    if (!::recommendationAdapter.isInitialized) {
                        recommendationAdapter = TravelDestinationCityRecommendationAdapter(element.list, onClickListener)
                        rv_destination_recommendation.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
                        rv_destination_recommendation.adapter = recommendationAdapter
                    } else {
                        recommendationAdapter.updateList(element.list)
                    }

                    (destination_recommendation_title as View).setOnSystemUiVisibilityChangeListener {  }
                }
            } else {
                itemView.hide()
            }
        } else {
            itemView.layout_content.hide()
            itemView.layout_shimmering.show()
            onViewHolderBindListener.onCityRecommendationVHBind()
        }
    }

    companion object {
        val LAYOUT = R.layout.layout_travel_destination_recommendation
    }
}