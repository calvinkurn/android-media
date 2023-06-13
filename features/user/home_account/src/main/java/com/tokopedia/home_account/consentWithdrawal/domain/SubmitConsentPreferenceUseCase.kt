package com.tokopedia.home_account.consentWithdrawal.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.consentWithdrawal.data.SubmitConsentPreferenceDataModel
import com.tokopedia.home_account.consentWithdrawal.data.SubmitConsentPurposeReq
import javax.inject.Inject

class SubmitConsentPreferenceUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<SubmitConsentPurposeReq, SubmitConsentPreferenceDataModel>(dispatcher.io) {

    override suspend fun execute(params: SubmitConsentPurposeReq): SubmitConsentPreferenceDataModel {
        return graphqlRepository.request(graphqlQuery(), params)
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
