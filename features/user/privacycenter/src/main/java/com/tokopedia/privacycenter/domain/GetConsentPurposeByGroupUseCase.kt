package com.tokopedia.privacycenter.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.data.ConsentPurposeGroupDataModel
import com.tokopedia.privacycenter.data.GetConsentPurposeDataModel
import javax.inject.Inject

class GetConsentPurposeByGroupUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Map<String, Int>, PrivacyCenterStateResult<ConsentPurposeGroupDataModel>>(dispatcher.io) {

    override suspend fun execute(params: Map<String, Int>): PrivacyCenterStateResult<ConsentPurposeGroupDataModel> {
        val response: GetConsentPurposeDataModel = graphqlRepository.request(graphqlQuery(), params)

        return if (response.consentGroup.isSuccess) {
            PrivacyCenterStateResult.Success(response.consentGroup)
        } else {
            PrivacyCenterStateResult.Fail(
                Throwable(response.consentGroup.errorMessages.toString())
            )
        }
    }

    override fun graphqlQuery(): String = """
        query GetPurposesByGroup(${'$'}groupId: Int!) {
            GetPurposesByGroup(groupId: ${'$'}groupId){
                isSuccess
                refId
                errorMessages
                groupId
                consents {
                    mandatory {
                        consentTitle
                        consentSubtitle
                        consentStatus
                        purposeId
                        optInUrl
                        optOutUrl
                        optIntAppLink
                        optOutAppLink
                        priority
                    }
                    optional {
                        consentTitle
                        consentSubtitle
                        consentStatus
                        purposeId
                        optInUrl
                        optOutUrl
                        optIntAppLink
                        optOutAppLink
                        priority
                    }
                }
            }
        }
    """.trimIndent()

    companion object {
        const val PARAM_GROUP_ID = "groupId"
    }
}
