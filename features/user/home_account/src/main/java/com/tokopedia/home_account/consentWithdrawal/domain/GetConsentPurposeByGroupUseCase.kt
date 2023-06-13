package com.tokopedia.home_account.consentWithdrawal.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.consentWithdrawal.data.GetConsentPurposeDataModel
import javax.inject.Inject

class GetConsentPurposeByGroupUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Map<String, Int>, GetConsentPurposeDataModel>(dispatcher.io) {

    override suspend fun execute(params: Map<String, Int>): GetConsentPurposeDataModel {
        return graphqlRepository.request(graphqlQuery(), params)
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
