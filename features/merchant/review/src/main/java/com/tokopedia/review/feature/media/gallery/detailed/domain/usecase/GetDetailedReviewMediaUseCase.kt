package com.tokopedia.review.feature.media.gallery.detailed.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductRevGetDetailedReviewMediaResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetDetailedReviewMediaUseCase @Inject constructor(
    @ApplicationContext
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ProductRevGetDetailedReviewMediaResponse>(graphqlRepository) {

    companion object {
        const val PARAM_PRODUCT_ID = "productID"
        const val PARAM_PAGE = "page"
        const val PARAM_LIMIT = "limit"
        const val DEFAULT_LIMIT = 10
        const val GET_PRODUCT_REVIEW_IMAGE_QUERY = """
            query getReviewImage(${'$'}productID: String!, ${'$'}page: Int!, ${'$'}limit: Int!) {
              productrevGetReviewImage(productID: ${'$'}productID, page: ${'$'}page, limit: ${'$'}limit) {
                list {
                  imageID
                  videoID
                  feedbackID
                  imageSiblings
                  imageNumber
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
                    badRatingReasonFmt
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
                }
                hasNext
                hasPrev
              }
            }
        """
    }

    init {
        setGraphqlQuery(GET_PRODUCT_REVIEW_IMAGE_QUERY)
        setTypeClass(ProductRevGetDetailedReviewMediaResponse::class.java)
    }

    private val requestParams = RequestParams.create()

    fun setParams(productId: String, page: Int) {
        requestParams.apply {
            putString(PARAM_PRODUCT_ID, productId)
            putInt(PARAM_PAGE, page)
            putInt(PARAM_LIMIT, DEFAULT_LIMIT)
        }
        setRequestParams(requestParams.parameters)
    }
}