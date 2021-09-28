package com.tokopedia.updateinactivephone.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.updateinactivephone.domain.data.PhoneValidationDataModel
import javax.inject.Inject

open class PhoneValidationUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Map<String, Any>, PhoneValidationDataModel>(dispatcher.io) {

    override suspend fun execute(params: Map<String, Any>): PhoneValidationDataModel {
        return request(repository, params)
    }

    override fun graphqlQuery(): String {
        return query
    }

    companion object {
        const val PARAM_PHONE = "phone"
        const val PARAM_EMAIL = "email"
        const val PARAM_INDEX = "index"

        private val query = """
            query validateInactivePhoneUser(${'$'}phone: String, ${'$'}email: String, ${'$'}index: Int) {
                ValidateInactivePhoneUser(msisdn: ${'$'}phone, email: ${'$'}email, index: ${'$'}index) {
                    isSuccess
                    errorMessage
                    status
                }
            }
        """.trimIndent()
    }
}