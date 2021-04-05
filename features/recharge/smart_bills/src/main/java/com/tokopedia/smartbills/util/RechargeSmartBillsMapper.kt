package com.tokopedia.smartbills.util

import android.view.View
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.smartbills.data.Section
import com.tokopedia.smartbills.presentation.fragment.SmartBillsFragment.Companion.MAIN_TYPE

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


    fun getAccordionSection(listSection: List<Section>): List<Section>{
        return listSection.filterNot { it.type == MAIN_TYPE }.sortedByDescending { it.type }
                .mapIndexed { index, it ->
                    Section(title = it.title, type = it.type,
                    text = it.text, bills = it.bills, positionAccordion = index)
        }
    }

    fun getNotAccordionSection(listSection: List<Section>): Section?{
        val listMainSection = listSection.filter { it.type == MAIN_TYPE }
        return listMainSection.firstOrNull()
    }


}