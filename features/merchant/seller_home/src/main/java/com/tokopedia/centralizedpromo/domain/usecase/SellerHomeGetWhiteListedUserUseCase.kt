package com.tokopedia.centralizedpromo.domain.usecase

import com.tokopedia.centralizedpromo.domain.model.SellerHomeWhiteListUserResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

@GqlQuery("SellerHomeGetWhiteListedUserGqlQuery", SellerHomeGetWhiteListedUserUseCase.QUERY)
class SellerHomeGetWhiteListedUserUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
    val userSession: UserSessionInterface
) : GraphqlUseCase<SellerHomeWhiteListUserResponse>(graphqlRepository) {

    init {
        setTypeClass(SellerHomeWhiteListUserResponse::class.java)
        setGraphqlQuery(SellerHomeGetWhiteListedUserGqlQuery.GQL_QUERY)
    }

    suspend fun executeQuery(): Boolean {
        var isUserWhiteListed = false
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setRequestParams(createRequestParams().parameters)
        val response = executeOnBackground().topAdsGetShopWhitelistedFeature
        response.data.forEach { data ->
            if (data.featureName == ON_BOARDING) {
                isUserWhiteListed = true
            }
        }
        return isUserWhiteListed
    }

    private fun createRequestParams(): RequestParams {
        return RequestParams.create().apply {
            putString(KEY_SHOP_ID, userSession.shopId)
        }
    }

    companion object {
        const val QUERY: String = """
            query topAdsGetShopWhitelistedFeature(${'$'}shopID: String!){
              topAdsGetShopWhitelistedFeature(shopID: ${'$'}shopID){
                  data {
                    featureID
                    featureName
                  }
                  errors {
                     code
                     detail
                     title
                  }
              }
            }
        """
        private const val KEY_SHOP_ID = "shopID"
        private const val ON_BOARDING = "onboarding"
    }
}
