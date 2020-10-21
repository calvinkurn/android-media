package com.tokopedia.sellerorder.list.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.list.domain.model.SomListGetShopTopAdsCategoryResponse
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class SomListGetTopAdsCategoryUseCase @Inject constructor(
        private val useCase: GraphqlUseCase<SomListGetShopTopAdsCategoryResponse.Data>
) : BaseGraphqlUseCase() {

    init {
        useCase.setGraphqlQuery(QUERY)
    }

    suspend fun execute(): Success<Int> {
        useCase.setTypeClass(SomListGetShopTopAdsCategoryResponse.Data::class.java)
        useCase.setRequestParams(params.parameters)
        return Success(useCase.executeOnBackground().topAdsGetShopInfo.data.category)
    }

    fun setParams(shopId: Int) {
        params.putInt(PARAM_SHOP_ID, shopId)
    }

    companion object {
        private const val PARAM_SHOP_ID = "shop_id"

        private val QUERY = """
           query topAdsGetShopInfo(${'$'}shop_id: Int!) {
              topAdsGetShopInfo(shop_id: ${'$'}shop_id) {
                data {
                  category
                }
              }
            } 
        """.trimIndent()
    }
}