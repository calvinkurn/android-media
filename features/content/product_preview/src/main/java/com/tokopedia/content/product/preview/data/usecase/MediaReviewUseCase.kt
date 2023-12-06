package com.tokopedia.content.product.preview.data.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * @author by astidhiyaa on 05/12/23
 */
class MediaReviewUseCase @Inject constructor(
    @ApplicationContext private val repo: GraphqlRepository,
    dispatchers: CoroutineDispatchers,
) : CoroutineUseCase<Map<String, Any>, MediaReviewResponse>(dispatchers.io) {

    override suspend fun execute(params: Map<String, Any>): MediaReviewResponse {
        return repo.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = QUERY
    fun convertToMap(param: Param) = mapOf(
        PRODUCT_ID_PARAM to param.productId,
        PAGE_PARAM to param.page,
        LIMIT_PARAM to LIMIT_VALUE,
    )

    data class Param(
        val productId: String,
        val page: Int,
    )

    companion object {
        private const val PRODUCT_ID_PARAM = "productID"
        private const val PAGE_PARAM = "page"
        private const val LIMIT_PARAM = "limit"
        private const val LIMIT_VALUE = 10
        private const val QUERY = """
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
                  video { // array
                    attachmentID
                    url
                    feedbackID
                  }
                  imageCountFmt // will be deprecated
                  imageCount // will be deprecated
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
