package com.tokopedia.tokomember_seller_dashboard.view.adapter.mapper

import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.tokomember_common_widget.util.ProgramActionType
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.ProgramAttributesItem
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.ProgramUpdateDataInput
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TimeWindow
import com.tokopedia.tokomember_seller_dashboard.model.MembershipGetProgramForm
import com.tokopedia.tokomember_seller_dashboard.util.DateUtil.addDuration
import com.tokopedia.tokomember_seller_dashboard.util.DateUtil.convertDuration

object ProgramUpdateMapper {

    fun formToUpdateMapper(
        membershipGetProgramForm: MembershipGetProgramForm?,
        programType: Int,
        periodInMonth: Int,
        cardIdCreate:Int = 0
    ): ProgramUpdateDataInput {
        var actionType = ""
        val timeWindow = TimeWindow(
            id = 0,
            startTime = convertDuration(
                membershipGetProgramForm?.programForm?.timeWindow?.startTime ?: ""
            ),
            endTime = addDuration(membershipGetProgramForm?.programForm?.timeWindow?.startTime ?: "", periodInMonth),
            periodInMonth = periodInMonth
        )
        val programAttributes = membershipGetProgramForm?.programForm?.programAttributes
        programAttributes.apply {
            this?.forEach {
                it?.isUseMultiplier = true
                it?.multiplierRates = 1
                it?.minimumTransaction = 50000
            }
        }
        val tierLevels = membershipGetProgramForm?.programForm?.tierLevels
        tierLevels?.apply {
            this.forEach{
                it?.metadata = "metadata"
            }
            this.getOrNull(0)?.name = "Premium"
            this.getOrNull(1)?.name = "VIP"
        }
        val programUpdateResponse = ProgramUpdateDataInput(
            id = membershipGetProgramForm?.programForm?.id.toIntSafely(),
            cardID = membershipGetProgramForm?.programForm?.cardID,
            name = membershipGetProgramForm?.programForm?.name,
            tierLevels = tierLevels,
            programAttributes = programAttributes,
            actionType = actionType,
            apiVersion = "3.0",
            timeWindow = timeWindow
        )
        when(programType){
            ProgramActionType.CREATE ->{
                timeWindow.id = 0
                actionType = "create"
                tierLevels.apply {
                    this?.forEach {
                        it?.id = 0
                    }
                }
                val programAttributeListItem = ProgramAttributesItem(
                    isUseMultiplier = true,
                    multiplierRates = 1,
                    minimumTransaction = 50000,
                    id = 0,
                    programID = 0,
                    tierLevelID = 0,
                )
                programUpdateResponse.apply {
                    this.tierLevels = tierLevels
                    this.programAttributes = listOf(programAttributeListItem,programAttributeListItem)
                    this.actionType = actionType
                    this.cardID = cardIdCreate
                }
            }
            ProgramActionType.EXTEND ->{
                actionType = "extend"
                programUpdateResponse.apply {
                    this.actionType = actionType
                }
            }
            ProgramActionType.EDIT ->{
                actionType = "edit"
                programUpdateResponse.apply {
                    this.actionType = actionType
                }
            }
            ProgramActionType.CANCEL ->{
                actionType = "cancel"
                programUpdateResponse.apply {
                    this.actionType = actionType
                }
            }
        }
        return programUpdateResponse
    }

}