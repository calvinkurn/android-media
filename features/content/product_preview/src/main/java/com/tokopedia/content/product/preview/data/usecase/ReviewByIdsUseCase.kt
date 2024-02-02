package com.tokopedia.content.product.preview.data.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.product.preview.data.response.ReviewByIdsResponse
import com.tokopedia.content.product.preview.data.usecase.ReviewByIdsUseCase.Companion.QUERY
import com.tokopedia.content.product.preview.data.usecase.ReviewByIdsUseCase.Companion.QUERY_NAME
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

@GqlQuery(QUERY_NAME, QUERY)
class ReviewByIdsUseCase @Inject constructor(
    @ApplicationContext private val repo: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<ReviewByIdsUseCase.Param, ReviewByIdsResponse>(dispatchers.io) {

    private val query: GqlQueryInterface = ReviewByIdsUseCaseQuery()

    override suspend fun execute(params: Param): ReviewByIdsResponse {
        return repo.request(query, params)
    }

    override fun graphqlQuery(): String = query.getQuery()

    data class Param(
        @SerializedName("ids")
        val ids: List<String>,
        @SerializedName("source")
        val source: String = "content"
    ) : GqlParam

    companion object {
        const val QUERY_NAME = "ReviewByIdsUseCaseQuery"
        const val QUERY = """
            query getProductReviewsByIDs(${'$'}ids: [String!], ${'$'}source: String!) {
              productrevGetProductReviewsByIDs(ids: ${'$'}ids, source: ${'$'}source) {
                reviews {
                  feedbackID
                  variantName
                  message
                  productRating
                  reviewCreateTime
                  reviewCreateTimestamp
                  isAnonymous
                  isReportable
                  reviewResponse {
                    message
                    createTime
                  }
                  imageAttachments {
                    attachmentID
                    description
                    imageThumbnailUrl
                    imageUrl
                  }
                  videoAttachments {
                    attachmentID
                    videoUrl
                  }
                  user {
                    userID
                    fullName
                    image
                    url
                    label
                  }
                  likeDislike {
                    totalLike
                    likeStatus
                  }
                  stats {
                    key
                    formatted
                    count
                  }
                  shop {
                    shopID
                    name
                    url
                    image
                  }
                }
              }
            }
        """
    }
}
