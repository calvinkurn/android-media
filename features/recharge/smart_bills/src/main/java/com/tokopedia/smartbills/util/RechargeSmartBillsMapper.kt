package com.tokopedia.smartbills.util

import android.view.View
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.smartbills.data.Section

object RechargeSmartBillsMapper {

    fun addAccordionData(content: View, element: Section): AccordionDataUnify {
        return AccordionDataUnify(
                title = element.title,
                expandableView = content
        ).apply {
            borderBottom = false
            borderTop = false
            contentPaddingLeft = 0
        }
    }


    fun getNotAccordionSection(listSection: List<Section>): List<Section>{
        return listSection.filterNot { it.type == 1 }
    }
}