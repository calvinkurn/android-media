package com.tokopedia.review.feature.reviewlist.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.review.feature.reviewdetail.data.ProductReviewDetailOverallResponse
import com.tokopedia.review.feature.reviewdetail.data.ProductReviewInitialDataResponse
import com.tokopedia.review.feature.reviewlist.data.ProductRatingOverallResponse
import com.tokopedia.review.feature.reviewlist.data.ProductReviewListResponse
import com.tokopedia.review.feature.reviewlist.view.model.ProductRatingWrapperUiModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetProductRatingOverallUseCase @Inject constructor(
        private val graphQlRepository: GraphqlRepository
) : UseCase<ProductRatingWrapperUiModel>() {

    companion object {
        private const val SORT_BY = "sortBy"
        private const val FILTER_BY = "filterBy"
        private const val LIMIT = "limit"
        private const val PAGE = "page"
        const val PRODUCT_RATING_QUERY_CLASS_NAME = "RatingProduct"
        const val PRODUCT_RATING_QUERY = """
            query get_product_rating_overall(${'$'}filterBy: String!) {
              productrevGetProductRatingOverallByShop(filterBy: ${'$'}filterBy) {
                rating
                productCount
                reviewCount
                filterBy
              }
            }
        """

        @JvmStatic
        fun createParams(filterBy: String, sortBy: String, limit: Int, page: Int): RequestParams = RequestParams.create().apply {
            putString(FILTER_BY, filterBy)
            putString(SORT_BY, sortBy)
            putInt(LIMIT, limit)
            putInt(PAGE, page)
        }
    }

    var requestParams: RequestParams = RequestParams.EMPTY

    @GqlQuery(PRODUCT_RATING_QUERY_CLASS_NAME, PRODUCT_RATING_QUERY)
    override suspend fun executeOnBackground(): ProductRatingWrapperUiModel {
        val productRatingWrapperUiModel = ProductRatingWrapperUiModel()

        val filterBy = requestParams.getString(FILTER_BY, "")
        val sortBy = requestParams.getString(SORT_BY, "")
        val limit = requestParams.getInt(LIMIT, 0)
        val page = requestParams.getInt(PAGE, 0)

        val ratingProductParam = mapOf(FILTER_BY to filterBy)
        val productListParam = mapOf(SORT_BY to sortBy, FILTER_BY to filterBy, LIMIT to limit, PAGE to page)

        val ratingProductRequest = GraphqlRequest(RatingProduct.GQL_QUERY, ProductRatingOverallResponse::class.java, ratingProductParam)
        val productListRequest = GraphqlRequest(ProductReviewList.GQL_QUERY, ProductReviewListResponse::class.java, productListParam)

        val requests = mutableListOf(ratingProductRequest, productListRequest)

        try {
            val gqlResponse = graphQlRepository.getReseponse(requests)

            if (gqlResponse.getError(ProductRatingOverallResponse::class.java)?.isNullOrEmpty() != true) {
                productRatingWrapperUiModel.productRatingOverall =
                        Success(gqlResponse.getData<ProductRatingOverallResponse>(ProductRatingOverallResponse::class.java).getProductRatingOverallByShop)
            } else {
                val error = gqlResponse.getError(ProductRatingOverallResponse::class.java).joinToString { it.message }
                productRatingWrapperUiModel.productRatingOverall = Fail(Throwable(message = error))
            }

            if (gqlResponse.getError(ProductReviewListResponse::class.java)?.isNullOrEmpty() != true) {
                productRatingWrapperUiModel.reviewProductList =
                        Success(gqlResponse.getData<ProductReviewListResponse>(ProductReviewListResponse::class.java).productShopRatingAggregate)
            } else {
                val error = gqlResponse.getError(ProductReviewListResponse::class.java).joinToString { it.message }
                productRatingWrapperUiModel.reviewProductList = Fail(Throwable(message = error))
            }
        } catch (e: Throwable) {

        }

        return productRatingWrapperUiModel
    }
}