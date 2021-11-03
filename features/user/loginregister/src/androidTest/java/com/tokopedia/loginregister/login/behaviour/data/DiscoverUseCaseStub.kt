package com.tokopedia.loginregister.login.behaviour.data

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.discover.pojo.DiscoverPojo
import com.tokopedia.loginregister.discover.usecase.DiscoverUseCase
import kotlinx.coroutines.CoroutineDispatcher

class DiscoverUseCaseStub(
    graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatcher
): DiscoverUseCase(graphqlRepository, dispatcher) {

    var response = DiscoverPojo()

    override suspend fun execute(params: String): DiscoverPojo {
        return response
    }
}