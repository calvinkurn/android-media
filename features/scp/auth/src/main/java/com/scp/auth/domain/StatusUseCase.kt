package com.scp.auth.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

class StatusUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
): CoroutineUseCase<Unit, Any>(dispatcher.io) {

    override suspend fun execute(params: Unit): Any {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = """
	query statusQuery{
      status
	}
    """.trimIndent()
}
