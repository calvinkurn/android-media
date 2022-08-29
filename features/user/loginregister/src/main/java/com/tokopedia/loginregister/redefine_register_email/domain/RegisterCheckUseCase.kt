package com.tokopedia.loginregister.redefine_register_email.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.redefine_register_email.domain.data.RegisterCheckModel
import javax.inject.Inject

class RegisterCheckUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<String, RegisterCheckModel>(dispatchers.io) {

    override fun graphqlQuery(): String =
        """
            mutation registerCheck(${'$'}id: String!) {
                registerCheck(id: ${'$'}id) {
                    status
                    errors
                }
            }
        """.trimIndent()

    override suspend fun execute(params: String): RegisterCheckModel {
        val parameters = mapOf(
            ID to params
        )
        return repository.request(graphqlQuery(), parameters)
    }

    companion object {
        private const val ID = "id"
    }

}