package com.tokopedia.shopdiscount.subsidy.model.mapper

import com.tokopedia.shopdiscount.subsidy.model.request.SubsidyEngineGetSellerOutReasonListRequest

object SubsidyEngineGetSellerOutReasonListRequestMapper {

    fun map(userId: String): SubsidyEngineGetSellerOutReasonListRequest {
        return SubsidyEngineGetSellerOutReasonListRequest(
            requestHeader = SubsidyEngineGetSellerOutReasonListRequest.RequestHeader(
                usecase = SubsidyEngineGetSellerOutReasonListRequest.RequestHeader.USE_CASE_SLASH_PRICE
            ),
            userId = userId
        )
    }

}
