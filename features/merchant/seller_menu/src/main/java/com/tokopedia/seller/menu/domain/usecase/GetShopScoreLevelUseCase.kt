package com.tokopedia.seller.menu.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.menu.domain.query.ShopScoreLevelParam
import com.tokopedia.seller.menu.domain.query.ShopScoreLevelResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetShopScoreLevelUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository
): GraphqlUseCase<ShopScoreLevelResponse>(graphqlRepository) {

    companion object {
        const val PARAM_INPUT = "input"
        val SHOP_SCORE_LEVEL_QUERY = """
            query shopScoreLevel(${'$'}input: ShopScoreLevelParam!){
              shopScoreLevel(input: ${'$'}input){
                result {
                  shopScore
                  shopLevel
                }
                error {
                  message
                }
              }
            }
        """.trimIndent()
    }

    init {
        setGraphqlQuery(SHOP_SCORE_LEVEL_QUERY)
        setTypeClass(ShopScoreLevelResponse::class.java)
    }

    suspend fun execute(shopId: String): ShopScoreLevelResponse.ShopScoreLevel.Result {
        val shopScoreParam = ShopScoreLevelParam(shopID = shopId)
        val requestParams = RequestParams().apply {
            putObject(PARAM_INPUT, shopScoreParam)
        }.parameters

        setRequestParams(requestParams)

        val getShopScore = executeOnBackground()
        val data = getShopScore.shopScoreLevel.result
        val errorMessage = getShopScore.shopScoreLevel.error.message

        if(errorMessage.isEmpty()) {
            return data
        } else {
            throw MessageErrorException(errorMessage)
        }
    }
}

