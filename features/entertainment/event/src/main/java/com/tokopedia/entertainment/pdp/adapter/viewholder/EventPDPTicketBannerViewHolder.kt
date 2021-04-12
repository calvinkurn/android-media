package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.data.EventPDPTicketBanner
import kotlinx.android.synthetic.main.item_event_pdp_parent_ticket_banner.view.*

class EventPDPTicketBannerViewHolder(view: View): AbstractViewHolder<EventPDPTicketBanner>(view) {

    override fun bind(element: EventPDPTicketBanner?) {
        itemView.tgEventTicketRecommendationTitle.text =
                getString(R.string.ent_event_pdp_ticket_recommendation_label)
    }

    companion object {
        val LAYOUT = R.layout.item_event_pdp_parent_ticket_banner

    }
}