package com.tokopedia.activation.domain

import com.tokopedia.activation.domain.mapper.GetShopFeatureMapper
import com.tokopedia.activation.model.ShopFeatureModel
import com.tokopedia.activation.model.response.GetShopFeatureResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetShopFeatureUseCase @Inject constructor(val graphqlUseCase: GraphqlUseCase<GetShopFeatureResponse>, private val mapper: GetShopFeatureMapper) : UseCase<ShopFeatureModel>() {

   /* fun execute(shopId: String, onSuccess: (String) -> Unit, onError: (Throwable) -> Unit) {
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setRequestParams(mapOf(PARAM_TYPE to 1, PARAM_SHOP_ID to shopId))
        graphqlUseCase.setTypeClass(GetShopFeatureResponse::class.java)
        graphqlUseCase.execute({ response : GetShopFeatureResponse ->
            onSuccess(SUCCESS)
        }, {
            throwable:  Throwable  -> onError(throwable)
        })
    }*/

    override suspend fun executeOnBackground(): ShopFeatureModel {
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setRequestParams(mapOf(PARAM_TYPE to 1, PARAM_SHOP_ID to "shopId"))
        graphqlUseCase.setTypeClass(GetShopFeatureResponse::class.java)
        val result = graphqlUseCase.executeOnBackground()
        return mapper.convertToUIModel(result.data.shopFeature.shopData)
    }

    companion object {
        const val PARAM_TYPE = "type"
        const val PARAM_SHOP_ID = "shopId"

        const val SUCCESS = "success"

        val QUERY = """
            {
              shopFeature(type: ${"$"}type, shopID:${"$"}shopId) {
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