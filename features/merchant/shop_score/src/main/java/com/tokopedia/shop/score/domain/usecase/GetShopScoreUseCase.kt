package com.tokopedia.shop.score.domain.usecase

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.score.domain.model.ShopScoreData
import com.tokopedia.shop.score.domain.model.ShopScoreResponse
import com.tokopedia.shop.score.domain.param.ShopScoreParam
import com.tokopedia.shop.score.domain.query.GetShopScore
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetShopScoreUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
): GraphqlUseCase<ShopScoreResponse>(gqlRepository) {

    companion object {
        private const val PARAM_INPUT = "input"
    }

    init {
        setGraphqlQuery(GetShopScore.QUERY)
        setTypeClass(ShopScoreResponse::class.java)
    }

    suspend fun execute(shopId: String): ShopScoreData {
        val shopScoreParam = ShopScoreParam(shopId)
        val requestParams = RequestParams().apply {
            putObject(PARAM_INPUT, shopScoreParam)
        }.parameters

        setRequestParams(requestParams)

        val getShopScore = executeOnBackground()
        val data = getShopScore.data
        val errorMessage = data.error?.message

        if(errorMessage.isNullOrEmpty()) {
            return data
        } else {
            throw MessageErrorException(errorMessage)
        }
    }
}