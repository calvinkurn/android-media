package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.sellerhome.domain.mapper.ShopStateInfoMapper
import com.tokopedia.sellerhome.domain.model.GetShopStateResponse
import com.tokopedia.sellerhome.view.model.ShopStateInfoUiModel
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.DynamicSellerStateInfoParamModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("GetShopStateGqlQuery", GetShopStateUseCase.QUERY)
class GetShopStateUseCase @Inject constructor(
    private val mapper: ShopStateInfoMapper,
    gqlRepository: GraphqlRepository
) : GraphqlUseCase<GetShopStateResponse>(gqlRepository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setGraphqlQuery(GetShopStateGqlQuery())
        setTypeClass(GetShopStateResponse::class.java)
    }

    suspend fun executeInBackground(
        shopId: String,
        dataKey: String,
        pageSource: String
    ): ShopStateInfoUiModel {
        val requestParams = createRequestParam(shopId, dataKey, pageSource)
        setRequestParams(requestParams.parameters)

        try {
            val data: GetShopStateResponse = executeOnBackground()
            return mapper.mapToUiModel(data)
        } catch (e: Exception) {
            throw RuntimeException(e.message.orEmpty())
        }
    }

    private fun createRequestParam(
        shopId: String,
        dataKey: String,
        pageSource: String
    ): RequestParams {
        return RequestParams.create().apply {
            putObject(
                PARAM_DATA_KEYS,
                listOf(
                    DataKeyModel(
                        key = dataKey,
                        jsonParams = DynamicSellerStateInfoParamModel(
                            shopId = shopId,
                            pageSource = pageSource
                        ).toJsonString()
                    )
                )
            )
        }
    }

    companion object {
        const val QUERY = """
            query fetchInfoWidgetData(${'$'}dataKeys: [dataKey!]!) {
              fetchInfoWidgetData(dataKeys: ${'$'}dataKeys) {
                data {
                  meta {
                    shopState
                  }
                }
              }
            }
        """

        private const val PARAM_DATA_KEYS = "dataKeys"
    }
}
