package com.tokopedia.talk.feature.reply.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.reply.data.model.discussion.DiscussionDataByQuestionIDResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class DiscussionDataByQuestionIDUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<DiscussionDataByQuestionIDResponseWrapper>(graphqlRepository)  {

    companion object {

        const val PARAM_QUESTION_ID = "questionID"
        const val PARAM_SHOP_ID = "shopID"

        private val query by lazy {
            val questionID = "\$questionID"
            val shopID = "\$shopID"
            """
                query discussionDataByQuestionID($questionID: String!, $shopID: String) {
                  discussionDataByQuestionID(questionID: $questionID, shopID: $shopID) {
                    maxAnswerLength
                    question {
                      questionID
                      content
                      maskedContent
                      userName
                      userID
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
            """.trimIndent()
        }
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(DiscussionDataByQuestionIDResponseWrapper::class.java)
    }

    fun setParams(questionId: String, shopId: String) {
        val requestParams = RequestParams()
        requestParams.putString(PARAM_QUESTION_ID, questionId)
        requestParams.putString(PARAM_SHOP_ID, shopId)
        setRequestParams(requestParams.parameters)
    }
}