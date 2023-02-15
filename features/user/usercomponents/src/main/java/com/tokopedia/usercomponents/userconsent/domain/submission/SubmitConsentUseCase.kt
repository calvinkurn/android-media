package com.tokopedia.usercomponents.userconsent.domain.submission

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

class SubmitConsentUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<ConsentSubmissionParam, ConsentSubmissionResponse>(dispatchers.io) {

    override suspend fun execute(params: ConsentSubmissionParam): ConsentSubmissionResponse {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String {
        return """
            mutation SubmitConsent(
                ${'$'}purposes: [SubmitConsentPurposeReq]!, 
                ${'$'}collectionId: String!, 
                ${'$'}version: String!,
                ${'$'}dataElements: [DataElement]!,
                ${'$'}default: Boolean
            ) {
                SubmitConsent(
                    purposes: ${'$'}purposes,
                    collectionId: ${'$'}collectionId,
                    version: ${'$'}version,
                    dataElements: ${'$'}dataElements,
                    default: ${'$'}default
                ) {
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
}
