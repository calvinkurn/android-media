package com.tokopedia.tokofood.feature.search.searchresult.domain.usecase

import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokofood.common.domain.param.TokoFoodMerchantListParamMapper
import com.tokopedia.tokofood.feature.search.searchresult.domain.response.TokofoodSearchMerchantResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

private const val QUERY = """
        query TokofoodSearchMerchant(${'$'}params: String!, ${'$'}pageKey: String, ${'$'}limit: Int) {
          tokofoodSearchMerchant(params: ${'$'}params, pageKey: ${'$'}pageKey, limit: ${'$'}limit) {
            merchants {
              id
              applink
              brandID
              name
              addressLocality
              imageURL
              priceLevel {
                icon
                fareCount
              }
              merchantCategories
              rating
              ratingFmt
              distance
              distanceFmt
              etaFmt
              promo
              hasBranch
              branchApplink
              isClosed
              additionalData {
                topTextBanner
                discountIcon
              }
            }
            state {
              status
              title
              subtitle
            }
            nextPageKey
          }
        }
    """


@GqlQuery("TokofoodSearchMerchant", QUERY)
class TokofoodSearchMerchantUseCase @Inject constructor(
    repository: GraphqlRepository
) : GraphqlUseCase<TokofoodSearchMerchantResponse>(repository) {

    init {
        setTypeClass(TokofoodSearchMerchantResponse::class.java)
        setGraphqlQuery(TokofoodSearchMerchant())
    }

    suspend fun execute(
        localCacheModel: LocalCacheModel?,
        searchParameter: HashMap<String, String>,
        pageKey: String? = null
    ): TokofoodSearchMerchantResponse {
        setRequestParams(
            createRequestParams(localCacheModel, searchParameter, pageKey).parameters
        )
        return executeOnBackground()
    }

    companion object {

        private const val PARAMS_KEY = "params"

        @JvmStatic
        fun createRequestParams(
            localCacheModel: LocalCacheModel?,
            searchParameter: HashMap<String, String>,
            pageKey: String?
        ): RequestParams {
            return RequestParams.create().apply {
                val params = getParamsStringValue(localCacheModel, searchParameter)
                putString(PARAMS_KEY, params)
                if (pageKey != null) {
                    putString(TokoFoodMerchantListParamMapper.PAGE_KEY, pageKey)
                }
                putInt(
                    TokoFoodMerchantListParamMapper.LIMIT_KEY,
                    TokoFoodMerchantListParamMapper.LIMIT
                )
            }
        }

        private fun getParamsStringValue(
            localCacheModel: LocalCacheModel?,
            searchParameter: HashMap<String, String>
        ): String {
            val searchParamMap = searchParameter.entries.associate {
                it.key as? String to it.value as? Any
            }
            val updatedSearchParamMap = searchParamMap.toMutableMap().apply {
                put(
                    TokoFoodMerchantListParamMapper.LAT_LONG_KEY,
                    TokoFoodMerchantListParamMapper.mapLocation(localCacheModel)
                )
                put(
                    TokoFoodMerchantListParamMapper.TIMEZONE_KEY,
                    TokoFoodMerchantListParamMapper.TIMEZONE
                )
                put(
                    TokoFoodMerchantListParamMapper.USER_CITY_ID_KEY,
                    localCacheModel?.city_id.orEmpty()
                )
            }
            return UrlParamUtils.generateUrlParamString(updatedSearchParamMap)
        }
    }

}
