package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.broadcaster.domain.model.GetProductsByEtalaseResponse
import com.tokopedia.play.broadcaster.util.handler.DefaultUseCaseHandler
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by jegul on 02/06/20
 */
class GetProductsInEtalaseUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : UseCase<GetProductsByEtalaseResponse.GetProductListData>() {

    private val query = """
            query GetShopProductList(${'$'}shopId: String!, ${'$'}filter: [GoodsFilterInput], ${'$'}sort: GoodsSortInput) {
                ProductList(shopID: ${'$'}shopId, filter: ${'$'}filter, sort: ${'$'}sort) {
                    header {
                        messages
                        reason
                        errorCode
                    }
                    data {
                        id
                        name
                        stock
                        pictures {
                            urlThumbnail
                        }
                    }
                    meta {
                        totalHits
                    }
                }  
            }
        """

    var params: Map<String, Any> = emptyMap()

    override suspend fun executeOnBackground(): GetProductsByEtalaseResponse.GetProductListData {
        val gqlResponse = DefaultUseCaseHandler(
                gqlRepository = graphqlRepository,
                query = query,
                typeOfT = GetProductsByEtalaseResponse::class.java,
                params = params,
                gqlCacheStrategy = GraphqlCacheStrategy
                        .Builder(CacheType.ALWAYS_CLOUD).build()
        ).executeWithRetry()
        val response = gqlResponse.getData<GetProductsByEtalaseResponse>(GetProductsByEtalaseResponse::class.java)
        val errors = response?.productList?.header?.messages.orEmpty()
        if (response != null && errors.isNullOrEmpty()) {
            return response.productList
        } else {
            throw MessageErrorException(errors.joinToString(","))
        }
    }

    companion object {

        private const val PARAMS_SHOP_ID = "shopId"
        private const val PARAMS_FILTER = "filter"
        private const val PARAMS_SORT = "sort"
        private const val PARAMS_ID = "id"
        private const val PARAMS_VALUE = "value"

        private const val PARAMS_INPUT_KEYWORD = "keyword"
        private const val PARAMS_INPUT_PAGE = "page"
        private const val PARAMS_INPUT_PER_PAGE = "perPage"
        private const val PARAMS_INPUT_ETALASE_ID = "menu"
        private const val PARAMS_INPUT_STATUS = "status"

        private const val PARAMS_INPUT_UPDATE_TIME = "UPDATE_TIME"

        fun createParams(
                shopId: String,
                page: Int,
                perPage: Int,
                etalaseId: String = "",
                keyword: String = ""
        ): Map<String, Any> = mapOf(
                PARAMS_SHOP_ID to shopId,
                PARAMS_FILTER to mutableListOf(
                        getFilterInput(PARAMS_INPUT_KEYWORD, keyword),
                        getFilterInput(PARAMS_INPUT_PAGE, page.toString()),
                        getFilterInput(PARAMS_INPUT_PER_PAGE, perPage.toString()),
                        getFilterInput(PARAMS_INPUT_STATUS, "ACTIVE")
                ).apply {
                    if (etalaseId.isNotEmpty())
                        add(getFilterInput(PARAMS_INPUT_ETALASE_ID, etalaseId))
                },
                PARAMS_SORT to mapOf(PARAMS_INPUT_UPDATE_TIME to "DESC")
        )

        private fun getFilterInput(
                id: String,
                vararg value: String
        ) = mapOf(
                PARAMS_ID to id,
                PARAMS_VALUE to value
        )
    }
}