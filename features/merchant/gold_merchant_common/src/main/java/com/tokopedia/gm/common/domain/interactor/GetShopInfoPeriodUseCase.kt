package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.data.source.cloud.model.PMPeriodTypeResponse
import com.tokopedia.gm.common.data.source.cloud.model.ParamShopInfoByID
import com.tokopedia.gm.common.data.source.cloud.model.ShopInfoByIDResponse
import com.tokopedia.gm.common.data.source.cloud.model.ShopInfoPeriodWrapperResponse
import com.tokopedia.gm.common.domain.mapper.ShopScoreCommonMapper
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetShopInfoPeriodUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val shopScoreCommonMapper: ShopScoreCommonMapper
) : GraphqlUseCase<ShopInfoPeriodUiModel>(graphqlRepository) {

    companion object {
        const val SHOP_ID = "shopIDs"
        const val SHOP_INFO_INPUT = "input"
        const val SOURCE = "source"
        val SHOP_INFO_ID_QUERY = """
            query shopInfoByID(${'$'}input: ParamShopInfoByID!) {
              shopInfoByID(input: ${'$'}input) {
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

    override suspend fun executeOnBackground(): ShopInfoPeriodUiModel {
        val shopInfoPeriodWrapperResponse = ShopInfoPeriodWrapperResponse()
        var shopInfoPeriodUiModel = ShopInfoPeriodUiModel()
        val shopId = requestParams.getInt(SHOP_ID, 0)

        val shopInfoParam = mapOf(SHOP_INFO_INPUT to ParamShopInfoByID(shopIDs = listOf(shopId)))
        val periodTypeParam = mapOf(SHOP_ID to shopId, SOURCE to GetPMPeriodTypeUseCase.GOLD_MERCHANT_SOURCE)

        val shopInfoRequest = GraphqlRequest(SHOP_INFO_ID_QUERY, ShopInfoByIDResponse::class.java, shopInfoParam)
        val periodTypeRequest = GraphqlRequest(GetPMPeriodTypeUseCase.PM_SETTING_INFO_QUERY, PMPeriodTypeResponse::class.java, periodTypeParam)

        val requests = mutableListOf(shopInfoRequest, periodTypeRequest)
        try {
            val gqlResponse = graphqlRepository.getReseponse(requests)
            if (!gqlResponse.getError(ShopInfoByIDResponse::class.java).isNullOrEmpty()) {
                shopInfoPeriodWrapperResponse.shopInfoByIDResponse = gqlResponse.getData<ShopInfoByIDResponse>(ShopInfoByIDResponse::class.java).shopInfoByID
            }

            if (!gqlResponse.getError(PMPeriodTypeResponse::class.java).isNullOrEmpty()) {
                shopInfoPeriodWrapperResponse.goldGetPMSettingInfo = gqlResponse.getData<PMPeriodTypeResponse>(PMPeriodTypeResponse::class.java).goldGetPMSettingInfo
            }
            shopInfoPeriodUiModel = shopScoreCommonMapper.mapToGetShopInfo(shopInfoPeriodWrapperResponse)
        } catch (e: Throwable) { }

        return shopInfoPeriodUiModel
    }
}