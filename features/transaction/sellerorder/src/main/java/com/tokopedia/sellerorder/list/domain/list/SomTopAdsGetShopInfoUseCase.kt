package com.tokopedia.sellerorder.list.domain.list

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.list.data.model.SomTopAdsGetShopInfoResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class SomTopAdsGetShopInfoUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomTopAdsGetShopInfoResponse.Data>) {

    init {
        useCase.setGraphqlQuery(QUERY)
    }

    suspend fun execute(shopId: Int): Result<SomTopAdsGetShopInfoResponse.Data.TopAdsGetShopInfo.SomTopAdsShopInfo> {
        useCase.setTypeClass(SomTopAdsGetShopInfoResponse.Data::class.java)
        useCase.setRequestParams(generateParam(shopId))

        return try {
            val orderList = useCase.executeOnBackground().topAdsGetShopInfo.somTopAdsShopInfo
            Success(orderList)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun generateParam(shopId: Int): Map<String, Any?> {
        return mapOf(SomConsts.PARAM_SHOP_ID to shopId)
    }

    companion object {
        val QUERY = """
            query topAdsGetShopInfo(${'$'}shop_id: Int!) {
              topAdsGetShopInfo(shop_id:${'$'}shop_id){
                data {
                  category
                }
              }
            }
        """.trimIndent()
    }
}