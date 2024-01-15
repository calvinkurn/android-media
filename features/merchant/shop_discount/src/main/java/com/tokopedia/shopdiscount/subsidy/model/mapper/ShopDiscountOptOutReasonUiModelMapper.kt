package com.tokopedia.shopdiscount.subsidy.model.mapper

import com.tokopedia.shopdiscount.subsidy.model.response.OptOutReasonListResponse
import com.tokopedia.shopdiscount.subsidy.model.uimodel.ShopDiscountOptOutReasonUiModel

object ShopDiscountOptOutReasonUiModelMapper {

    fun map(
        response: OptOutReasonListResponse,
    ): List<ShopDiscountOptOutReasonUiModel> {
        return response.listOptOutReason.map {
            ShopDiscountOptOutReasonUiModel(
                reason = it,
                isReasonFromResponse = true,
                isSelected = false
            )
        }
    }

}
