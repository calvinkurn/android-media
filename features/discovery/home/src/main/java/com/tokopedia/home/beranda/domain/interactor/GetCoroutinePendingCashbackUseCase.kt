package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.common_wallet.pendingcashback.data.PendingCashbackEntity
import com.tokopedia.common_wallet.pendingcashback.data.ResponsePendingCashback
import com.tokopedia.common_wallet.pendingcashback.view.PendingCashback
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetCoroutinePendingCashbackUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<ResponsePendingCashback>
)
    : UseCase<PendingCashback>() {
    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(ResponsePendingCashback::class.java)
    }
    override suspend fun executeOnBackground(): PendingCashback {
        graphqlUseCase.clearCache()
        graphqlUseCase.setRequestParams(mapOf())
        return mapper(graphqlUseCase.executeOnBackground().pendingCashbackEntity)
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