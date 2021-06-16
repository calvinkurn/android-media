package com.tokopedia.review.feature.reading.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.feature.reading.data.ProductReviewList
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery(GetProductReviewListUseCase.GET_PRODUCT_REVIEW_LIST_USE_CASE_CLASS_NAME, GetProductReviewListUseCase.GET_PRODUCT_REVIEW_LIST_QUERY)
class GetProductReviewListUseCase @Inject constructor(graphqlRepository: GraphqlRepository): GraphqlUseCase<ProductReviewList>(graphqlRepository) {

    companion object {
        const val PARAM_PRODUCT_ID = "productID"
        const val PARAM_PAGE = "page"
        const val PARAM_LIMIT = "limit"
        const val PARAM_SORT = "sortBy"
        const val PARAM_FILTER = "filterBy"
        const val DEFAULT_LIMIT = 10
        const val GET_PRODUCT_REVIEW_LIST_USE_CASE_CLASS_NAME = "ProductReviewListQuery"
        const val GET_PRODUCT_REVIEW_LIST_QUERY = """
            query productrevGetProductReviewList(${'$'}productID: String!, ${'$'}page: Int!, ${'$'}limit: Int!, ${'$'}sortBy: String, ${'$'}filterBy: String) {
              productrevGetProductReviewList(productID: ${'$'}productID, page: ${'$'}page, limit: ${'$'}limit, sortBy: ${'$'}sortBy, filterBy: ${'$'}filterBy) {
                list {
                  feedbackID
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
                  user {
                    userID
                    fullName
                    image
                    url
                  }
                  imageAttachments {
                    imageThumbnailUrl
                    imageUrl
                  }
                  likeDislike {
                    totalLike
                    likeStatus
                  }
                }
                shop {
                  shopID
                  name
                  url
                  image
                }
                hasNext
              }
            }
        """
    }

    init {
        setGraphqlQuery(ProductReviewListQuery.GQL_QUERY)
        setTypeClass(ProductReviewList::class.java)
    }

    private val requestParams = RequestParams.create()

    fun setParams(productId: String, page: Int, sort: String = "", filter: String = "") {
        requestParams.apply {
            putString(PARAM_PRODUCT_ID, productId)
            putInt(PARAM_PAGE, page)
            putInt(PARAM_LIMIT, DEFAULT_LIMIT)
            putString(PARAM_SORT, sort)
            putString(PARAM_FILTER, filter)
        }
        setRequestParams(requestParams.parameters)
    }
}