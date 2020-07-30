package com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop_showcase.shop_showcase_add.data.model.RemoveShowcaseProductBaseResponse
import com.tokopedia.shop_showcase.shop_showcase_add.data.model.RemoveShowcaseProductParam
import com.tokopedia.shop_showcase.shop_showcase_add.data.model.RemoveShowcaseProductResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

/**
 * @author by Rafli Syam
 */
class RemoveShopShowcaseProductUseCase(
        private val gqlRepository: GraphqlRepository
): UseCase<RemoveShowcaseProductResponse>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): RemoveShowcaseProductResponse {
        val request = GraphqlRequest(MUTATION, RemoveShowcaseProductBaseResponse::class.java, params.parameters)
        val response = gqlRepository.getReseponse(listOf(request))
        val responseData = response.getData<RemoveShowcaseProductBaseResponse>(RemoveShowcaseProductBaseResponse::class.java)
        return responseData.removeProductMenuResponse
    }

    companion object {

        /**
         * Request param for remove product from showcase
         */
        private const val INPUT = "input"
        private const val SHOP_ID = "shopID"

        /**
         * Create request params for remove product from showcase
         */
        fun createRequestParams(
                removeShowcaseProductParam: RemoveShowcaseProductParam,
                shopId: String
        ): RequestParams = RequestParams.create().apply {
            putObject(INPUT, removeShowcaseProductParam.listRemoved)
            putString(SHOP_ID, shopId)
        }

        /**
         * GQL Mutation for remove product from showcase
         */
        const val MUTATION = "mutation RemoveProductMenu(\$input: [ProductMenuInput!], \$shopID: String!){\n" +
                "  RemoveProductMenu(input: \$input, shopID: \$shopID) {\n" +
                "    header{\n" +
                "      processTime\n" +
                "      errorCode\n" +
                "      messages\n" +
                "      reason\n" +
                "    }\n" +
                "    isSuccess \n" +
                "  }\n" +
                "}"

    }
}