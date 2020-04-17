package com.tokopedia.talk.feature.reply.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class DiscussionDataByQuestionIDUseCase @Inject constructor(graphqlRepository: GraphqlRepository)  {

    companion object {

        const val PARAM_QUESTION_ID = "questionID"
        const val PARAM_SHOP_ID = "shopID"

        private val query by lazy {
            val questionID = "\$questionID"
            val shopID = "\$shopID"
            """
                query discussionDataByQuestionID($questionID: String!, $shopID: String!) {
                    discussionDataByQuestionID(questionID: $questionID, shopID: $shopID) {
                        question {
                            content
                            maskedContent
                            userName
                            userID
                            createTime
                            createTimeFormatted
                            
                            likeCount
                            state {
                                allowReply
                                allowUnmask
                                allowReport
                                allowFollow
                                allowDelete
                                isFollowed
                            }
                            
                            totalAnswer
                            answer {
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
                                    isFollowed
                                    isLiked
                                    allowLike
                                    allowReply
                                    allowUnmask
                                    allowReport
                                    allowFollow
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
}