package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.text.Html
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.data.pdp.EventPDPAboutEntity
import com.tokopedia.entertainment.pdp.listener.OnBindItemListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.partial_event_pdp_about.view.*

class EventPDPAboutViewHolder(view: View, val onBindItemListener: OnBindItemListener) : AbstractViewHolder<EventPDPAboutEntity>(view) {

    override fun bind(element: EventPDPAboutEntity) {
        with(itemView) {
            if (element.isLoaded) {
                shimmering.gone()
                container.show()

                if (!element.sectionData.section.isNullOrEmpty()) {
                    tg_event_pdp_about_title.text = element.sectionData.section
                } else {
                    tg_event_pdp_about_title.gone()
                }

                if(!element.longDesc.isNullOrEmpty()) {
                    tg_event_pdp_about_desc.apply {
                        setText(Html.fromHtml(element.longDesc))
                    }
                } else {
                    tg_event_pdp_about_desc.gone()
                }

                if(!element.sectionData.content.isNullOrEmpty() && !element.sectionData.section.isNullOrEmpty()) {
                    btn_event_pdp_about_see_all.setOnClickListener {
                        element.sectionData.content.firstOrNull()?.let {
                            onBindItemListener.seeAllAbout(it.valueText, element.sectionData.section)
                        }
                    }
                } else {
                    btn_event_pdp_about_see_all.gone()
                }

            } else {
                container.gone()
                shimmering.show()
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.partial_event_pdp_about
    }
}