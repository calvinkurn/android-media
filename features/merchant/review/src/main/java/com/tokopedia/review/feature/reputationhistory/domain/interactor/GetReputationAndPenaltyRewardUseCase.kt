package com.tokopedia.review.feature.reputationhistory.domain.interactor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.review.feature.reputationhistory.data.model.response.ReputationPenaltyRewardWrapper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetReputationAndPenaltyRewardUseCase @Inject constructor(
    private val getReputationShopUseCase: GetReputationShopUseCase,
    private val getReputationPenaltyRewardUseCase: GetReputationPenaltyRewardUseCase,
    private val dispatcherProvider: CoroutineDispatchers
) {
    suspend fun execute(shopId: String, page: Long, startDate: String, endDate: String): Result<ReputationPenaltyRewardWrapper> {
       return try {
           withContext(dispatcherProvider.io) {
               val getReputationShopResponse = async {
                   getReputationShopUseCase.setParams(shopId)
                   getReputationShopUseCase.executeOnBackground()
               }
               val getReputationPenaltyRewardResponse = async {
                   getReputationPenaltyRewardUseCase.setParams(shopId, page, startDate, endDate)
                   getReputationPenaltyRewardUseCase.executeOnBackground()
               }
               Success(ReputationPenaltyRewardWrapper(
                   getReputationShopResponse.await(),
                   getReputationPenaltyRewardResponse.await()
               ))
           }
       } catch (e: Throwable) {
           Fail(e)
       }
    }
}