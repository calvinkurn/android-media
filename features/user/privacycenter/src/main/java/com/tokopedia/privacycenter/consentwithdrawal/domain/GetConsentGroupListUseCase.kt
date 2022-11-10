package com.tokopedia.privacycenter.consentwithdrawal.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.consentwithdrawal.data.ConsentGroupListDataModel
import com.tokopedia.privacycenter.consentwithdrawal.data.GetConsentGroupListDataModel
import javax.inject.Inject

class GetConsentGroupListUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Unit, PrivacyCenterStateResult<ConsentGroupListDataModel>>(dispatcher.io) {

    override suspend fun execute(params: Unit): PrivacyCenterStateResult<ConsentGroupListDataModel> {
        val response: GetConsentGroupListDataModel = graphqlRepository.request(graphqlQuery(), params)

        return if (response.consentGroupList.success) {
            PrivacyCenterStateResult.Success(response.consentGroupList)
        } else {
            PrivacyCenterStateResult.Fail(
                MessageErrorException(response.consentGroupList.errorMessages.toString())
            )
        }
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
          }
        }
    """.trimIndent()
}
