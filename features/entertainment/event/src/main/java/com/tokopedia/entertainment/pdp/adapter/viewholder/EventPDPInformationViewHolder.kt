package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.databinding.PartialEventPdpInformationBinding
import com.tokopedia.entertainment.pdp.data.ValueAccordion
import com.tokopedia.entertainment.pdp.data.pdp.EventPDPInformationEntity
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography

class EventPDPInformationViewHolder(val binding: PartialEventPdpInformationBinding) :
        AbstractViewHolder<EventPDPInformationEntity>(binding.root) {

    override fun bind(element: EventPDPInformationEntity) {
        with(binding) {
            if (element.isLoaded) {
                container.show()
                shimmering.root.gone()

                if (!element.sectionData.content.isNullOrEmpty()) {
                    if (accordionEventPdpInformation.accordionData.isEmpty()) {
                        if (!element.sectionData.content[Int.ZERO].valueAccordion.isNullOrEmpty()) {
                            element.sectionData.content[Int.ZERO].valueAccordion.forEach {
                                accordionEventPdpInformation.addGroup(
                                        mapValueAccordionToAccordionData(root.context, it)
                                )
                            }
                        } else {
                            accordionEventPdpInformation.gone()
                        }
                    }
                } else {
                    container.gone()
                    accordionEventPdpInformation.gone()
                }
            } else {
                container.gone()
                shimmering.root.show()
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
