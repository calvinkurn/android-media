package com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.SendSmileyReputationResponseWrapper
import com.tokopedia.review.feature.inbox.buyerreview.view.mapper.SendSmileyReputationResponseMapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * @author by nisie on 8/31/17.
 */

class SendSmileyReputationUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<SendSmileyReputationResponseWrapper.Data>,
    private val mapper: SendSmileyReputationResponseMapper
) {

    companion object {
        private const val PARAM_SCORE: String = "reputationScore"
        private const val PARAM_REPUTATION_ID: String = "reputationID"
        private const val PARAM_ROLE: String = "role"
        private const val I_AM_SELLER: Int = 2
        private const val I_AM_BUYER: Int = 1
        private const val REVIEW_IS_FROM_BUYER = 1

        private const val QUERY_NAME = "SendReputationSmiley"
        private const val QUERY = """
            mutation SendReputationSmiley(${'$'}reputationID: String!, ${'$'}reputationScore: Int!, ${'$'}role: Int!) {
              inboxReviewInsertReputationV2(reputationId: ${'$'}reputationID, reputationScore: ${'$'}reputationScore, buyerSeller: ${'$'}role) {
                success
              }
            }
        """

        fun getParam(reputationId: String?, score: String?, role: Int): RequestParams {
            val params: RequestParams = RequestParams.create()
            params.putInt(PARAM_SCORE, score.toIntOrZero())
            params.putString(PARAM_REPUTATION_ID, reputationId)
            params.putInt(PARAM_ROLE, getRole(role))
            return params
        }

        private fun getRole(role: Int): Int {
            return if (role == REVIEW_IS_FROM_BUYER) I_AM_SELLER else I_AM_BUYER
        }
    }

    init {
        useCase.setGraphqlQuery(SendReputationSmiley())
        useCase.setTypeClass(SendSmileyReputationResponseWrapper.Data::class.java)
    }

    @GqlQuery(QUERY_NAME, QUERY)
    suspend fun execute(params: RequestParams): Boolean {
        useCase.setRequestParams(params.parameters)
        return mapper.mapResponseToUiData(useCase.executeOnBackground().response)
    }
}