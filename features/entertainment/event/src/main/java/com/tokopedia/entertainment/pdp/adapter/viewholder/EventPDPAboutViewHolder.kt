package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.text.Html
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.databinding.PartialEventPdpAboutBinding
import com.tokopedia.entertainment.pdp.data.pdp.EventPDPAboutEntity
import com.tokopedia.entertainment.pdp.listener.OnBindItemListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show

class EventPDPAboutViewHolder(val binding: PartialEventPdpAboutBinding, val onBindItemListener: OnBindItemListener) : AbstractViewHolder<EventPDPAboutEntity>(binding.root) {

    override fun bind(element: EventPDPAboutEntity) {
        with(binding) {
            if (element.isLoaded) {
                shimmering.root.gone()
                container.show()

                if (!element.sectionData.section.isNullOrEmpty()) {
                    tgEventPdpAboutTitle.text = element.sectionData.section
                } else {
                    tgEventPdpAboutTitle.gone()
                }

                if(!element.longDesc.isNullOrEmpty()) {
                    tgEventPdpAboutDesc.apply {
                        setText(Html.fromHtml(element.longDesc))
                    }
                } else {
                    tgEventPdpAboutDesc.gone()
                }

                if(!element.sectionData.content.isNullOrEmpty() && !element.sectionData.section.isNullOrEmpty()) {
                    btnEventPdpAboutSeeAll.setOnClickListener {
                        element.sectionData.content.firstOrNull()?.let {
                            onBindItemListener.seeAllAbout(it.valueText, element.sectionData.section)
                        }
                    }
                } else {
                    btnEventPdpAboutSeeAll.gone()
                }

            } else {
                container.gone()
                shimmering.root.show()
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.partial_event_pdp_about
    }
}
