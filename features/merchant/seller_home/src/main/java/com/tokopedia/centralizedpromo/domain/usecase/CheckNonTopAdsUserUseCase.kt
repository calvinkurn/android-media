package com.tokopedia.centralizedpromo.domain.usecase

import com.tokopedia.centralizedpromo.domain.model.NonTopAdsUserResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

private val QUERY: String =
    """query  topadsGetShopInfoV2_1(${'$'}shop_id : String!, ${'$'}source : String!){
  topadsGetShopInfoV2_1(shopID: ${'$'}shop_id, source: ${'$'}source) {
    data {
      ads {
        is_used
      }
    }
    
  }
}""".trimIndent()

class CheckNonTopAdsUserUseCase @Inject constructor(
    repository: GraphqlRepository,
) : GraphqlUseCase<NonTopAdsUserResponse>(repository) {

    companion object {
        private const val SHOP_ID_KEY = "shop_id"
        private const val SOURCE = "source"
        private const val SOURCE_VALUE = "seller_home"

        private fun createRequestParams(shopId: String, source: String): RequestParams {
            return RequestParams.create().apply {
                putString(SHOP_ID_KEY, shopId)
                putString(SOURCE, source)
            }
        }
    }

    init {
        setGraphqlQuery(QUERY)
        setTypeClass(NonTopAdsUserResponse::class.java)
    }

    suspend fun execute(shopId: String): Boolean {
        var isNonTopAdsUser = true
        setRequestParams(createRequestParams(shopId, SOURCE_VALUE).parameters)
        val response = executeOnBackground().topAdsGetShopInfoV21
        if (response?.nonTopAdsUserData?.ads?.isNullOrEmpty() == true) {
            isNonTopAdsUser = false
        } else {
            run breaker@{
                response?.nonTopAdsUserData?.ads?.forEach {
                    if (it?.isUsed == true) {
                        isNonTopAdsUser = false
                        return@breaker
                    }
                }
            }
        }
        return isNonTopAdsUser
    }
}
