package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.data.source.cloud.model.PMPeriodTypeResponse
import com.tokopedia.gm.common.data.source.cloud.model.ShopInfoByIDResponse
import com.tokopedia.gm.common.domain.mapper.ShopScoreMapper
import com.tokopedia.gm.common.presentation.ShopInfoTransitionUiModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetShopInfoUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ShopInfoTransitionUiModel>(graphqlRepository) {

    companion object {
        const val SHOP_ID = "shopID"
        const val SOURCE = "source"
        private const val SELLER_APP_SOURCE = "sellerapp"
        val SHOP_INFO_ID_QUERY = """
            query shopInfoByID(${'$'}shopIDs: Int!, ${'$'}source: String!) {
              shopInfoByID(input:{shopIDs:[${'$'}shopID], fields:["create_info"], source: ${'$'}source}) {
                result{
                  createInfo{
                    shopCreated
                  }
                }
                error{
                  message
                }
              }
            }
        """.trimIndent()

        @JvmStatic
        fun createParams(shopID: Int): RequestParams = RequestParams.create().apply {
            putInt(SHOP_ID, shopID)
        }
    }

    var requestParams: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ShopInfoTransitionUiModel {
        var shopInfoTransition = ShopInfoTransitionUiModel()
        val shopId = requestParams.getInt(SHOP_ID, 0)

        val shopInfoParam = mapOf(SHOP_ID to shopId, SOURCE to SELLER_APP_SOURCE)
        val periodTypeParam = mapOf(SHOP_ID to shopId, SOURCE to GetPMPeriodTypeUseCase.GOLD_MERCHANT_SOURCE)

        val shopInfoRequest = GraphqlRequest(SHOP_INFO_ID_QUERY, ShopInfoByIDResponse::class.java, shopInfoParam)
        val periodTypeRequest = GraphqlRequest(GetPMPeriodTypeUseCase.PM_SETTING_INFO_QUERY, PMPeriodTypeResponse::class.java, periodTypeParam)

        val requests = mutableListOf(shopInfoRequest, periodTypeRequest)
        try {
            val gqlResponse = graphqlRepository.getReseponse(requests)
            if (!gqlResponse.getError(ShopInfoByIDResponse::class.java).isNullOrEmpty() &&
                    !gqlResponse.getError(PMPeriodTypeResponse::class.java).isNullOrEmpty()) {
                val shopInfoData = gqlResponse.getData<ShopInfoByIDResponse>(ShopInfoByIDResponse::class.java).shopInfoByID
                val periodTypeData = gqlResponse.getData<PMPeriodTypeResponse>(PMPeriodTypeResponse::class.java).goldGetPMSettingInfo.data
                shopInfoTransition = ShopScoreMapper.mapToGetShopInfo(shopInfoData, periodTypeData)
            }
        } catch (e: Throwable) { }

        return shopInfoTransition
    }
}