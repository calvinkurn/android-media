package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductListData
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterMapper
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption
import com.tokopedia.shop.common.data.source.cloud.query.param.option.SortOption
import com.tokopedia.shop.common.data.source.cloud.query.GetProductList
import com.tokopedia.shop.common.data.source.cloud.query.param.option.ExtraInfo
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GQLGetProductListUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
): GraphqlUseCase<ProductListData>(gqlRepository) {

    init {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        setCacheStrategy(cacheStrategy)

        setGraphqlQuery(GetProductList.QUERY)
        setTypeClass(ProductListData::class.java)
    }

    suspend fun execute(requestParams: RequestParams): ProductListData {
        setRequestParams(requestParams.parameters)
        val response = executeOnBackground()
        val header = response.productList?.header
        val errorCode = header?.errorCode

        if(errorCode?.isNotEmpty() == true) {
            throw MessageErrorException()
        }

        return response
    }

    companion object {
        private const val PARAM_SHOP_ID = "shopID"
        private const val PARAM_WAREHOUSE_ID = "warehouseID"
        private const val PARAM_FILTER = "filter"
        private const val PARAM_SORT = "sort"
        private const val PARAM_EXTRA_INFO = "extraInfo"

        /**
         * Create request params for GetProductList GQL
         * Documentation: https://tokopedia.atlassian.net/wiki/spaces/MC/pages/656903551/GQL+ProductList
         *
         * Example:
         * val shopId = "4516"
         * val categoryIds = listOf('1', '2')
         * val filterParams = listOf(FilterByCondition.FeaturedOnly, FilterByCategory(categoryIds))
         * val sortParam = SortOption.SortByPrice(SortOrder.ASC)
         *
         * GQLGetProductListUseCase.createRequestParams(shopId, filterParams, sortParam)
         *
         * @param shopId required, get product list by shopId.
         * @param warehouseId optional, locationId of selected warehouse.
         * @param filterOptions optional, support multiple values.
         * @param sortOption optional, support only single value.
         * @param extraInfoOptions optional, support multiple values.
         *
         * @return GetProductRequestParams required to execute GetProductListUseCase
         */
        fun createRequestParams(
            shopId: String,
            warehouseId: String? = null,
            filterOptions: List<FilterOption>? = null,
            sortOption: SortOption? = null,
            extraInfoOptions: List<ExtraInfo>? = null
        ): RequestParams {
            return RequestParams().apply {
                putString(PARAM_SHOP_ID, shopId)

                warehouseId?.let {
                    putString(PARAM_WAREHOUSE_ID, it)
                }

                filterOptions?.let {
                    val filterParams = FilterMapper.mapToRequestParam(it)
                    putObject(PARAM_FILTER, filterParams)
                }

                sortOption?.let {
                    val sortParam = FilterMapper.mapToRequestParam(it)
                    putObject(PARAM_SORT, sortParam)
                }

                extraInfoOptions?.let { extraInfo ->
                    val extraInfoParams = extraInfo.map { it.value }
                    putObject(PARAM_EXTRA_INFO, extraInfoParams)
                }
            }
        }
    }
}