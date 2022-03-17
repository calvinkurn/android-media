package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.data.source.cloud.model.ParamShopInfoByID
import com.tokopedia.gm.common.data.source.cloud.model.ShopInfoByIDResponse
import com.tokopedia.gm.common.data.source.cloud.model.ShopInfoPeriodWrapperResponse
import com.tokopedia.gm.common.domain.mapper.ShopScoreCommonMapper
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import java.io.IOException
import javax.inject.Inject

open class GetShopCreatedInfoUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    private val shopScoreCommonMapper: ShopScoreCommonMapper
) : BaseGqlUseCase<ShopInfoPeriodUiModel>() {

    companion object {
        const val SHOP_ID = "shopIDs"
        const val SHOP_INFO_INPUT = "input"
        const val SOURCE = "source"
        private const val SHOP_ID_DEFAULT = 0L
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
        fun createParams(shopID: Long): RequestParams = RequestParams.create().apply {
            putLong(SHOP_ID, shopID)
        }
    }

    var requestParams: RequestParams = RequestParams.create()

    override suspend fun executeOnBackground(): ShopInfoPeriodUiModel {
        val shopInfoPeriodWrapperResponse = ShopInfoPeriodWrapperResponse()
        val shopId = requestParams.getLong(SHOP_ID, SHOP_ID_DEFAULT)

        val shopInfoParam = mapOf(SHOP_INFO_INPUT to ParamShopInfoByID(shopIDs = listOf(shopId)))

        val shopInfoRequest =
            GraphqlRequest(SHOP_INFO_ID_QUERY, ShopInfoByIDResponse::class.java, shopInfoParam)

        val requests = mutableListOf(shopInfoRequest)
        try {
            val gqlResponse = graphqlRepository.response(requests, cacheStrategy)
            if (gqlResponse.getError(ShopInfoByIDResponse::class.java).isNullOrEmpty()) {
                shopInfoPeriodWrapperResponse.shopInfoByIDResponse =
                    gqlResponse.getData<ShopInfoByIDResponse>(ShopInfoByIDResponse::class.java).shopInfoByID
            } else {
                val dataError = gqlResponse.getError(ShopInfoByIDResponse::class.java)
                    .joinToString { it.message }
                throw MessageErrorException(dataError)
            }
            return shopScoreCommonMapper.mapToGetShopInfo(shopInfoPeriodWrapperResponse)
        } catch (e: IOException) {
            throw IOException(e.message)
        } catch (e: Throwable) {
            throw Exception(e.message)
        }
    }
}