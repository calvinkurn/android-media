package com.tokopedia.review.feature.createreputation.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

@GqlQuery(
    ProductrevGetPostSubmitBottomSheetUseCase.POST_SUBMIT_BOTTOM_SHEET_QUERY_CLASS_NAME,
    ProductrevGetPostSubmitBottomSheetUseCase.POST_SUBMIT_BOTTOM_SHEET_QUERY
)
class ProductrevGetPostSubmitBottomSheetUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<com.tokopedia.review.feature.createreputation.model.ProductrevGetPostSubmitBottomSheetResponseWrapper>(
    graphqlRepository
) {

    init {
        setGraphqlQuery(GetPostSubmitBottomSheet.GQL_QUERY)
        setTypeClass(com.tokopedia.review.feature.createreputation.model.ProductrevGetPostSubmitBottomSheetResponseWrapper::class.java)
    }

    companion object {
        const val PARAM_FEEDBACK_ID = "feedbackID"
        const val PARAM_REVIEW_TEXT = "reviewText"
        const val PARAM_IMAGES_TOTAL = "imagesTotal"
        const val PARAM_IS_INBOX_EMPTY = "isInboxEmpty"
        const val PARAM_INCENTIVE_AMOUNT = "incentiveAmount"
        const val POST_SUBMIT_BOTTOM_SHEET_QUERY_CLASS_NAME = "GetPostSubmitBottomSheet"
        const val POST_SUBMIT_BOTTOM_SHEET_QUERY =
            """
                query productrevGetPostSubmitBottomSheet(${'$'}feedbackID: String!, ${'$'}reviewText: String, ${'$'}imagesTotal: Int, ${'$'}isInboxEmpty: Boolean, ${'$'}incentiveAmount: Int) {
                  productrevGetPostSubmitBottomSheet(feedbackID: ${'$'}feedbackID, reviewText: ${'$'}reviewText, imagesTotal: ${'$'}imagesTotal, isInboxEmpty: ${'$'}isInboxEmpty, incentiveAmount: ${'$'}incentiveAmount) {
                    type
                    title
                    description
                    imageURL
                    buttonList {
                      type
                      text
                      unifyType
                      unifyVariant
                      unifySize
                      webLink
                      appLink
                    }
                  }
                }
            """
    }

    fun setParams(requestParams: PostSubmitReviewRequestParams) {
        setRequestParams(requestParams.toRequestParamMap())
    }

    data class PostSubmitReviewRequestParams(
        val feedbackId: String,
        val reviewText: String,
        val imagesTotal: Int,
        val isInboxEmpty: Boolean,
        val incentiveAmount: Int
    ) {
        fun toRequestParamMap(): Map<String, Any> {
            return mapOf(
                PARAM_FEEDBACK_ID to feedbackId,
                PARAM_REVIEW_TEXT to reviewText,
                PARAM_IMAGES_TOTAL to imagesTotal,
                PARAM_IS_INBOX_EMPTY to isInboxEmpty,
                PARAM_INCENTIVE_AMOUNT to incentiveAmount
            )
        }
    }
}