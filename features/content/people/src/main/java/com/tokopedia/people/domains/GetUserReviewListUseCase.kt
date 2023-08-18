package com.tokopedia.people.domains

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.people.model.GetUserReviewListRequest
import com.tokopedia.people.model.GetUserReviewListResponse
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 12, 2023
 */
@GqlQuery(GetUserReviewListUseCase.QUERY_NAME, GetUserReviewListUseCase.QUERY)
class GetUserReviewListUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    @ApplicationContext private val repository: GraphqlRepository,
) : CoroutineUseCase<GetUserReviewListRequest, GetUserReviewListResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = GetUserReviewListUseCaseQuery().getQuery()

    override suspend fun execute(params: GetUserReviewListRequest): GetUserReviewListResponse {
        val param = mapOf(
            PARAM_USER_ID to params.userID,
            PARAM_LIMIT to params.limit,
            PARAM_PAGE to params.page,
        )

        return repository.request(graphqlQuery(), param)
    }

    companion object {
        private const val PARAM_USER_ID = "userID"
        private const val PARAM_LIMIT = "limit"
        private const val PARAM_PAGE = "page"

        const val QUERY_NAME = "GetUserReviewListUseCaseQuery"
        const val QUERY = """
            query ProductrevGetUserReviewList(
                ${"$${PARAM_USER_ID}"}: String!,
                ${"$${PARAM_LIMIT}"}: Int!,
                ${"$${PARAM_PAGE}"}: Int!
            ) {
                productrevGetUserReviewList(
                    ${PARAM_USER_ID}: ${"$${PARAM_USER_ID}"},
                    ${PARAM_LIMIT}: ${"$${PARAM_LIMIT}"},
                    ${PARAM_PAGE}: ${"$${PARAM_PAGE}"}
                ) {
                  list {
                    feedbackID
                    product {
                        productID
                        productName
                        productImageURL
                        productPageURL
                        productStatus
                        productVariant {
                            variantID
                            variantName
                        }
                    }
                    rating
                    reviewText
                    reviewTime
                    attachments {
                        attachmentID
                        thumbnailURL
                        fullsizeURL
                    }
                    videoAttachments {
                        attachmentID
                        videoUrl
                    }
                    likeDislike {
                        totalLike
                        likeStatus
                    }
                  }
                  hasNext
                }
            }
        """
    }
}
