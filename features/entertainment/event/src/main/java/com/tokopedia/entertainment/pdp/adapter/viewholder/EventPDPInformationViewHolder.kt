package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.data.ValueAccordion
import com.tokopedia.entertainment.pdp.data.pdp.EventPDPInformationEntity
import com.tokopedia.entertainment.pdp.listener.OnBindItemListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.partial_event_pdp_information.view.*

class EventPDPInformationViewHolder(view: View, val onBindItemListener: OnBindItemListener) :
        AbstractViewHolder<EventPDPInformationEntity>(view) {

    override fun bind(element: EventPDPInformationEntity) {
        with(itemView) {
            if (element.isLoaded) {
                container.show()
                shimmering.gone()

                if (!element.sectionData.content.isNullOrEmpty()) {
                    if (accordion_event_pdp_information.accordionData.isEmpty()) {
                        if (!element.sectionData.content[0].valueAccordion.isNullOrEmpty()) {
                            element.sectionData.content[0].valueAccordion.forEach {
                                accordion_event_pdp_information.addGroup(
                                        mapValueAccordionToAccordionData(context, it)
                                )
                            }
                        } else {
                            accordion_event_pdp_information.gone()
                        }
                    }
                } else {
                    container.gone()
                    accordion_event_pdp_information.gone()
                }
            } else {
                container.gone()
                shimmering.show()
            }
        }
    }

    private fun mapValueAccordionToAccordionData(context: Context, value: ValueAccordion): AccordionDataUnify {
        val content = Typography(context)
        content.apply {
            text = value.content
            setType(Typography.BODY_3)
            setWeight(Typography.REGULAR)
        }
        return AccordionDataUnify(
                subtitle = value.title,
                expandableView = content
        ).apply {
            borderBottom = false
            borderTop = false
        }
    }

    companion object {
        val LAYOUT = R.layout.partial_event_pdp_information
    }
}