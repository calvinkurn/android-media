package com.tokopedia.activation.domain

import com.tokopedia.activation.domain.mapper.GetShopFeatureMapper
import com.tokopedia.activation.model.ShopFeatureModel
import com.tokopedia.activation.model.response.GetShopFeatureResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetShopFeatureUseCase @Inject constructor(val graphqlUseCase: GraphqlUseCase<GetShopFeatureResponse>, private val mapper: GetShopFeatureMapper) : UseCase<ShopFeatureModel>() {

    override suspend fun executeOnBackground(): ShopFeatureModel {
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setRequestParams(mapOf(
                PARAM_TYPE to useCaseRequestParams.getInt(PARAM_TYPE, 1),
                PARAM_SHOP_ID to useCaseRequestParams.getString(PARAM_SHOP_ID, "")
        ))
        graphqlUseCase.setTypeClass(GetShopFeatureResponse::class.java)
        val result = graphqlUseCase.executeOnBackground()
        return mapper.convertToUIModel(result.shopFeature.shopData)
    }

    fun generateRequestParams(shopId: String): RequestParams {
        return RequestParams.create().apply {
            putInt(PARAM_TYPE, 1)
            putString(PARAM_SHOP_ID, shopId)
        }
    }

    companion object {
        const val PARAM_TYPE = "type"
        const val PARAM_SHOP_ID = "shopId"

        const val SUCCESS = "success"

        val QUERY = """
            query shopFeature(${"$"}type: Int!, ${"$"}shopId: String)
            {
              shopFeature(type: ${"$"}type, shopID: ${"$"}shopId) {
                data{
                  title
                  type
                  value
                }
              }
            }
        """.trimIndent()
    }

}