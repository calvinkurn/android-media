package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.data.source.cloud.model.ShopScoreResponse
import com.tokopedia.gm.common.data.source.local.model.ShopScoreResultUiModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 03/06/21
 */

class GetShopScoreLevelUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository
) : BaseGqlUseCase<ShopScoreResultUiModel>() {

    override suspend fun executeOnBackground(): ShopScoreResultUiModel {
        val gqlRequest = GraphqlRequest(QUERY, ShopScoreResponse::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)
        val gqlErrors = gqlResponse.getError(ShopScoreResponse::class.java)
        if (gqlErrors.isNullOrEmpty()) {
            val data = gqlResponse.getData<ShopScoreResponse>(ShopScoreResponse::class.java)
            if (data != null) {
                return ShopScoreResultUiModel(data.shopScoreLevel?.result?.shopScore.orZero())
            } else {
                throw RuntimeException()
            }
        } else {
            throw MessageErrorException(gqlErrors.firstOrNull()?.message.orEmpty())
        }
    }

    companion object {
        private const val KEY_INPUT = "input"
        private const val KEY_SHOP_ID = "shopID"
        private const val KEY_SOURCE = "source"
        private const val KEY_CALCULATE_SHOP_SCORE = "calculateScore"
        private const val KEY_GET_NEXT_MIN_VALUE = "getNextMinValue"

        private val QUERY = """
            query getShopScoreLevel(${'$'}input: ShopScoreLevelParam!) {
              shopScoreLevel(input: ${'$'}input) {
                result {
                  shopScore
                }
              }
            }
        """.trimIndent()

        fun getRequestParams(shopId: String, source: String): RequestParams {
            return RequestParams.create().apply {
                putObject(KEY_INPUT, mapOf(
                        KEY_SHOP_ID to shopId,
                        KEY_SOURCE to source,
                        KEY_CALCULATE_SHOP_SCORE to true,
                        KEY_GET_NEXT_MIN_VALUE to false
                ))
            }
        }
    }
}