package com.tokopedia.usercomponents.userconsent.domain.collection

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

class GetConsentCollectionUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
): CoroutineUseCase<ConsentCollectionParam, ConsentCollectionResponse>(dispatchers.io) {

    override suspend fun execute(params: ConsentCollectionParam): ConsentCollectionResponse {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String {
        return """
            query GetCollectionPoint(${'$'}collectionID: String!, ${'$'}version: String) {
              GetCollectionPoint(collectionID: ${'$'}collectionID, version: ${'$'}version) {
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
                      uiDescription
                      alwaysMandatory
                      personalDataType
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
                    collectionPointPurposeRequirement
                    collectionPointStatementOnlyFlag
                    policyNoticeType
                    PolicyNoticeTnCPageID
                    PolicyNoticePolicyPageID
                  }
                }  
              }
            }
        """.trimIndent()
    }
}
