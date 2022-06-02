package com.tokopedia.usercomponents.userconsent.domain

import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetConsentCollectionUseCase @Inject constructor(
    private val repository: GraphqlRepository
): CoroutineUseCase<ConsentCollectionParam, ConsentCollectionResponse>(Dispatchers.IO) {

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