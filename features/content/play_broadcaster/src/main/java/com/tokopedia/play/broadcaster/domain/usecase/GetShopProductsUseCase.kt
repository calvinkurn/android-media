package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.config.GlobalConfig
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.domain.model.product.GetShopProductsResponse
import com.tokopedia.play_common.domain.usecase.RetryableGraphqlUseCase
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 28/01/22
 */
@GqlQuery(GetShopProductsUseCase.QUERY_NAME, GetShopProductsUseCase.QUERY)
class GetShopProductsUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository
) : RetryableGraphqlUseCase<GetShopProductsResponse>(graphqlRepository) {

    init {
        setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build()
        )
        setTypeClass(GetShopProductsResponse::class.java)
        setGraphqlQuery(GetShopProductsUseCaseQuery())
    }

    companion object {

        private const val PARAM_SHOP_ID = "shopID"
        private const val PARAM_PAGE = "page"
        private const val PARAM_PER_PAGE = "perPage"
        private const val PARAM_KEYWORD = "keyword"
        private const val PARAM_ETALASE_ID = "etalaseId"
        private const val PARAM_IS_QA = "isQA"
        private const val PARAM_SORT = "sort"

        const val QUERY_NAME = "GetShopProductsUseCaseQuery"
        const val QUERY = """
            query ShopProducts(
                ${"$$PARAM_SHOP_ID"}: String!,
                ${"$$PARAM_PAGE"}: Int,
                ${"$$PARAM_PER_PAGE"}: Int,
                ${"$$PARAM_KEYWORD"}: String,
                ${"$$PARAM_ETALASE_ID"}: String,
                ${"$$PARAM_IS_QA"}: Boolean,
                ${"$$PARAM_SORT"}: Int
            )  {
                GetShopProduct(
                    shopID: ${"$$PARAM_SHOP_ID"},
                    filter: {
                        page: ${"$$PARAM_PAGE"},
                        perPage: ${"$$PARAM_PER_PAGE"},
                        fkeyword: ${"$$PARAM_KEYWORD"},
                        fmenu: ${"$$PARAM_ETALASE_ID"},
                        sort: ${"$$PARAM_SORT"}
                    },
                    isQA: ${"$$PARAM_IS_QA"}
                ) {
                    links {
                        next
                    }
                    data {
                        product_id
                        name
                        stock
                        price {
                            text_idr
                        }
                        campaign {
                            discounted_percentage
                            original_price_fmt
                        }
                        primary_image {
                            thumbnail
                        }
                    }
                }
            }
        """

        fun createParams(
            shopId: String,
            etalaseId: String = "0",
            page: Int = 1,
            perPage: Int = 25,
            keyword: String = "",
            sort: Int = 0,
            isQA: Boolean = GlobalConfig.DEBUG
        ): Map<String, Any> {
            return mapOf(
                PARAM_SHOP_ID to shopId,
                PARAM_ETALASE_ID to etalaseId,
                PARAM_PAGE to page,
                PARAM_PER_PAGE to perPage,
                PARAM_KEYWORD to keyword,
                PARAM_SORT to sort,
                PARAM_IS_QA to isQA
            )
        }
    }
}
