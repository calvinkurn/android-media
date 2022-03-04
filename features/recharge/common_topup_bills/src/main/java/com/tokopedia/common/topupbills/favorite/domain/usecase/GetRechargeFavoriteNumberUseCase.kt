package com.tokopedia.common.topupbills.favorite.domain.usecase

import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberData
import com.tokopedia.common.topupbills.data.requests.DigiPersoRequestParam
import com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetRechargeFavoriteNumberUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository
): GraphqlUseCase<TopupBillsPersoFavNumberData>(graphqlRepository) {
    
    private var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): TopupBillsPersoFavNumberData {
        val gqlRequest = GraphqlRequest(
            CommonTopupBillsGqlQuery.rechargePersoFavoriteNumber,
            TopupBillsPersoFavNumberData::class.java,
            params.parameters
        )
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest))

        val error = gqlResponse.getError(TopupBillsPersoFavNumberData::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData(TopupBillsPersoFavNumberData::class.java)
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.toString())
        }
    }

    fun setRequestParams(categoryIds: List<Int>, operatorIds: List<Int> = listOf()){
        params = RequestParams.create().apply {
            putObject(
                FAVORITE_NUMBER_PARAM_INPUT, DigiPersoRequestParam(
                    channelName = CHANNEL_NAME,
                    clientNumbers = listOf(),
                    dgCategoryIDs = categoryIds,
                    pgCategoryIDs = listOf(),
                    dgOperatorIds = operatorIds
                )
            )
        }
    }

    companion object {
        const val CHANNEL_NAME = "favorite_number_list"
        const val FAVORITE_NUMBER_PARAM_INPUT = "input"
    }
}