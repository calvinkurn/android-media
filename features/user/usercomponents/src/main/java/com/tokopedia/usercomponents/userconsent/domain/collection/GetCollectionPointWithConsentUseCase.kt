package com.tokopedia.usercomponents.userconsent.domain.collection

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.usercomponents.userconsent.common.ConsentCollectionResponse
import javax.inject.Inject

class GetCollectionPointWithConsentUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
): CoroutineUseCase<ConsentCollectionParam, ConsentCollectionResponse>(dispatchers.io) {

    override suspend fun execute(params: ConsentCollectionParam): ConsentCollectionResponse {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String {
        return """
            query GetCollectionPointWithConsent(${'$'}collectionID: String!, ${'$'}version: String, ${'$'}dataElements: [DataElement]) {
              GetCollectionPointWithConsent(collectionID: ${'$'}collectionID, version: ${'$'}version, dataElements: ${'$'}dataElements) {
                success
                refId
                errorMessages
                collectionPoints {
                  id
                  version
                  consentType
                  purposes {
                    id
                    label
                    description
                    version
                    purposeType
                    attributes {
                      uiName
                      dataElementType
                    }
                  }
                  notices {
                    id
                    name
                    url
                    privacyNoticeGuid
                    type
                  }
                  attributes {
                    requirementType
                    usingCheckbox
                    policyNoticeType
                    tncPageID
                    externalPartyPrivacyPolicy
                    externalPartyTnC
                    statementWording {
                      template
                      attributes {
                        key
                        type
                        text
                        link
                      }
                    }
                  }
                  needConsent
                }  
              }
            }
        """.trimIndent()
    }
}

