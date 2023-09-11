package com.tokopedia.home_account.consentWithdrawal.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.consentWithdrawal.data.GetConsentGroupListDataModel
import javax.inject.Inject

class GetConsentGroupListUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Unit, GetConsentGroupListDataModel>(dispatcher.io) {

    override suspend fun execute(params: Unit): GetConsentGroupListDataModel {
        return graphqlRepository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = """
        query GetConsentGroupList {
          GetConsentGroupList {
            success
            refId
            errorMessages
            groups{
              id
              groupTitle
              groupSubtitle
              groupImg
              priority
            }
            ticker
          }
        }
    """.trimIndent()
}
