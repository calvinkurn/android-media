package com.tokopedia.home_account.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.data.model.TokopointsBalanceDataModel
import com.tokopedia.home_account.domain.query.GetBalanceAndPointQuery
import javax.inject.Inject

open class GetTokopointsBalanceAndPointUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Unit, TokopointsBalanceDataModel>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return GetBalanceAndPointQuery.tokopointsQuery
    }

    override suspend fun execute(params: Unit): TokopointsBalanceDataModel {
        return request(repository, params)
    }
}