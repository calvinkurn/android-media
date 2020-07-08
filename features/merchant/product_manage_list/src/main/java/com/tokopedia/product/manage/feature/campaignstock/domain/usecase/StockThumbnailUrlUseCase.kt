package com.tokopedia.product.manage.feature.campaignstock.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.constant.ErrorNetMessage
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseDataNullException
import com.tokopedia.product.manage.feature.campaignstock.domain.model.StockThumbnailResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class StockThumbnailUrlUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository): GraphqlUseCase<String>(gqlRepository) {

    companion object {
        private const val QUERY = "query getProductThumbnail (\$productId: String!) {\n" +
                "  GetProduct(productID: \$productId) {\n" +
                "    header {\n" +
                "      processTime\n" +
                "      messages\n" +
                "      reason\n" +
                "    }\n" +
                "    data {\n" +
                "      picture {\n" +
                "        thumbnailURL\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}"

        private const val PRODUCT_ID_KEY = "productId"

        @JvmStatic
        fun createRequestParams(productId: String): RequestParams =
                RequestParams.create().apply {
                    putString(PRODUCT_ID_KEY, productId)
                }
    }

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): String {
        val gqlRequest = GraphqlRequest(QUERY, StockThumbnailResponse::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))
        val errors = gqlResponse.getError(StockThumbnailResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<StockThumbnailResponse>(StockThumbnailResponse::class.java)
            data.getProduct.data.picture.firstOrNull()?.thumbnailUrl?.let { url ->
                return url
            }
            throw ResponseDataNullException(ErrorNetMessage.MESSAGE_ERROR_NULL_DATA)
        } else {
            throw MessageErrorException(errors.joinToString { it.message })
        }
    }
}