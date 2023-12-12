package com.tokopedia.content.product.preview.data.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.product.preview.data.MediaReviewResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * @author by astidhiyaa on 05/12/23
 */
@GqlQuery(MediaReviewUseCase.QUERY_NAME, MediaReviewUseCase.QUERY)
class MediaReviewUseCase @Inject constructor(
    @ApplicationContext private val repo: GraphqlRepository,
    dispatchers: CoroutineDispatchers,
) : CoroutineUseCase<MediaReviewUseCase.Param, MediaReviewResponse>(dispatchers.io) {

    private val query: GqlQueryInterface = MediaReviewUseCaseQuery()

    override suspend fun execute(params: Param): MediaReviewResponse {
        return repo.request(query, params)
    }

    override fun graphqlQuery(): String = query.getQuery()
    data class Param(
        @SerializedName("productID")
        val productId: String,
        @SerializedName("page")
        val page: Int,
        @SerializedName("limit")
        val limit: Int = LIMIT_VALUE,
    ) : GqlParam

    companion object {
        private const val LIMIT_VALUE = 10
        const val QUERY_NAME = "MediaReviewUseCaseQuery"
        const val QUERY = """
            query getMediaReview(${'$'}productID: String!, ${'$'}page: Int!, ${'$'}limit: Int!) {
              productrevGetReviewImage(productID: ${'$'}productID, page: ${'$'}page, limit: ${'$'}limit){
                list {
                  imageID
                  feedbackID
                  imageSiblings
                  imageNumber
                  videoID
                }
                detail {
                  review {
                    shopID
                    user {
                      userID
                      fullName
                      image
                      url
                      label
                    }
                    feedbackID
                    variantName
                    description
                    rating
                    review
                    createTime
                    createTimestamp
                    updateTime
                    updateTimestamp
                    isUpdated
                    isReportable
                    isAnonymous
                    isLiked
                    totalLike
                    userStats {
                      key
                      formatted
                      count
                    }
                  }
                  image {
                    attachmentID
                    thumbnailURL
                    fullsizeURL
                    description
                    feedbackID
                  }
                  video { 
                    attachmentID
                    url
                    feedbackID
                  }
                  imageCountFmt
                  imageCount 
                  mediaCountFmt
                  mediaCount
                  mediaCountTitle
                }
                hasNext
                hasPrev
              }
            }
        """
    }
}
