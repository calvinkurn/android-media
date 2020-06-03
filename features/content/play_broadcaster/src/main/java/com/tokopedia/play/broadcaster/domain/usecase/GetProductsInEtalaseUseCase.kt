package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.broadcaster.domain.model.GetProductsByEtalaseResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by jegul on 02/06/20
 */
class GetProductsInEtalaseUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : UseCase<GetProductsByEtalaseResponse.GetShopProductData>() {

    private val query = """
            query getShopProduct(${'$'}shopId: String!, ${'$'}filter: ProductListFilter!){
                GetShopProduct(shopID: ${'$'}shopId, filter: ${'$'}filter){
                    status
                    errors
                    data {
                        product_id
                        name
                        product_url
                        stock
                        primary_image{
                            original
                            thumbnail
                            resize300
                        }
                    }
                    totalData
                }
            }
        """

    var params: Map<String, Any> = emptyMap()

    override suspend fun executeOnBackground(): GetProductsByEtalaseResponse.GetShopProductData {
        val gqlRequest = GraphqlRequest(query, GetProductsByEtalaseResponse::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val response = gqlResponse.getData<GetProductsByEtalaseResponse>(GetProductsByEtalaseResponse::class.java)
        val errors = response?.getShopProduct?.errors
        if (response != null && errors.isNullOrEmpty()) {
            return response.getShopProduct
        } else {
            throw MessageErrorException(errors.orEmpty())
        }
    }

    companion object {

        private const val PARAMS_SHOP_ID = "shopId"
        private const val PARAMS_FILTER = "filter"
        private const val PARAMS_PAGE = "page"
        private const val PARAMS_PER_PAGE = "perPage"
        private const val PARAMS_ETALASE_ID = "fmenu"
        private const val PARAMS_KEYWORD = "fkeyword"
        private const val PARAMS_SORT = "sort"

        fun createParams(
                shopId: String,
                page: Int,
                perPage: Int,
                etalaseId: String = "",
                keyword: String = ""
        ): Map<String, Any> = mapOf(
                PARAMS_SHOP_ID to shopId,
                PARAMS_FILTER to mapOf(
                        PARAMS_PAGE to page,
                        PARAMS_PER_PAGE to perPage,
                        PARAMS_ETALASE_ID to etalaseId,
                        PARAMS_KEYWORD to keyword,
                        PARAMS_SORT to 0
                )
        )
    }
}