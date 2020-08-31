package com.tokopedia.shop.open.domain

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.open.common.GQLQueryConstant.QUERY_SHOP_OPEN_REVAMP_GET_SURVEY_DATA
import com.tokopedia.shop.open.data.model.GetSurveyData
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class ShopOpenRevampGetSurveyUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
        @Named(QUERY_SHOP_OPEN_REVAMP_GET_SURVEY_DATA) val queryGetSurveyData: String
): UseCase<GetSurveyData>() {

    override suspend fun executeOnBackground(): GetSurveyData {
        val surveyDataRequest = GraphqlRequest(queryGetSurveyData, GetSurveyData::class.java)
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

    companion object { }
}