package com.tokopedia.sessioncommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.sessioncommon.data.GenerateKeyPojo
import javax.inject.Inject

class GenerateKeyUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Unit, GenerateKeyPojo>(dispatchers.io) {

    override fun graphqlQuery(): String =
        """
            query generate_key(${'$'}module: String!){
              generate_key(module: ${'$'}module){
                key
                server_timestamp
                h
              }
            }
        """.trimIndent()

    override suspend fun execute(params: Unit): GenerateKeyPojo {
        val parameters = mapOf(
            PARAM_RELATION_TYPE to VALUE_RELATION_TYPE
        )
        return repository.request(graphqlQuery(), parameters)
    }

    companion object {
        private const val PARAM_RELATION_TYPE = "module"
        private const val VALUE_RELATION_TYPE = "pwd"
    }
}
