package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.sellerhome.domain.mapper.ShopStateInfoMapper
import com.tokopedia.sellerhome.domain.model.GetShopStateInfoResponse
import com.tokopedia.sellerhome.view.model.ShopStateInfoUiModel
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.DynamicSellerStateInfoParamModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 23/11/22.
 */

@GqlQuery("GetShopStateInfoGqlQuery", GetShopStateInfoUseCase.QUERY)
class GetShopStateInfoUseCase @Inject constructor(
    private val mapper: ShopStateInfoMapper,
    gqlRepository: GraphqlRepository
) : GraphqlUseCase<GetShopStateInfoResponse>(gqlRepository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setGraphqlQuery(GetShopStateInfoGqlQuery())
        setTypeClass(GetShopStateInfoResponse::class.java)
    }

    suspend fun executeInBackground(
        shopId: String,
        dataKey: String,
        pageSource: String
    ): ShopStateInfoUiModel {
        val requestParams = createRequestParam(shopId, dataKey, pageSource)
        setRequestParams(requestParams.parameters)

        try {
            val data: GetShopStateInfoResponse = executeOnBackground()
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
                PARAM_DATA_KEYS, listOf(
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
                  dataKey
                  imageUrl
                  title
                  subtitle
                  button {
                    name
                    applink
                  }
                  buttonAlt {
                    name
                    applink
                  }
                  meta {
                    shopState
                  }
                  widgetDataSign
                  subType
                  state
                }
              }
            }
        """

        private const val PARAM_DATA_KEYS = "dataKeys"
    }
}