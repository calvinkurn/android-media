package com.tokopedia.updateinactivephone.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.domain.data.StatusInactivePhoneNumberDataModel
import javax.inject.Inject

open class GetStatusInactivePhoneNumberUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<InactivePhoneUserDataModel, StatusInactivePhoneNumberDataModel>(dispatcher.io) {

    override suspend fun execute(params: InactivePhoneUserDataModel): StatusInactivePhoneNumberDataModel {
        return repository.request(graphqlQuery(), createParams(params))
    }

    override fun graphqlQuery(): String {
        return """
            query GetStatusInactivePhoneNumber(${'$'}email: String, ${'$'}msisdn: String, ${'$'}index: Int) {
              GetStatusInactivePhoneNumber(email: ${'$'}email, msisdn: ${'$'}msisdn, index: ${'$'}index) {
                error_message
                is_success
                is_allowed
                userid_enc
              }
            }
        """.trimIndent()
    }

    private fun createParams(params: InactivePhoneUserDataModel): Map<String, Any> = mapOf(
        PARAM_EMAIL to params.email,
        PARAM_PHONE to params.oldPhoneNumber,
        PARAM_USER_INDEX to params.userIndex
    )

    companion object {
        const val PARAM_EMAIL = "email"
        const val PARAM_PHONE = "msisdn"
        const val PARAM_USER_INDEX = "index"
    }
}