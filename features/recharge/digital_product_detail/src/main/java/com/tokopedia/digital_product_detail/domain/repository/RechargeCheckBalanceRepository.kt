package com.tokopedia.digital_product_detail.domain.repository

import com.tokopedia.digital_product_detail.data.model.data.DigitalPersoData
import com.tokopedia.digital_product_detail.domain.model.DigitalCheckBalanceOTPModel

interface RechargeCheckBalanceRepository {
    suspend fun getRechargeCheckBalanceOTP(
        clientNumbers: List<String>,
        dgCategoryIds: List<Int>,
        dgOperatorIds: List<Int>,
        channelName: String,
    ): DigitalCheckBalanceOTPModel
    suspend fun getRechargeCheckBalance(
        clientNumbers: List<String>,
        dgCategoryIds: List<Int>,
        dgOperatorIds: List<Int>,
        channelName: String,
    ): DigitalPersoData
}
