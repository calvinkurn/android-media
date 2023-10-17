package com.tokopedia.digital_product_detail.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.digital_product_detail.data.mapper.DigitalPersoMapper
import com.tokopedia.digital_product_detail.domain.model.DigitalCheckBalanceModel
import com.tokopedia.digital_product_detail.domain.model.DigitalSaveAccessTokenResultModel
import com.tokopedia.digital_product_detail.domain.repository.RechargeCheckBalanceRepository
import com.tokopedia.digital_product_detail.domain.usecase.GetRechargeCheckBalanceUseCase
import com.tokopedia.digital_product_detail.domain.usecase.SaveRechargeUserBalanceAccessTokenUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeCheckBalanceRepositoryImpl @Inject constructor(
    private val getRechargeCheckBalanceUseCase: GetRechargeCheckBalanceUseCase,
    private val saveRechargeUserBalanceAccessTokenUseCase: SaveRechargeUserBalanceAccessTokenUseCase,
    private val digitalPersoMapper: DigitalPersoMapper,
    private val dispatchers: CoroutineDispatchers
) : RechargeCheckBalanceRepository {

    override suspend fun getRechargeCheckBalance(
        clientNumbers: List<String>,
        dgCategoryIds: List<Int>,
        dgOperatorIds: List<Int>,
        channelName: String
    ): DigitalCheckBalanceModel =
        withContext(dispatchers.io) {
            val data = getRechargeCheckBalanceUseCase.apply {
                setParams(clientNumbers, dgCategoryIds, dgOperatorIds, channelName)
            }.executeOnBackground()

            return@withContext digitalPersoMapper.mapDigiPersoToCheckBalanceModel(data.digitalPersoData)
        }

    override suspend fun saveRechargeUserBalanceAccessToken(
        msisdn: String,
        accessToken: String
    ): DigitalSaveAccessTokenResultModel =
        withContext(dispatchers.io) {
            val data = saveRechargeUserBalanceAccessTokenUseCase.apply {
                setParams(msisdn, accessToken)
            }.executeOnBackground()

            return@withContext digitalPersoMapper.mapSaveAccessTokenToAccessTokenResultModel(
                data.rechargeSaveTelcoUserBalanceAccessToken
            )
        }
}
