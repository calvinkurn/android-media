package com.tokopedia.common_wallet.pendingcashback.domain.coroutine

import com.tokopedia.common_wallet.pendingcashback.data.PendingCashbackEntity
import com.tokopedia.common_wallet.pendingcashback.data.ResponsePendingCashback
import com.tokopedia.common_wallet.pendingcashback.view.PendingCashback
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCoroutinePendingCashbackUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
)
    : UseCase<PendingCashback>() {
    override suspend fun executeOnBackground(): PendingCashback = withContext(Dispatchers.IO){
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val gqlRecommendationRequest = GraphqlRequest(
                com.tokopedia.common_wallet.balance.domain.query.PendingCashback.query,
                ResponsePendingCashback::class.java,
                mapOf()
        )
        val response = graphqlRepository.getReseponse(listOf(gqlRecommendationRequest), cacheStrategy)
        val errors = response.getError(ResponsePendingCashback::class.java)
        if(errors?.isNotEmpty() == true){
            error(errors.first().message)
        }
        val pendingCashbackResponse = response.getSuccessData<ResponsePendingCashback>()
        mapper(pendingCashbackResponse.pendingCashbackEntity)
    }

    private fun mapper(pendingCashbackEntity: PendingCashbackEntity?): PendingCashback {
        pendingCashbackEntity?.let {
            var amount = 0
            try {
                amount = Integer.parseInt(pendingCashbackEntity.balance)
            } catch (ignored: NumberFormatException) {
            }

            val pendingCashback = PendingCashback()
            pendingCashback.amount = amount
            pendingCashback.amountText = pendingCashbackEntity.balanceText
            return pendingCashback
        }
        throw RuntimeException("Error")
    }
}