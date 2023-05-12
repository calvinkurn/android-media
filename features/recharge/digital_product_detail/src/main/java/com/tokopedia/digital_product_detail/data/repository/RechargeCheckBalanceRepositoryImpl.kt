package com.tokopedia.digital_product_detail.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.digital_product_detail.data.mapper.DigitalPersoMapper
import com.tokopedia.digital_product_detail.data.model.data.DigitalPersoData
import com.tokopedia.digital_product_detail.domain.model.DigitalCheckBalanceOTPModel
import com.tokopedia.digital_product_detail.domain.repository.RechargeCheckBalanceRepository
import com.tokopedia.digital_product_detail.domain.usecase.GetRechargeCheckBalanceOtpUseCase
import com.tokopedia.digital_product_detail.domain.usecase.GetRechargeCheckBalanceUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeCheckBalanceRepositoryImpl @Inject constructor(
    private val getRechargeCheckBalanceOtpUseCase: GetRechargeCheckBalanceOtpUseCase,
    private val getRechargeCheckBalanceUseCase: GetRechargeCheckBalanceUseCase,
    private val digitalPersoMapper: DigitalPersoMapper,
    private val dispatchers: CoroutineDispatchers,
): RechargeCheckBalanceRepository {

    override suspend fun getRechargeCheckBalance(
        clientNumbers: List<String>,
        dgCategoryIds: List<Int>,
        dgOperatorIds: List<Int>,
        channelName: String
    ): DigitalPersoData =
        withContext(dispatchers.io) {
            return@withContext getRechargeCheckBalanceUseCase.apply {
                setParams(clientNumbers, dgCategoryIds, dgOperatorIds, channelName)
            }.executeOnBackground()
        }

    override suspend fun getRechargeCheckBalanceOTP(
        clientNumbers: List<String>,
        dgCategoryIds: List<Int>,
        dgOperatorIds: List<Int>,
        channelName: String,
    ): DigitalCheckBalanceOTPModel =
        withContext(dispatchers.io) {
            val data = getRechargeCheckBalanceOtpUseCase.apply {
                setParams(clientNumbers, dgCategoryIds, dgOperatorIds, channelName)
            }.executeOnBackground()

            return@withContext digitalPersoMapper.mapDigiPersoToCheckBalanceOTPModel(data.digitalPersoData)
        }
}
