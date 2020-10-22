package com.tokopedia.topchat.stub.common

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository

class GraphqlUseCaseStub<T : Any>(
        graphqlRepository: GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository
) : GraphqlUseCase<T>(graphqlRepository) {

    lateinit var response: T

    override suspend fun executeOnBackground(): T {
        return response
    }
}