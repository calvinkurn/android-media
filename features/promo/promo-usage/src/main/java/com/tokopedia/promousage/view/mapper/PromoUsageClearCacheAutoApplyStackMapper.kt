package com.tokopedia.promousage.view.mapper

import com.tokopedia.purchase_platform.common.feature.promo.data.response.clearpromo.ClearCacheAutoApplyStackResponse
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.SuccessDataUiModel
import javax.inject.Inject

internal class PromoUsageClearCacheAutoApplyStackMapper @Inject constructor() {

    fun mapClearCacheAutoApplyResponse(
        response: ClearCacheAutoApplyStackResponse
    ): ClearPromoUiModel {
        return ClearPromoUiModel(
            successDataModel = SuccessDataUiModel(
                success = response.successData.success,
                tickerMessage = response.successData.tickerMessage,
                defaultEmptyPromoMessage = response.successData.defaultEmptyPromoMessage
            )
        )
    }
}
