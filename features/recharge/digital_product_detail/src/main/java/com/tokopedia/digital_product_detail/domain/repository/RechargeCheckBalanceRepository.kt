package com.tokopedia.digital_product_detail.domain.repository

import com.tokopedia.digital_product_detail.domain.model.DigitalCheckBalanceModel

interface RechargeCheckBalanceRepository {
    suspend fun getRechargeCheckBalance(
        clientNumbers: List<String>,
        dgCategoryIds: List<Int>,
        dgOperatorIds: List<Int>,
        channelName: String,
    ): DigitalCheckBalanceModel
}
