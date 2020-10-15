package com.tokopedia.product.addedit.detail.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.addedit.detail.data.model.ShopShowcaseListSellerResponse
import com.tokopedia.product.addedit.detail.data.model.ShowcaseItem
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetShopShowCasesUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : UseCase<List<ShowcaseItem>>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): List<ShowcaseItem> {
        val graphqlRequest = GraphqlRequest(QUERY, ShopShowcaseListSellerResponse::class.java, params.parameters)
        val graphqlResponse: GraphqlResponse = graphqlRepository.getReseponse(listOf(graphqlRequest))
        val errors: List<GraphqlError>? = graphqlResponse.getError(ShopShowcaseListSellerResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = graphqlResponse.getData<ShopShowcaseListSellerResponse>(ShopShowcaseListSellerResponse::class.java)
            return data.shopShowcases.result
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        private const val QUERY = "query shopShowcases {\n" +
                "  shopShowcases(withDefault: true) {\n" +
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
                "    }\n" +
                "    error {\n" +
                "      message\n" +
                "    }\n" +
                "  }\n" +
                "}"
    }
}