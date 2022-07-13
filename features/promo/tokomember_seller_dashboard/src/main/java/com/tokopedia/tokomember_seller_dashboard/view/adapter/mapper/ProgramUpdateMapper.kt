package com.tokopedia.tokomember_seller_dashboard.view.adapter.mapper

import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.tokomember_common_widget.util.ProgramActionType
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.ProgramAttributesItem
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.ProgramUpdateDataInput
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TimeWindow
import com.tokopedia.tokomember_seller_dashboard.model.MembershipGetProgramForm
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.addDuration
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.convertDuration

object ProgramUpdateMapper {

    fun formToUpdateMapper(
        membershipGetProgramForm: MembershipGetProgramForm?,
        programType: Int,
        periodInMonth: Int,
        cardIdCreate:Int = 0,
        inToolsCardId:Int = 0,
    ): ProgramUpdateDataInput {
        var actionType = ""
        val startTime = if(programType == ProgramActionType.CANCEL){
            membershipGetProgramForm?.programForm?.timeWindow?.startTime
        }
        else{
            convertDuration(membershipGetProgramForm?.programForm?.timeWindow?.startTime ?: "")
        }
        val endTime = if(programType == ProgramActionType.CANCEL){
            membershipGetProgramForm?.programForm?.timeWindow?.endTime
        } else if (programType == ProgramActionType.EXTEND){
            addDuration(membershipGetProgramForm?.programForm?.timeWindow?.endTime ?: "", periodInMonth)
        }
        else{
            addDuration(membershipGetProgramForm?.programForm?.timeWindow?.startTime ?: "", periodInMonth)
        }
        val timeWindow = TimeWindow(
            id = 0,
            startTime = startTime,
            endTime = endTime,
            periodInMonth = periodInMonth,
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

        val programName = if (membershipGetProgramForm?.programForm?.name?.isEmpty() == true) {
            "Program Name"
        } else {
            membershipGetProgramForm?.programForm?.name
        }
        val programUpdateResponse = ProgramUpdateDataInput(
            id = membershipGetProgramForm?.programForm?.id.toIntSafely(),
            cardID = membershipGetProgramForm?.programForm?.cardID,
            name = programName,
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
                    this.cardID = inToolsCardId
                }
            }
            ProgramActionType.CREATE_BUAT ->{
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
            ProgramActionType.CREATE_FROM_COUPON ->{
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
                    this.timeWindow?.periodInMonth = null
                    this.tierLevels = listOf()
                    this.programAttributes = listOf()

                }
            }
        }
        return programUpdateResponse
    }

}