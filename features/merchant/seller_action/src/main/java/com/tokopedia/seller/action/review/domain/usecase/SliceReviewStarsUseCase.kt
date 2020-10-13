package com.tokopedia.seller.action.review.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.action.review.domain.model.InboxReviewList
import com.tokopedia.seller.action.review.domain.model.SliceReviewResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class SliceReviewStarsUseCase @Inject constructor(private val gqlRepository: GraphqlRepository)
    : UseCase<List<InboxReviewList>>() {

    companion object {
        private const val QUERY = "query getShopReviewByStar(\$filterBy: String!) {\n" +
                "  productrevGetInboxReviewByShop(page: 1, limit: 10, filterBy: \$filterBy) {\n" +
                "    list {\n" +
                "      feedbackID\n" +
                "      reviewText\n" +
                "      rating\n" +
                "      user {\n" +
                "        userName\n" +
                "      }\n" +
                "      product {\n" +
                "        productName\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}"

        private const val FILTER_BY_KEY = "filterBy"
        private const val RATING_BY_KEY = "rating="

        @JvmStatic
        fun createRequestParams(reviewStars: Int) =
                RequestParams.create().apply {
                    putString(FILTER_BY_KEY, "$RATING_BY_KEY$reviewStars")
                }
    }

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): List<InboxReviewList> {
        val request = GraphqlRequest(QUERY, SliceReviewResponse::class.java, params.parameters)
        val response = gqlRepository.getReseponse(listOf(request))

        val errors = response.getError(SliceReviewResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = response.getData<SliceReviewResponse>(SliceReviewResponse::class.java)
            return data.productrevGetInboxReviewByShop.list
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }
}