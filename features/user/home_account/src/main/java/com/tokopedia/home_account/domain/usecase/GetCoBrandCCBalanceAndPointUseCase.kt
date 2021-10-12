package com.tokopedia.home_account.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.data.model.CoBrandCCBalanceDataModel
import com.tokopedia.home_account.domain.query.GetBalanceAndPointQuery
import javax.inject.Inject

open class GetCoBrandCCBalanceAndPointUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Unit, CoBrandCCBalanceDataModel>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return GetBalanceAndPointQuery.coBrandCCQuery
    }

    override suspend fun execute(params: Unit): CoBrandCCBalanceDataModel {
        return repository.request(graphqlQuery(), params)
    }
}