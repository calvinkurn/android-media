package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.data.source.cloud.model.PMPeriodTypeResponse
import com.tokopedia.gm.common.presentation.ShopInfoTransitionUiModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetShopInfoUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository
): GraphqlUseCase<ShopInfoTransitionUiModel>(graphqlRepository) {

    companion object {
        const val SHOP_ID = "shopID"
        const val SOURCE = "source"
        private const val GOLD_MERCHANT_SOURCE = "goldmerchant"
        const val PM_SHOP_INFO_QUERY = """
            query goldGetPMShopInfo(${'$'}shopID: Int!, ${'$'}source: String!){
              goldGetPMShopInfo(shopID: ${'$'}shopID, source: ${'$'}source) {
                data{
                  is_new_seller
                  shop_age
                }
              }
            }
        """

        @JvmStatic
        fun createParams(shopID: Int): RequestParams = RequestParams.create().apply {
            putInt(SHOP_ID, shopID)
            putString(SOURCE, GOLD_MERCHANT_SOURCE)
        }
    }

    var requestParams: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ShopInfoTransitionUiModel {
        val shopInfoTransition = ShopInfoTransitionUiModel()

        val shopId = requestParams.getInt(SHOP_ID, 0)
        val source = requestParams.getString(SOURCE, "")

        val shopInfoParam = mapOf(SHOP_ID to shopId, SOURCE to GOLD_MERCHANT_SOURCE)

        val periodTypeRequest = GraphqlRequest(GetPMPeriodTypeUseCase.PM_SETTING_INFO_QUERY, PMPeriodTypeResponse::class.java)

        return shopInfoTransition
    }
}