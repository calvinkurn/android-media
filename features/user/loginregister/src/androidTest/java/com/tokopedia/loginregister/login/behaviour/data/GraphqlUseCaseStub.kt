package com.tokopedia.loginregister.login.behaviour.data

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

open class GraphqlUseCaseStub<T : Any> @Inject constructor(
        graphqlRepository: GraphqlRepository
) : GraphqlUseCase<T>(graphqlRepository) {

    lateinit var response: T
    var isError = false
    var delayMs = 0L

    override suspend fun executeOnBackground(): T {
        if (isError) {
            throw IllegalStateException()
        }
        if (delayMs != 0L) {
            delay(delayMs)
        }
        return response
    }
}