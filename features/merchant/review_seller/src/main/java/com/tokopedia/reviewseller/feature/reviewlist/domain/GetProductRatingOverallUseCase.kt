package com.tokopedia.reviewseller.feature.reviewlist.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.reviewseller.feature.reviewlist.data.ProductRatingOverallResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetProductRatingOverallUseCase @Inject constructor(
        private val graphQlRepository: GraphqlRepository
) : UseCase<ProductRatingOverallResponse.ProductGetProductRatingOverallByShop>() {

    companion object {
        private const val FILTER_BY = "filterBy"
        private val gqlQuery = """
            query get_product_rating_overall(${'$'}filterBy: String!) {
              productrevGetProductRatingOverallByShop(filterBy: ${'$'}filterBy) {
                rating
                productCount
                reviewCount
                filterBy
              }
            }
        """.trimIndent()

        @JvmStatic
        fun createParams(filterBy: String): Map<String, String> = mapOf(FILTER_BY to filterBy)
    }

    var params = mapOf<String, Any>()

    override suspend fun executeOnBackground(): ProductRatingOverallResponse.ProductGetProductRatingOverallByShop {
        val gqlRequest = GraphqlRequest(gqlQuery, ProductRatingOverallResponse::class.java, params)
        val gqlResponse = graphQlRepository.getReseponse(listOf(gqlRequest))
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error.isNullOrEmpty()) {
            return gqlResponse.getData<ProductRatingOverallResponse>(ProductRatingOverallResponse::class.java).getProductRatingOverallByShop
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message} )
        }
    }

}