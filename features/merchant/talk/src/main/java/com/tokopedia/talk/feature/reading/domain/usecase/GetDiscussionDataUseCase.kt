package com.tokopedia.talk.feature.reading.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class GetDiscussionDataUseCase @Inject constructor(graphqlRepository: GraphqlRepository)  {

    companion object {
        private val query = """
            query discussionDataByProductID(
                productID: Int!,
                page: Int!,
                limit: Int!,
                sortBy: String,
                category: String
            ) {
                hasNext
                totalQuestion
                question {
                    content
                    maskedContent
                    userName
                    userID
                    createTime
                    createTimeFormatted
                    
                    likeCount
                    state {
                        isMasked
                        isLiked
                        allowLike
                    }
                    
                    totalAnswer
                    answer {
                        content
                        userName
                        userID
                        createTime
                        createTimeFormatted
                        attachedProductCount
                    }
                }
            }
        """.trimIndent()
    }
}