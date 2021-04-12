package com.tokopedia.topchat.stub.common

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class GraphqlUseCaseStub<T : Any> @Inject constructor(
        graphqlRepository: GraphqlRepository
) : GraphqlUseCase<T>(graphqlRepository) {

    lateinit var response: T
    var delayMs = 0L

    override suspend fun executeOnBackground(): T {
        if(delayMs != 0L) {
            delay(delayMs)
        }
        return response
    }
}