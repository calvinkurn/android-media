package com.tokopedia.product.addedit.detail.domain.interactor

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.product.addedit.detail.domain.UniverseSearchResponse
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.UNIVERSE_SEARCH_QUERY
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetSearchShopProductUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        @Named(UNIVERSE_SEARCH_QUERY)
        private val gqlQuery: String
): UseCase<UniverseSearchResponse>() {

    var requestParams = mapOf<String, Any>()

    override suspend fun executeOnBackground(): UniverseSearchResponse {

        val gqlRequest = GraphqlRequest(gqlQuery, UniverseSearchResponse::class.java, requestParams)
        val gqlResponse: GraphqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest))

        val errors: List<GraphqlError>? = gqlResponse.getError(UniverseSearchResponse::class.java)
        if (errors.isNullOrEmpty()) {
            return gqlResponse.getData(UniverseSearchResponse::class.java)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        private const val KEY_SHOP_ID = "shopID"
        private const val KEY_SEARCH_QUERY = "q"

        private const val QUERY = "query universe_search(\$shopID: Int, \$q: String) {\n" +
                "    universe_search(q: \$q, shopID: \$shopID){\n" +
                "        data{\n" +
                "            id\n" +
                "            name\n" +
                "            items {\n" +
                "                location\n" +
                "                imageURI\n" +
                "                applink\n" +
                "                url\n" +
                "                keyword\n" +
                "                recom\n" +
                "                sc\n" +
                "                isOfficial\n" +
                "                post_count\n" +
                "                affiliate_username\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}"

        @JvmStatic
        fun createRequestParam(
                shopID: Int,
                searchQuery: String
        ) = HashMap<String, Any>().apply {
            put(KEY_SHOP_ID, shopID)
            put(KEY_SEARCH_QUERY, searchQuery)
        }
    }
}