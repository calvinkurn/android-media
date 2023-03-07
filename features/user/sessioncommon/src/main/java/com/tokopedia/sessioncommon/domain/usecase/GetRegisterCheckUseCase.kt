package com.tokopedia.sessioncommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.sessioncommon.data.RegisterCheckModel
import javax.inject.Inject

class GetRegisterCheckUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<String, RegisterCheckModel>(dispatchers.io) {

    override fun graphqlQuery(): String =
        """
            mutation registerCheck(${'$'}id: String!) {
                registerCheck(id: ${'$'}id) {
                    isExist
                    errors
                    view
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
