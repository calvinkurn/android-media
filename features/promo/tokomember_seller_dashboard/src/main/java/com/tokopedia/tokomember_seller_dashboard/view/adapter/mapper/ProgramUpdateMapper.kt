package com.tokopedia.tokomember_seller_dashboard.view.adapter.mapper

import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.tokomember_common_widget.util.ProgramActionType
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.ProgramUpdateDataInput
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TimeWindow
import com.tokopedia.tokomember_seller_dashboard.model.MembershipGetProgramForm
import com.tokopedia.utils.date.toDate
import java.text.SimpleDateFormat
import java.util.*

object ProgramUpdateMapper {

    fun formToUpdateMapper(
        membershipGetProgramForm: MembershipGetProgramForm?,
        programType: Int,
        periodInMonth: Int
    ): ProgramUpdateDataInput {
        var actionType = ""
        val timeWindow = TimeWindow(id = 0, startTime = membershipGetProgramForm?.programForm?.timeWindow?.startTime, endTime = membershipGetProgramForm?.programForm?.timeWindow?.endTime, periodInMonth = periodInMonth)
        val programAttributes = membershipGetProgramForm?.programForm?.programAttributes
        programAttributes.apply {
            this?.forEach {
                it?.isUseMultiplier = true
                it?.multiplierRates = 1
                it?.minimumTransaction = 5000
            }
        }
        val tierLevels = membershipGetProgramForm?.programForm?.tierLevels
        tierLevels?.apply {
            this.forEach{
                it?.metadata = "metadata"
            }
        }
        var programUpdateResponse = ProgramUpdateDataInput(
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
                programAttributes.apply {
                    this?.forEach {
                        it?.id = 0
                        it?.programID = 0
                        it?.tierLevelID = 0
                    }
                }
                programUpdateResponse.apply {
                    this.tierLevels = tierLevels
                    this.programAttributes = programAttributes
                    this.actionType = actionType
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

    fun setDate(time: String): String {
        val selectedTime = time.substringBefore(" ")
        val date = selectedTime.toDate("yyyy-MM-dd")
        val day = SimpleDateFormat("dd").format(date)
        val month = SimpleDateFormat("MMMM").format(date)
        val year = selectedTime.substringBefore("-")
        return "$day $month $year"
    }

    fun setTime(time: String): String {
        val selectedTime  = time.substringAfter(" ").substringBefore(" ").substringBeforeLast(":")
        return "$selectedTime WIB"
    }

    fun convertDateTime(t: Date): String {
        var time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").format(t)
        time = time.substring(0, time.length-2)
        return time
    }

}