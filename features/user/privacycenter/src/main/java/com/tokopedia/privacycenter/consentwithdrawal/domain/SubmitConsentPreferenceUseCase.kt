package com.tokopedia.privacycenter.consentwithdrawal.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.consentwithdrawal.data.SubmitConsentDataModel
import com.tokopedia.privacycenter.consentwithdrawal.data.SubmitConsentPreferenceDataModel
import com.tokopedia.privacycenter.consentwithdrawal.data.SubmitConsentPurposeReq
import javax.inject.Inject

class SubmitConsentPreferenceUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<SubmitConsentPurposeReq, PrivacyCenterStateResult<SubmitConsentDataModel>>(dispatcher.io) {

    override suspend fun execute(params: SubmitConsentPurposeReq): PrivacyCenterStateResult<SubmitConsentDataModel> {
        val response: SubmitConsentPreferenceDataModel = graphqlRepository.request(graphqlQuery(), params)

        return if (response.data.isSuccess) {
            PrivacyCenterStateResult.Success(response.data)
        } else {
            PrivacyCenterStateResult.Fail(
                Throwable(response.data.errorMessages.toString())
            )
        }
    }

    override fun graphqlQuery(): String = """
        mutation SubmitConsentPreference(${'$'}purposes: [SubmitConsentPurposeReq!]!) {
          SubmitConsentPreference(purposes: ${'$'}purposes) {
            refId
            isSuccess
            errorMessages
            receipts {
              receiptId
              identifier
              identifierType
              purposeId
              transactionType
              version
            }
          }
        }

    """.trimIndent()
}
