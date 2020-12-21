package com.tokopedia.shop.open.domain


import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.open.data.model.SendSurveyData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class ShopOpenRevampSendSurveyUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase
): UseCase<SendSurveyData>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): SendSurveyData {
        val sendSurvey = GraphqlRequest(QUERY, SendSurveyData::class.java, params.parameters)
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
        private const val QUERY = "mutation sendSurveyData(\$input: ParamSendSurveyData!) {\n" +
                "  sendSurveyData(input: \$input){\n" +
                "    success\n" +
                "  \tmessage\n" +
                "  }\n" +
                "}"

        fun createRequestParams(paramData: Map<String, Any>): RequestParams = RequestParams.create().apply {
            putObject(INPUT, paramData)
        }

    }

}