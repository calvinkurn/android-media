package com.tokopedia.shopdiscount.subsidy.model.mapper

import com.tokopedia.shopdiscount.subsidy.model.request.DoOptOutSubsidyRequest

object DoOptOutSubsidyRequestMapper {

    fun map(
        listPromotionId: List<String>,
        listReasonOptOut: List<String>
    ): DoOptOutSubsidyRequest {
        return DoOptOutSubsidyRequest(
            requestHeader = DoOptOutSubsidyRequest.RequestHeader(
                usecase = DoOptOutSubsidyRequest.RequestHeader.USE_CASE_SLASH_PRICE
            ),
            listSubsidyPromotion = listPromotionId,
            listReasonOutProgram = listReasonOptOut
        )
    }

}
