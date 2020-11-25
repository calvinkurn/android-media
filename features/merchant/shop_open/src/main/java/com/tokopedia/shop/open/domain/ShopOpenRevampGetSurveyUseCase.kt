package com.tokopedia.shop.open.domain

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.open.data.model.GetSurveyData
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class ShopOpenRevampGetSurveyUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase
): UseCase<GetSurveyData>() {

    override suspend fun executeOnBackground(): GetSurveyData {
        val surveyDataRequest = GraphqlRequest(QUERY, GetSurveyData::class.java)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(surveyDataRequest)
        val gqlResponse = graphqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(GetSurveyData::class.java) ?: listOf()
        if (error == null || error.isEmpty()) {
            return gqlResponse.run {
                getData<GetSurveyData>(GetSurveyData::class.java)
            }
        } else {
            throw  MessageErrorException(error.mapNotNull {
                it.message
            }.joinToString(separator = ", "))
        }
    }

    companion object {
        private const val QUERY = "query {\n" +
                "  getSurveyData(id:1){\n" +
                "    result{\n" +
                "      questions{\n" +
                "        ID\n" +
                "        type\n" +
                "        question\n" +
                "        choices{\n" +
                "          ID\n" +
                "          choice\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "    error{\n" +
                "      message\n" +
                "    }\n" +
                "  }\n" +
                "}"
    }
}