package com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop_showcase.shop_showcase_add.data.model.UpdateShopShowcaseBaseResponse
import com.tokopedia.shop_showcase.shop_showcase_add.data.model.UpdateShopShowcaseParam
import com.tokopedia.shop_showcase.shop_showcase_add.data.model.UpdateShopShowcaseResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

class UpdateShopShowcaseUseCase(
        private val gqlRepository: GraphqlRepository
): UseCase<UpdateShopShowcaseResponse>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): UpdateShopShowcaseResponse {
        val request = GraphqlRequest(MUTATION, UpdateShopShowcaseBaseResponse::class.java, params.parameters)
        val response = gqlRepository.getReseponse(listOf(request))
        val responseData = response.getData<UpdateShopShowcaseBaseResponse>(UpdateShopShowcaseBaseResponse::class.java)
        return responseData.updateShopShowcaseResponse
    }

    companion object {

        /**
         * Request param for update showcase name
         */
        private const val INPUT = "input"

        /**
         * Create request params for update shop showcase name
         */
        fun createRequestParams(
                updateShopShowcaseParam: UpdateShopShowcaseParam
        ): RequestParams = RequestParams.create().apply {
            putObject(INPUT, updateShopShowcaseParam)
        }

        /**
         * GQL Mutation to update shop showcase name
         */
        const val MUTATION = "mutation updateShopShowcase(\$input: ParamUpdateShopShowcase!) {\n" +
                "  updateShopShowcase(input: \$input) {\n" +
                "    success\n" +
                "    message\n" +
                "  }\n" +
                "}"

    }
}