package com.tokopedia.home_account.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.data.model.TokopointsBalanceDataModel
import com.tokopedia.home_account.domain.query.GetBalanceAndPointQuery
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

open class GetTokopointsBalanceAndPointUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatcher
) : CoroutineUseCase<Unit, TokopointsBalanceDataModel>(dispatcher) {

    override fun graphqlQuery(): String {
        return GetBalanceAndPointQuery.tokopointsQuery
    }

    override suspend fun execute(params: Unit): TokopointsBalanceDataModel {
        return repository.request(graphqlQuery(), params)
    }
}