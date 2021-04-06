package com.tokopedia.smartbills.util

import android.view.View
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.smartbills.data.RechargeBills
import com.tokopedia.smartbills.data.RechargeListSmartBills
import com.tokopedia.smartbills.data.RechargeMultipleSBMBill
import com.tokopedia.smartbills.data.Section
import com.tokopedia.smartbills.presentation.fragment.SmartBillsFragment.Companion.ACTION_TYPE
import com.tokopedia.smartbills.presentation.fragment.SmartBillsFragment.Companion.MAIN_TYPE
import com.tokopedia.smartbills.presentation.fragment.SmartBillsFragment.Companion.PAID_TYPE

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

    fun getUUIDAction(listSection: List<Section>): List<String> {
        val section = listSection.filter { it.type == ACTION_TYPE }.firstOrNull()
        return if (section == null) listOf() else mapUUIDAction(section)
    }

    fun mapUUIDAction(section: Section): List<String> {
        return section.bills.map { it.uuid }
    }

    fun mapActiontoStatement(action: RechargeMultipleSBMBill, statement: RechargeListSmartBills): RechargeListSmartBills {
        val resultStateMent = RechargeListSmartBills(
                    statement.userID, statement.total, statement.totalText, statement.month,
                    statement.monthText, statement.dateRangeText, statement.isOngoing, statement.summaries,
                    mapActionSectiontoStatementSection(action, statement.sections)
            )

        return resultStateMent
    }

    fun mapActionSectiontoStatementSection(action: RechargeMultipleSBMBill, listSection: List<Section>): ArrayList<Section> {
        val sections = arrayListOf<Section>()
        sections.add(mapSections(MAIN_TYPE, listSection.filter { it.type == MAIN_TYPE }.firstOrNull(), action) ?: Section())
        sections.add(mapSections(ACTION_TYPE, listSection.filter { it.type == ACTION_TYPE }.firstOrNull(), action)  ?: Section())
        sections.add(mapSections(PAID_TYPE, listSection.filter { it.type == PAID_TYPE }.firstOrNull(), action)  ?: Section())
        return sections
    }

    fun mapSections(type:Int, statementSection: Section?, action: RechargeMultipleSBMBill): Section? {
      val section =  statementSection?.let {
           val bills: ArrayList<RechargeBills> = arrayListOf()
           bills.addAll(it.bills)
           action.bills.forEach {
               if (it.section.type == type) bills.add(it)
           }
           Section(
                   it.title,
                   it.type,
                   it.text,
                   bills,
                   it.positionAccordion
           )
       }

        return section
    }
}