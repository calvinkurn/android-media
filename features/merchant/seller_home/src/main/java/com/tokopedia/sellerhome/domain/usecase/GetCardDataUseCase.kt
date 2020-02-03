package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.sellerhome.GraphqlQuery
import com.tokopedia.sellerhome.domain.mapper.CardMapper
import com.tokopedia.sellerhome.domain.model.GetCardDataResponse
import com.tokopedia.sellerhome.util.getData
import com.tokopedia.sellerhome.util.toJson
import com.tokopedia.sellerhome.view.model.CardDataUiModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created By @ilhamsuaib on 2020-01-27
 */

class GetCardDataUseCase(
        private val gqlRepository: GraphqlRepository,
        private val cardMapper: CardMapper
) : UseCase<List<CardDataUiModel>>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): List<CardDataUiModel> {
        val gqlRequest = GraphqlRequest(GraphqlQuery.GET_CARD_DATA, GetCardDataResponse::class.java, params.parameters)
        val gqlResponse: GraphqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))

        val errors: List<GraphqlError>? = gqlResponse.getError(GetCardDataResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetCardDataResponse>()
            val widgetData = data.getCardData?.cardData.orEmpty()
            return cardMapper.mapRemoteModelToUiModel(widgetData)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        private const val SHOP_ID = "shopID"
        private const val DATA_KEY = "dataKey"
        private const val START_DATE = "startDate"
        private const val END_DATE = "endDate"

        fun getRequestParams(
                shopId: Int,
                dataKey: List<String>,
                startDate: String,
                endDate: String
        ): RequestParams = RequestParams.create().apply {
            putInt(SHOP_ID, shopId)
            putObject(DATA_KEY, dataKey)
            putString(START_DATE, startDate)
            putString(END_DATE, endDate)
        }
    }
}