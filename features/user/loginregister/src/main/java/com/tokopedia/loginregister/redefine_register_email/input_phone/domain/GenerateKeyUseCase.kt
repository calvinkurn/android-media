package com.tokopedia.loginregister.redefine_register_email.input_phone.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.redefine_register_email.input_phone.domain.data.GenerateKeyModel
import com.tokopedia.sessioncommon.constants.SessionConstants
import javax.inject.Inject

class GenerateKeyUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Unit, GenerateKeyModel>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
            query generate_key(${'$'}module: String!) {
                generate_key(module: ${'$'}module) {
                    key
                    server_timestamp
                    h
                }
            }
        """.trimIndent()

    override suspend fun execute(params: Unit): GenerateKeyModel {
        val module: String = SessionConstants.GenerateKeyModule.PASSWORD.value
        return repository.request(graphqlQuery(), module)
    }
}