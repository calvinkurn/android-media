package com.tokopedia.travelhomepage.destination.presentation.adapter.viewholder

import android.text.Html
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.destination.listener.OnViewHolderBindListener
import com.tokopedia.travelhomepage.destination.model.TravelDestinationSummaryModel
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemBindListener
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemClickListener
import kotlinx.android.synthetic.main.layout_travel_destination_summary.view.*

/**
 * @author by jessica on 2020-01-02
 */

class TravelDestinationSummaryViewHolder(itemView: View, val onViewHolderBindListener: OnViewHolderBindListener)
    : AbstractViewHolder<TravelDestinationSummaryModel>(itemView) {

    override fun bind(element: TravelDestinationSummaryModel) {
        if (element.isLoaded) {
            with(itemView) {
                destination_summary_title.text = Html.fromHtml(element.title)
                destination_summary_description.text = element.description
            }
        } else {
            onViewHolderBindListener.onCitySummaryVHBind()
        }
    }

    companion object {
        val LAYOUT = R.layout.layout_travel_destination_summary
    }

}