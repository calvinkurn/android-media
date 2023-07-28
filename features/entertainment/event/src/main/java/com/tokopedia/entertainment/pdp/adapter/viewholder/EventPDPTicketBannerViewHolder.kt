package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.databinding.ItemEventPdpParentTicketBannerBinding
import com.tokopedia.entertainment.pdp.data.EventPDPTicketBanner
import com.tokopedia.utils.view.binding.viewBinding

class EventPDPTicketBannerViewHolder(view: View): AbstractViewHolder<EventPDPTicketBanner>(view) {

    private val binding: ItemEventPdpParentTicketBannerBinding? by viewBinding()
    override fun bind(element: EventPDPTicketBanner?) {
        binding?.tgEventTicketRecommendationTitle?.text =
                getString(R.string.ent_event_pdp_ticket_recommendation_label)
        binding?.tgEventTicketRecommendationTitle?.background =
                binding?.root?.context?.getDrawable(com.tokopedia.unifyprinciples.R.color.Unify_NN0)
    }

    companion object {
        val LAYOUT = R.layout.item_event_pdp_parent_ticket_banner
    }
}
