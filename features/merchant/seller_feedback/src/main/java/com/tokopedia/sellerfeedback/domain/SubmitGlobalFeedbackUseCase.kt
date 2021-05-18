package com.tokopedia.sellerfeedback.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerfeedback.data.SubmitGlobalFeedbackResponseWrapper
import com.tokopedia.sellerfeedback.presentation.SellerFeedback
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class SubmitGlobalFeedbackUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository
) : GraphqlUseCase<SubmitGlobalFeedbackResponseWrapper>(graphqlRepository) {
    companion object {
        const val PARAM_SHOPID = "shopID"
        const val PARAM_FEEDBACKSCORE = "feedbackScore"
        const val PARAM_FEEDBACKTYPE = "feedbackType"
        const val PARAM_FEEDBACKPAGE = "feedbackPage"
        const val PARAM_FEEDBACKDETAIL = "feedbackDetail"
        const val PARAM_UPLOADID1 = "uploadID1"
        const val PARAM_UPLOADID2 = "uploadID2"
        const val PARAM_UPLOADID3 = "uploadID3"
        const val SUBMIT_GLOBAL_FEEDBACK_QUERY_CLASS_NAME = "SubmitGlobalFeedback"
        const val SUBMIT_GLOBAL_FEEDBACK_QUERY = """
            query submitGlobalFeedback(
                ${'$'}shopID: Int!,
                ${'$'}feedbackScore: String!,
                ${'$'}feedbackType: String!,
                ${'$'}feedbackPage: String!,
                ${'$'}feedbackDetail: String!,
                ${'$'}uploadID1: String!,
                ${'$'}uploadID2: String,
                ${'$'}uploadID3: String
            ) {
                submitGlobalFeedback(
                    shopID: ${'$'}shopID
                    feedbackScore: ${'$'}feedbackScore
                    feedbackType: ${'$'}feedbackType
                    feedbackPage: ${'$'}feedbackPage
                    feedbackDetail: ${'$'}feedbackDetail
                    uploadID1: ${'$'}uploadID1
                    uploadID2: ${'$'}uploadID2
                    uploadID3: ${'$'}uploadID3
                ) {
                    state
                    error
                    errorMsg
                }
            }
        """
    }

    init {
        init()
    }

    @GqlQuery(SUBMIT_GLOBAL_FEEDBACK_QUERY_CLASS_NAME, SUBMIT_GLOBAL_FEEDBACK_QUERY)
    private fun init() {
        setTypeClass(SubmitGlobalFeedbackResponseWrapper::class.java)
        setGraphqlQuery(SubmitGlobalFeedback.GQL_QUERY)
    }

    fun setParams(sellerFeedback: SellerFeedback) {
        setRequestParams(RequestParams.create().apply {
            putInt(PARAM_SHOPID, sellerFeedback.shopId)
            putString(PARAM_FEEDBACKSCORE, sellerFeedback.feedbackScore)
            putString(PARAM_FEEDBACKTYPE, sellerFeedback.feedbackType)
            putString(PARAM_FEEDBACKPAGE, sellerFeedback.feedbackPage)
            putString(PARAM_FEEDBACKDETAIL, sellerFeedback.feedbackDetail)
            putString(PARAM_UPLOADID1, sellerFeedback.uploadId1)
            sellerFeedback.uploadId2?.let { putString(PARAM_UPLOADID2, sellerFeedback.uploadId2) }
            sellerFeedback.uploadId3?.let { putString(PARAM_UPLOADID3, sellerFeedback.uploadId3) }

        }.parameters)
    }
}