package com.tokopedia.review.feature.reputationhistory.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.review.feature.reputationhistory.data.model.response.ReputationPenaltyRewardWrapper
import com.tokopedia.review.feature.reputationhistory.domain.mapper.SellerReputationPenaltyMapper
import com.tokopedia.review.feature.reputationhistory.view.model.SellerReputationPenaltyMergeUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetReputationShopAndPenaltyRewardUseCase @Inject constructor(
    private val getReputationShopUseCase: GetReputationShopUseCase,
    private val getReputationPenaltyRewardUseCase: GetReputationPenaltyRewardUseCase,
    private val dispatcherProvider: CoroutineDispatchers,
    private val sellerReputationPenaltyMapper: SellerReputationPenaltyMapper
) {
    suspend fun execute(shopId: Long, page: Int, startDate: String, endDate: String): Result<SellerReputationPenaltyMergeUiModel> {
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
               Success(sellerReputationPenaltyMapper.mapToSellerReputationMerge(ReputationPenaltyRewardWrapper(
                   getReputationShopResponse.await(),
                   getReputationPenaltyRewardResponse.await()
               ), startDate, endDate))
           }
       } catch (e: Throwable) {
           Fail(e)
       }
    }
}