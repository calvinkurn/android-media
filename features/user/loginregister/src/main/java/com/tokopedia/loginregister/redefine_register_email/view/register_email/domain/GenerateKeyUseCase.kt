package com.tokopedia.loginregister.redefine_register_email.view.register_email.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.redefine_register_email.view.register_email.domain.data.GenerateKeyModel
import javax.inject.Inject

class GenerateKeyUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Unit, GenerateKeyModel>(dispatchers.io) {

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

    override suspend fun execute(params: Unit): GenerateKeyModel {
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