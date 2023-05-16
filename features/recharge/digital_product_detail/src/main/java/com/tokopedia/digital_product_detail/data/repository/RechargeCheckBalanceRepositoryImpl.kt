package com.tokopedia.digital_product_detail.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.digital_product_detail.data.mapper.DigitalPersoMapper
import com.tokopedia.digital_product_detail.domain.model.DigitalCheckBalanceModel
import com.tokopedia.digital_product_detail.domain.repository.RechargeCheckBalanceRepository
import com.tokopedia.digital_product_detail.domain.usecase.GetRechargeCheckBalanceUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeCheckBalanceRepositoryImpl @Inject constructor(
    private val getRechargeCheckBalanceUseCase: GetRechargeCheckBalanceUseCase,
    private val digitalPersoMapper: DigitalPersoMapper,
    private val dispatchers: CoroutineDispatchers,
): RechargeCheckBalanceRepository {

    override suspend fun getRechargeCheckBalance(
        clientNumbers: List<String>,
        dgCategoryIds: List<Int>,
        dgOperatorIds: List<Int>,
        channelName: String,
    ): DigitalCheckBalanceModel =
        withContext(dispatchers.io) {
            val data = getRechargeCheckBalanceUseCase.apply {
                setParams(clientNumbers, dgCategoryIds, dgOperatorIds, channelName)
            }.executeOnBackground()

            return@withContext digitalPersoMapper.mapDigiPersoToCheckBalanceOTPModel(data.digitalPersoData)
        }
}
