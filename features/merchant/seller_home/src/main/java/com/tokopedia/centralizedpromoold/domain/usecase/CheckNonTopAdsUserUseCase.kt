package com.tokopedia.centralizedpromoold.domain.usecase

import com.tokopedia.centralizedpromoold.domain.usecase.CheckNonTopAdsUserGqlQuery
import com.tokopedia.centralizedpromoold.domain.model.NonTopAdsUserResponseOld
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("CheckNonTopAdsUserGqlQuery", CheckNonTopAdsUserUseCase.QUERY)
class CheckNonTopAdsUserUseCase @Inject constructor(
    repository: GraphqlRepository,
) : GraphqlUseCase<NonTopAdsUserResponseOld>(repository) {

    init {
        setGraphqlQuery(CheckNonTopAdsUserGqlQuery())
        setTypeClass(NonTopAdsUserResponseOld::class.java)
    }

    suspend fun execute(shopId: String): Boolean {
        setRequestParams(createRequestParams(shopId).parameters)
        val response = executeOnBackground().topAdsGetShopInfoV21
        val ads = response?.nonTopAdsUserData?.ads
        if (ads?.isNullOrEmpty() == true) return false
        return !ads.any { it?.isUsed == true }
    }

    companion object {
        const val QUERY: String = """
            query topadsGetShopInfo(${'$'}shop_id : String!, ${'$'}source : String!){
              topadsGetShopInfoV2_1(shopID: ${'$'}shop_id, source: ${'$'}source) {
                data {
                  ads {
                    is_used
                  }
                }  
              }
            }
        """

        private const val SHOP_ID_KEY = "shop_id"
        private const val SOURCE = "source"
        private const val SOURCE_VALUE = "seller_home"

        private fun createRequestParams(shopId: String): RequestParams {
            return RequestParams.create().apply {
                putString(SHOP_ID_KEY, shopId)
                putString(SOURCE, SOURCE_VALUE)
            }
        }
    }
}
