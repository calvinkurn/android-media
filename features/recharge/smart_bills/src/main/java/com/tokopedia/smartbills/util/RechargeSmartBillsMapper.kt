package com.tokopedia.smartbills.util

import android.view.View
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.smartbills.data.Section

object RechargeSmartBillsMapper {

    fun addAccordionData(content: View, element: Section): AccordionDataUnify {
        val accordion = AccordionDataUnify(
                title = element.title,
                expandableView = content
        )

        accordion.setContentPadding(0,0,0,0)
        accordion.setBorder(false, false)

        return accordion
    }


    fun getNotAccordionSection(listSection: List<Section>): List<Section>{
        return listSection.filterNot { it.type == 1 }
    }
}