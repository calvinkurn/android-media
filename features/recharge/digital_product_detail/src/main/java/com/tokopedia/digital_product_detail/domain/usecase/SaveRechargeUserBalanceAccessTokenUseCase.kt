package com.tokopedia.digital_product_detail.domain.usecase

import com.tokopedia.digital_product_detail.data.model.data.DigitalSaveAccessTokenResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

@GqlQuery(SaveRechargeUserBalanceAccessTokenUseCase.QUERY_NAME, SaveRechargeUserBalanceAccessTokenUseCase.QUERY)
class SaveRechargeUserBalanceAccessTokenUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<DigitalSaveAccessTokenResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(RechargeSaveTelcoUserBalanceAccessTokenQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(DigitalSaveAccessTokenResponse::class.java)
    }

    fun setParams(
        msisdn: String,
        accessToken: String
    ) {
        val params = mapOf(
            KEY_MSISDN to msisdn,
            KEY_ACCESS_TOKEN to accessToken
        )
        setRequestParams(params)
    }

    companion object {
        private const val KEY_MSISDN = "msisdn"
        private const val KEY_ACCESS_TOKEN = "accessToken"
        const val QUERY_NAME = "RechargeSaveTelcoUserBalanceAccessTokenQuery"
        const val QUERY = """
            query saveRechargeUserBalanceAccessToken(${'$'}msisdn: String!, ${'$'}accessToken: String!) {
              rechargeSaveTelcoUserBalanceAccessToken(msisdn: ${'$'}msisdn, accessToken: ${'$'}accessToken) {
                agentRC
                grc
                message
              }
            }
        """
    }
}
