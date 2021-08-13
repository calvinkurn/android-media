package com.tokopedia.talk.feature.reply.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.reply.data.model.discussion.DiscussionDataByQuestionIDResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class DiscussionDataByQuestionIDUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<DiscussionDataByQuestionIDResponseWrapper>(graphqlRepository)  {

    companion object {

        const val PARAM_QUESTION_ID = "questionID"
        const val PARAM_SHOP_ID = "shopID"
        private const val TALK_DISCUSSION_DATA_BY_QUESTION_ID_QUERY_CLASS_NAME = "TalkDiscussionDataByQuestionId"
        private const val query =
            """
                query discussionDataByQuestionID(${'$'}questionID: String!, ${'$'}shopID: String) {
                  discussionDataByQuestionID(questionID: ${'$'}questionID, shopID: ${'$'}shopID) {
                    shopID
                    shopURL
                    productName
                    productID
                    thumbnail
                    isSellerView
                    url
                    maxAnswerLength
                    productStock
                    productStockMessage
                    isSellerView
                    question {
                      questionID
                      content
                      maskedContent
                      userName
                      userID
                      userThumbnail
                      createTime
                      createTimeFormatted
                      state {
                        allowReply
                        allowUnmask
                        allowReport
                        allowDelete
                        allowFollow
                        isFollowed
                        isMasked
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
                          allowUnmask
                          allowReport
                          allowDelete
                          isYours
                          isAutoReplied
                        }
                        attachedProductCount
                        attachedProduct {
                          productID
                          name
                          priceFormatted
                          url
                          thumbnail
                        }
                      }
                    }
                  }
                }
            """
    }

    init {
        setupUseCase()
    }

    @GqlQuery(TALK_DISCUSSION_DATA_BY_QUESTION_ID_QUERY_CLASS_NAME, query)
    private fun setupUseCase() {
        setGraphqlQuery(TalkDiscussionDataByQuestionId.GQL_QUERY)
        setTypeClass(DiscussionDataByQuestionIDResponseWrapper::class.java)
    }

    fun setParams(questionId: String, shopId: String) {
        setRequestParams(RequestParams.create().apply {
            putString(PARAM_QUESTION_ID, questionId)
            if(shopId.isNotEmpty()) {
                putString(PARAM_SHOP_ID, shopId)
            }
        }.parameters)
    }
}