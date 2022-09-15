package com.tokopedia.sellerorder.detail.presentation.mapper

import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.detail.data.model.GetResolutionTicketStatusResponse
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.data.model.SomDetailResolution

object SomResolutionResponseMapper {

    fun mapResponseToResolutionUIModel(data: GetResolutionTicketStatusResponse.ResolutionGetTicketStatus.ResolutionData): SomDetailData? {
        return try {
            val reso = SomDetailResolution(
                title = data.cardTitle!!,
                status = data.resolutionStatus!!.text!!,
                description = data.description!!,
                picture = data.profilePicture!!,
                showDeadline = data.deadline!!.showDeadline!!,
                deadlineDateTime = data.deadline.datetime!!,
                backgroundColor = data.deadline.backgroundColor!!,
                redirectPath = data.redirectPath!!.android!!,
                resolutionStatusFontColor = data.resolutionStatus.fontColor!!
            )
            SomDetailData(reso, typeLayout = SomConsts.DETAIL_RESO_TYPE)
        } catch (e: NullPointerException) {
            null
        }
    }

}