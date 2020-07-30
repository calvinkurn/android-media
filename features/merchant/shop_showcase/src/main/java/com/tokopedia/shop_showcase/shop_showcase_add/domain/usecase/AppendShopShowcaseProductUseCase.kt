package com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop_showcase.shop_showcase_add.data.model.AppendShowcaseProductBaseResponse
import com.tokopedia.shop_showcase.shop_showcase_add.data.model.AppendShowcaseProductResponse
import com.tokopedia.shop_showcase.shop_showcase_add.data.model.AppendShowcaseProductParam
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

class AppendShopShowcaseProductUseCase(
        private val gqlRepository: GraphqlRepository
): UseCase<AppendShowcaseProductResponse>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): AppendShowcaseProductResponse {
        val request = GraphqlRequest(MUTATION, AppendShowcaseProductBaseResponse::class.java, params.parameters)
        val response = gqlRepository.getReseponse(listOf(request))
        val responseData = response.getData<AppendShowcaseProductBaseResponse>(AppendShowcaseProductBaseResponse::class.java)
        return responseData.appendProductMenuResponse
    }

    companion object {

        /**
         * Request param for append new showcase product
         */
        private const val INPUT = "input"
        private const val SOURCE = "source"
        private const val SHOP_ID = "shopID"
        private const val DEFAULT_SOURCE = "android"

        /**
         * Create request params for append new showcase product
         */
        fun createRequestParams(
                appendShowcaseProductParam: AppendShowcaseProductParam,
                shopId: String
        ): RequestParams = RequestParams.create().apply {
            putObject(INPUT, appendShowcaseProductParam.listAppended)
            putString(SOURCE, DEFAULT_SOURCE)
            putString(SHOP_ID, shopId)
        }

        /**
         * GQL Mutation to append new showcase product
         */
        const val MUTATION = "mutation AppendProductMenu(\n" +
                "    \$input: [ProductMenuInput!],\n" +
                "    \$source: String!,\n" +
                "    \$shopID: String!\n" +
                "  ){\n" +
                "    AppendProductMenu(input: \$input, source: \$source, shopID: \$shopID) {\n" +
                "     \theader{\n" +
                "      \tprocessTime\n" +
                "      \terrorCode\n" +
                "      \tmessages\n" +
                "      \treason\n" +
                "    \t}\n" +
                "    \tisSuccess \n" +
                "    }\n" +
                "}"

    }
}