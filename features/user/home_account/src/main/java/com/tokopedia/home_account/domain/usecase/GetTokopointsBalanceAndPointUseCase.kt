package com.tokopedia.home_account.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.data.model.BalanceAndPointDataModel
import com.tokopedia.home_account.domain.query.GetTokopointsBalanceAndPointQuery
import javax.inject.Inject

class GetTokopointsBalanceAndPointUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Unit, BalanceAndPointDataModel>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return GetTokopointsBalanceAndPointQuery.query
    }

    override suspend fun execute(params: Unit): BalanceAndPointDataModel {
        return request(repository, params)
    }
}