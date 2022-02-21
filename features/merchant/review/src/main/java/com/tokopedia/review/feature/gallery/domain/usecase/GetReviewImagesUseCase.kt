package com.tokopedia.review.feature.gallery.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.feature.gallery.data.ProductrevGetReviewImageResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery(GetReviewImagesUseCase.GET_PRODUCT_REVIEW_IMAGE_USE_CASE_CLASS_NAME, GetReviewImagesUseCase.GET_PRODUCT_REVIEW_IMAGE_QUERY)
class GetReviewImagesUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<ProductrevGetReviewImageResponse>(graphqlRepository) {

    companion object {
        const val PARAM_PRODUCT_ID = "productID"
        const val PARAM_PAGE = "page"
        const val PARAM_LIMIT = "limit"
        const val DEFAULT_LIMIT = 10
        const val GET_PRODUCT_REVIEW_IMAGE_USE_CASE_CLASS_NAME = "ProductReviewImageQuery"
        const val GET_PRODUCT_REVIEW_IMAGE_QUERY = """
            query getReviewImage(${'$'}productID: String!, ${'$'}page: Int!, ${'$'}limit: Int!) {
              productrevGetReviewImage(productID: ${'$'}productID, page: ${'$'}page, limit: ${'$'}limit) {
                list {
                  imageID
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
        setGraphqlQuery(ProductReviewImageQuery.GQL_QUERY)
        setTypeClass(ProductrevGetReviewImageResponse::class.java)
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