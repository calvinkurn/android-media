package com.tokopedia.seller.search.common.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.search.common.data.SellerSearchResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetSellerSearchUseCase @Inject constructor(
        private val graphQlRepository: GraphqlRepository
): UseCase<SellerSearchResponse.SellerSearch>() {

    companion object {
        private const val KEYWORD = "keyword"
        private const val LANG = "lang"
        private const val SHOP_ID = "shop_id"
        private const val SECTION = "section"

        private val gqlQuery = """
            query sellerSearch(${'$'}keyword: String!, ${'$'} lang :String!, ${'$'}shop_id: String!) {
              	sellerSearch(keyword: ${'$'}keyword, lang: ${'$'}lang, shop_id: ${'$'}shop_id) {
                data{
                  sections {
                    id
                    has_more
                    action_title
                    action_link
                    title
                    items {
                      id
                      title
                      description
                      label
                      click_event
                      url
                      app_url
                      image_url
                      ref_id
                    }
                  }
                }
              }
            }
        """.trimIndent()

        @JvmStatic
        fun createParams(keyword: String, lang: String, shopId: String): Map<String, Any> =
                mapOf(KEYWORD to keyword, LANG to lang, SHOP_ID to shopId, SHOP_ID to shopId)
    }

    var params = mapOf<String, Any>()

    override suspend fun executeOnBackground(): SellerSearchResponse.SellerSearch {
        val gqlRequest = GraphqlRequest(gqlQuery, SellerSearchResponse::class.java, params)
        val gqlResponse = graphQlRepository.getReseponse(listOf(gqlRequest))
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error.isNullOrEmpty()) {
            return gqlResponse.getData<SellerSearchResponse>(SellerSearchResponse::class.java).sellerSearch
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message} )
        }
    }
}