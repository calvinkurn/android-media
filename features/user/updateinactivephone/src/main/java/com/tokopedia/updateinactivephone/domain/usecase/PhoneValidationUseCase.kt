package com.tokopedia.updateinactivephone.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.domain.data.PhoneValidationDataModel
import javax.inject.Inject

open class PhoneValidationUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<InactivePhoneUserDataModel, PhoneValidationDataModel>(dispatcher.io) {

    override suspend fun execute(params: InactivePhoneUserDataModel): PhoneValidationDataModel {
        return repository.request(graphqlQuery(), createParams(params))
    }

    override fun graphqlQuery(): String {
        return """
            query validateInactivePhoneUser(${'$'}msisdn: String, ${'$'}email: String, ${'$'}index: Int) {
                ValidateInactivePhoneUser(msisdn: ${'$'}msisdn, email: ${'$'}email, index: ${'$'}index) {
                    isSuccess
                    errorMessage
                    status
                }
            }
        """.trimIndent()
    }

    private fun createParams(params: InactivePhoneUserDataModel): Map<String, Any> = mapOf(
        PARAM_EMAIL to params.email,
        PARAM_PHONE to params.oldPhoneNumber,
        PARAM_INDEX to params.userIndex
    )

    companion object {
        private const val PARAM_PHONE = "msisdn"
        private const val PARAM_EMAIL = "email"
        private const val PARAM_INDEX = "index"
    }
}