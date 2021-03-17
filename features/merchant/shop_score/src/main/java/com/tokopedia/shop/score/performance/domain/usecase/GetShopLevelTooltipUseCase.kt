package com.tokopedia.shop.score.performance.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.score.performance.domain.mapper.ShopScoreMapper
import com.tokopedia.shop.score.performance.domain.model.ShopLevelTooltipParam
import com.tokopedia.shop.score.performance.domain.model.ShopLevelTooltipResponse
import com.tokopedia.shop.score.performance.presentation.model.ShopInfoLevelUiModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetShopLevelTooltipUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository,
        private val shopScoreMapper: ShopScoreMapper
) : UseCase<ShopInfoLevelUiModel>() {

    companion object {
        //need adjust type input
        const val SHOP_LEVEL_INPUT = "input"
        val SHOP_LEVEL_TOOLTIP_QUERY = """
            query shopLevel(${'$'}input: ShopLevelRequest!){
              shopLevel(input: ${'$'}input){
                result {
                  period
                  nextUpdate
                  shopLevel
                  itemSold
                  niv
                }
                error {
                  message
                }
              }
            }
        """.trimIndent()

        @JvmStatic
        fun createParams(shopLevelTooltipParam: ShopLevelTooltipParam): Map<String, ShopLevelTooltipParam> =
                mapOf(SHOP_LEVEL_INPUT to shopLevelTooltipParam)
    }

    var params = mapOf<String, ShopLevelTooltipParam>()

    override suspend fun executeOnBackground(): ShopInfoLevelUiModel {
        val gqlRequest = GraphqlRequest(SHOP_LEVEL_TOOLTIP_QUERY, ShopLevelTooltipResponse::class.java, params)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))
        val error = gqlResponse.getError(GraphqlError::class.java)
        val gqlResult = gqlResponse.getData<ShopLevelTooltipResponse>(ShopLevelTooltipResponse::class.java).shopLevel
        if (error.isNullOrEmpty()) {
            return shopScoreMapper.mapToShoInfoLevelUiModel(gqlResult.result)
        } else {
            throw MessageErrorException(gqlResult.error.message)
        }
    }
}