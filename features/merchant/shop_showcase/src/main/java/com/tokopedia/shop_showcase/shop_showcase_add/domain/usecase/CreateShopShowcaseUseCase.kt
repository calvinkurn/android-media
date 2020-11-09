package com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop_showcase.shop_showcase_add.data.model.AddShopShowcaseParam
import com.tokopedia.shop_showcase.shop_showcase_add.data.model.AddShopShowcaseBaseReponse
import com.tokopedia.shop_showcase.shop_showcase_add.data.model.AddShopShowcaseResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

class CreateShopShowcaseUseCase(
        private val gqlRepository: GraphqlRepository
): UseCase<AddShopShowcaseResponse>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): AddShopShowcaseResponse {
        val request = GraphqlRequest(MUTATION, AddShopShowcaseBaseReponse::class.java, params.parameters)
        val response = gqlRepository.getReseponse(listOf(request))
        val responseData = response.getData<AddShopShowcaseBaseReponse>(AddShopShowcaseBaseReponse::class.java)
        return responseData.addShopShowcaseReponse
    }

    companion object {

        /**
         * Request param keys
         */
        private const val INPUT = "input"

        /**
         * Create request params for create new shop showcase gql call
         */
        fun createRequestParams(
                addShopShowcase: AddShopShowcaseParam
        ): RequestParams = RequestParams.create().apply {
            putObject(INPUT, addShopShowcase)
        }

        /**
         * GQL Mutation to create new shop showcase
         */
        const val MUTATION: String = "mutation addShopShowcase(\$input: ParamAddShopShowcase!) {\n" +
                "  addShopShowcase(input: \$input) {\n" +
                "  \tsuccess\n" +
                "    message\n" +
                "    createdId\n" +
                "  }\n" +
                "}"
    }
}