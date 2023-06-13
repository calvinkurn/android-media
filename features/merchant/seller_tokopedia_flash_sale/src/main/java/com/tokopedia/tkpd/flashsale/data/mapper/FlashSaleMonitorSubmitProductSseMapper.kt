package com.tokopedia.tkpd.flashsale.data.mapper

import com.google.gson.Gson
import com.tokopedia.tkpd.flashsale.data.response.FlashSaleMonitorSubmitProductSseResponse
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleProductSubmissionSseResult
import javax.inject.Inject

class FlashSaleMonitorSubmitProductSseMapper @Inject constructor() {

    fun map(message: String): FlashSaleProductSubmissionSseResult? {
        return convertToModel(message, FlashSaleMonitorSubmitProductSseResponse::class.java)?.let {
            FlashSaleProductSubmissionSseResult(
                it.campaignId,
                it.status,
                it.countProcessedProduct,
                it.countAllProduct,
                it.campaignId
            )
        }
    }

    private fun <T> convertToModel(message: String, classOfT: Class<T>): T? {
        return try {
            Gson().fromJson(message, classOfT)
        } catch (e: Exception) {
            null
        }
    }
}
