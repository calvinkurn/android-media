package com.tokopedia.talk.feature.reading.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.reading.data.model.discussiondata.DiscussionDataResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetDiscussionDataUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<DiscussionDataResponseWrapper>(graphqlRepository) {

    companion object {

        const val PARAM_PRODUCT_ID = "productID"
        const val PARAM_SHOP_ID = "shopID"
        const val PARAM_PAGE = "page"
        const val PARAM_LIMIT = "limit"
        const val PARAM_SORT = "sortBy"
        const val PARAM_CATEGORY = "category"
        private const val TALK_DISCUSSION_DATA_QUERY_CLASS_NAME = "TalkDiscussionData"
        private const val query =
            """
            query discussionDataByProductID(${'$'}productID: String!, ${'$'}shopID: String, ${'$'}page: Int!, ${'$'}limit: Int!, ${'$'}sortBy: String, ${'$'}category: String) {
              discussionDataByProductID(productID: ${'$'}productID, shopID: ${'$'}shopID, page: ${'$'}page, limit: ${'$'}limit, sortBy: ${'$'}sortBy, category: ${'$'}category) {
                shopID
                shopURL
                hasNext
                totalQuestion
                question {
                  questionID
                  content
                  maskedContent
                  userName
                  userThumbnail
                  userID
                  createTime
                  createTimeFormatted
                  state {
                    isMasked
                    allowReply
                    isYours
                  }
                  totalAnswer
                  answer {
                    answerID
                    content
                    maskedContent
                    userName
                    userThumbnail
                    userID
                    isSeller
                    createTime
                    createTimeFormatted
                    likeCount
                    state {
                        isMasked
                        isLiked
                        allowLike
                        isYours
                    }
                    attachedProductCount
                  }
                }
              }
            }
        """
    }

    init {
        setupUseCase()
    }

    @GqlQuery(TALK_DISCUSSION_DATA_QUERY_CLASS_NAME, query)
    private fun setupUseCase() {
        setGraphqlQuery(TalkDiscussionData.GQL_QUERY)
        setTypeClass(DiscussionDataResponseWrapper::class.java)
    }

    fun setParams(productId: String, shopId: String, page: Int, limit: Int, sortBy: String, category: String) {
        setRequestParams(
                RequestParams().apply {
                    putString(PARAM_PRODUCT_ID, productId)
                    putString(PARAM_SHOP_ID, shopId)
                    putInt(PARAM_PAGE, page)
                    putInt(PARAM_LIMIT, limit)
                    putString(PARAM_SORT, sortBy)
                    putString(PARAM_CATEGORY, category)
                }.parameters
        )
    }
}