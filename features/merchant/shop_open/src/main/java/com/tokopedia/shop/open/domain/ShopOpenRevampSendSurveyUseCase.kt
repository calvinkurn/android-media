package com.tokopedia.shop.open.domain


import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.open.common.GQLQueryConstant.QUERY_SHOP_OPEN_REVAMP_SEND_SURVEY_DATA
import com.tokopedia.shop.open.data.model.SendSurveyData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class ShopOpenRevampSendSurveyUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
        @Named(QUERY_SHOP_OPEN_REVAMP_SEND_SURVEY_DATA) val querySendSurvey: String
): UseCase<SendSurveyData>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): SendSurveyData {
        val sendSurvey = GraphqlRequest(querySendSurvey, SendSurveyData::class.java, params.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(sendSurvey)
        val gqlResponse = graphqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(SendSurveyData::class.java) ?: listOf()
        if (error == null || error.isEmpty()) {
            return gqlResponse.run {
                getData<SendSurveyData>(SendSurveyData::class.java)
            }
        } else {
            throw MessageErrorException(error.mapNotNull {
                it.message
            }.joinToString(separator = ", "))
        }
    }

    companion object {
        const val INPUT = "input"

        fun createRequestParams(paramData: Map<String, Any>): RequestParams = RequestParams.create().apply {
            putObject(INPUT, paramData)
        }

    }

}