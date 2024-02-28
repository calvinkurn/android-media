package com.tokopedia.shopdiscount.subsidy.model.mapper

import com.tokopedia.shopdiscount.subsidy.model.response.SubsidyEngineGetSellerOutReasonListResponse
import com.tokopedia.shopdiscount.subsidy.model.uimodel.ShopDiscountOptOutReasonUiModel

object ShopDiscountOptOutReasonUiModelMapper {

    fun map(
        response: SubsidyEngineGetSellerOutReasonListResponse,
    ): List<ShopDiscountOptOutReasonUiModel> {
        return response.subsidyEngineGetSellerOutReasonList.listReasonOption.map {
            ShopDiscountOptOutReasonUiModel(
                reason = it.reason,
                isReasonFromResponse = true,
                isSelected = false
            )
        }
    }

}
