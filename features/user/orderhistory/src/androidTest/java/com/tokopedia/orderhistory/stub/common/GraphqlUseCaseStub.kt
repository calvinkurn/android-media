package com.tokopedia.orderhistory.stub.common

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class GraphqlUseCaseStub<T : Any> @Inject constructor(
        graphqlRepository: GraphqlRepository
) : GraphqlUseCase<T>(graphqlRepository) {

    lateinit var response: T

    override suspend fun executeOnBackground(): T {
        return response
    }
}