package com.tokopedia.shop.common.graphql.domain.usecase.shopetalase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopShowcaseListSellerResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetShopEtalaseUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : UseCase<ShopShowcaseListSellerResponse>() {

    companion object {
        private const val WITH_DEFAULT = "withDefault"

        fun createRequestParams(
                withDefault: Boolean = false
        ): RequestParams = RequestParams.create().apply {
            putBoolean(WITH_DEFAULT, withDefault)
        }

        private const val QUERY = "query shopShowcases(\$withDefault: Boolean) {\n" +
                "  shopShowcases(withDefault:\$withDefault) {\n" +
                "    result {\n" +
                "      id\n" +
                "      name\n" +
                "      count\n" +
                "      type\n" +
                "      highlighted\n" +
                "      alias\n" +
                "      uri\n" +
                "      useAce\n" +
                "      badge\n" +
                "      aceDefaultSort\n" +
                "      imageURL\n" +
                "    }\n" +
                "    error {\n" +
                "      message\n" +
                "    }\n" +
                "  }\n" +
                "}"
    }

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ShopShowcaseListSellerResponse {
        val graphqlRequest = GraphqlRequest(QUERY, ShopShowcaseListSellerResponse::class.java, params.parameters)
        val graphqlResponse: GraphqlResponse = graphqlRepository.getReseponse(listOf(graphqlRequest))
        val errors: List<GraphqlError>? = graphqlResponse.getError(ShopShowcaseListSellerResponse::class.java)
        if (errors.isNullOrEmpty()) {
            return graphqlResponse.getData(ShopShowcaseListSellerResponse::class.java)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }
}