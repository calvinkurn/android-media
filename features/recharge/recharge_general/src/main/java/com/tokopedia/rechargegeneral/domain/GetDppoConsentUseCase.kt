package com.tokopedia.rechargegeneral.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.rechargegeneral.domain.GetDppoConsentUseCase.Companion.QUERY_NAME
import com.tokopedia.rechargegeneral.domain.GetDppoConsentUseCase.Companion.QUERY_VALUE
import com.tokopedia.rechargegeneral.model.RechargeGeneralDppoConsent
import javax.inject.Inject

@GqlQuery(QUERY_NAME, QUERY_VALUE)
class GetDppoConsentUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<RechargeGeneralDppoConsent>(graphqlRepository) {

    init {
        setGraphqlQuery(QueryGetDppoConsent())
        setTypeClass(RechargeGeneralDppoConsent::class.java)
    }

    suspend fun execute(categoryId: Int): RechargeGeneralDppoConsent {
        setRequestParams(createRequestParams(categoryId))
        return executeOnBackground()
    }

    private fun createRequestParams(categoryId: Int): HashMap<String, Any> {
        return hashMapOf(
            KEY_INPUT to hashMapOf(
                KEY_CHANNEL_NAME to CHANNEL_NAME_RECHARGE_PDP_CONSENT,
                KEY_ID to listOf(categoryId)
            )
        )
    }

    companion object {
        private const val KEY_INPUT = "input"
        private const val KEY_CHANNEL_NAME = "channelName"
        private const val KEY_ID = "dgCategoryIDs"
        private const val CHANNEL_NAME_RECHARGE_PDP_CONSENT = "recharge_pdp_consent"

        const val QUERY_NAME = "QueryGetDppoConsent"
        const val QUERY_VALUE = """
            query digiPersoGetPersonalizedItems(${'$'}input: DigiPersoGetPersonalizedItemsRequest!) {
              digiPersoGetPersonalizedItems(input: ${'$'}input) {
                items {
                  id
                  title
                }
              }
            }
        """
    }
}
