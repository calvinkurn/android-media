package com.tokopedia.centralizedpromo.domain.usecase

import com.tokopedia.centralizedpromo.domain.model.SellerHomeWhiteListUserResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

val GET_WHITELISTED_USERS_SELLER_HOME: String =
    """query topAdsGetShopWhitelistedFeature(${'$'}shopID: String!){
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
""".trimIndent()

private const val KEY_SHOP_ID = "shopID"
private const val ON_BOARDING = "onboarding"

class SellerHomeGetWhiteListedUserUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
    val userSession: UserSessionInterface
) : GraphqlUseCase<SellerHomeWhiteListUserResponse>(graphqlRepository) {

    init {
        setTypeClass(SellerHomeWhiteListUserResponse::class.java)
        setGraphqlQuery(GET_WHITELISTED_USERS_SELLER_HOME)
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
}
