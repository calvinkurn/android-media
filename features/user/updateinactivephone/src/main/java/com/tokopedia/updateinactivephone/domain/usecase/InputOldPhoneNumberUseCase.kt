package com.tokopedia.updateinactivephone.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.updateinactivephone.domain.data.RegisterCheckModel
import javax.inject.Inject

open class InputOldPhoneNumberUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<String, RegisterCheckModel>(dispatcher.io) {

    override fun graphqlQuery(): String =
        """
        mutation register_check (${'$'}id: String!){
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
        return graphqlRepository.request(graphqlQuery(), parameters)
    }

    companion object {
        private const val ID = "id"
    }

}