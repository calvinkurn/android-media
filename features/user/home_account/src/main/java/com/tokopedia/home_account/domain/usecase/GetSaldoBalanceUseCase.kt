package com.tokopedia.home_account.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.data.model.SaldoBalanceDataModel
import com.tokopedia.home_account.domain.query.GetSaldoBalanceQuery
import javax.inject.Inject

class GetSaldoBalanceUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Unit, SaldoBalanceDataModel>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return GetSaldoBalanceQuery.query
    }

    override suspend fun execute(params: Unit): SaldoBalanceDataModel {
        return request(repository, params)
    }
}