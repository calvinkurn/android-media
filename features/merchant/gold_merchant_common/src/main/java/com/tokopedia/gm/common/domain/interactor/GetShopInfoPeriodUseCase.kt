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
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetShopInfoPeriodUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val shopScoreCommonMapper: ShopScoreCommonMapper
) : GraphqlUseCase<ShopInfoPeriodUiModel>(graphqlRepository) {

    companion object {
        const val SHOP_ID = "shopIDs"
        const val SHOP_INFO_INPUT = "input"
        const val SHOP_ID_PM = "shopID"
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

        const val GOLD_MERCHANT_SOURCE = "goldmerchant"
        val PM_SETTING_INFO_QUERY = """
            query goldGetPMSettingInfo(${'$'}shopID: Int!, ${'$'}source: String!){
              goldGetPMSettingInfo(shopID: ${'$'}shopID, source: ${'$'}source) {
                 period_type
                 period_start_date_time
                 period_end_date_time
              }
            }
        """.trimIndent()

        @JvmStatic
        fun createParams(shopID: Int): RequestParams = RequestParams.create().apply {
            putLong(SHOP_ID, shopID.toLong())
        }
    }

    var requestParams: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ShopInfoPeriodUiModel {
        val shopInfoPeriodWrapperResponse = ShopInfoPeriodWrapperResponse()
        val shopId = requestParams.getLong(SHOP_ID, 0)

        val shopInfoParam = mapOf(SHOP_INFO_INPUT to ParamShopInfoByID(shopIDs = listOf(shopId)))
        val periodTypeParam = mapOf(SHOP_ID_PM to shopId, SOURCE to GOLD_MERCHANT_SOURCE)

        val shopInfoRequest = GraphqlRequest(SHOP_INFO_ID_QUERY, ShopInfoByIDResponse::class.java, shopInfoParam)
        val periodTypeRequest = GraphqlRequest(PM_SETTING_INFO_QUERY, PMPeriodTypeResponse::class.java, periodTypeParam)

        val requests = mutableListOf(shopInfoRequest, periodTypeRequest)
        try {
            val gqlResponse = graphqlRepository.getReseponse(requests)
            if (gqlResponse.getError(ShopInfoByIDResponse::class.java).isNullOrEmpty()) {
                shopInfoPeriodWrapperResponse.shopInfoByIDResponse = gqlResponse.getData<ShopInfoByIDResponse>(ShopInfoByIDResponse::class.java).shopInfoByID
            } else {
                val dataError = gqlResponse.getError(ShopInfoByIDResponse::class.java).joinToString { it.message }
                throw MessageErrorException(dataError)
            }

            if (gqlResponse.getError(PMPeriodTypeResponse::class.java).isNullOrEmpty()) {
                shopInfoPeriodWrapperResponse.goldGetPMSettingInfo = gqlResponse.getData<PMPeriodTypeResponse>(PMPeriodTypeResponse::class.java).goldGetPMSettingInfo
            } else {
                val dataError = gqlResponse.getError(PMPeriodTypeResponse::class.java).joinToString { it.message }
                throw MessageErrorException(dataError)
            }
            return shopScoreCommonMapper.mapToGetShopInfo(shopInfoPeriodWrapperResponse)
        } catch (e: Throwable) {
            throw MessageErrorException(e.message)
        }
    }
}