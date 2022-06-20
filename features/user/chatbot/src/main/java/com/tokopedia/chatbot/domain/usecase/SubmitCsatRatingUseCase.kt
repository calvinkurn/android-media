package com.tokopedia.chatbot.domain.usecase

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.domain.gqlqueries.SubmitCsatRatingQuery
import com.tokopedia.chatbot.domain.gqlqueries.queries.SUBMIT_CSAT_RATING
import com.tokopedia.chatbot.domain.pojo.csatRating.csatInput.InputItem
import com.tokopedia.chatbot.domain.pojo.csatRating.csatResponse.SubmitCsatGqlResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber
import javax.inject.Inject


@GqlQuery("submitRatingCSAT", SUBMIT_CSAT_RATING)
class SubmitCsatRatingUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<SubmitCsatGqlResponse>(graphqlRepository) {

    fun submitCsatRating(
        onSuccess: (SubmitCsatGqlResponse) -> Unit,
        onError: (Throwable) -> Unit,
        input: InputItem
    ) {
        try {
            this.setTypeClass(SubmitCsatGqlResponse::class.java)
            this.setRequestParams(generateParam(input))
            this.setGraphqlQuery(SubmitCsatRatingQuery())

            this.execute(
                { result ->
                    onSuccess(result)
                }, { error ->
                    onError(error)
                }
            )

        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun generateParam(input: InputItem): Map<String, Any> {
        val requestParams = HashMap<String, Any>()
        requestParams[PARAM_INPUT] = input
        return requestParams
    }

    companion object {
        private val PARAM_INPUT: String = "input"
    }

}