package com.tokopedia.shop_showcase.shop_showcase_product_add.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop_showcase.shop_showcase_product_add.data.model.GetProductListData
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.mapper.ProductMapper
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.model.GetProductListFilter
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.ShowcaseProduct
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

/**
 * @author by Rafli Syam on 2020-03-09
 */

class GetProductListUseCase(
        private val gqlRepository: GraphqlRepository,
        private val productMapper: ProductMapper
) : UseCase<List<ShowcaseProduct>>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): List<ShowcaseProduct> {
        val request = GraphqlRequest(QUERY, GetProductListData::class.java, params.parameters)
        val response = gqlRepository.getReseponse(listOf(request))
        val responseData = response.getData<GetProductListData>(GetProductListData::class.java)
        return productMapper.mapToUIModel(responseData.getProductList.data)
    }

    companion object {

        /**
         * Request Param Keys
         */
        private const val SHOP_ID = "shopID"
        private const val FILTER = "filter"

        /**
         * Create request parameters for get product list gql call
         */
        fun createRequestParams(
                shopId: String,
                filter: GetProductListFilter
        ): RequestParams = RequestParams.create().apply {
            putString(SHOP_ID, shopId)
            putObject(FILTER, filter)
        }

        /**
         * GQL Query for get product list by shop ID
         */
        const val QUERY = "query GetProductList(\$shopID: String!, \$filter: ProductListFilter!) {\n" +
                "  GetProductList(shopID: \$shopID, filter: \$filter){\n" +
                "    status\n" +
                "    errors\n" +
                "    totalData\n" +
                "    links{\n" +
                "      self\n" +
                "      next\n" +
                "      prev\n" +
                "    }\n" +
                "    data{\n" +
                "      product_id\n" +
                "      condition\n" +
                "      name\n" +
                "      product_url\n" +
                "      status\n" +
                "      stock\n" +
                "      minimum_order\n" +
                "      stats{\n" +
                "        reviewCount\n" +
                "        rating\n" +
                "      }\n" +
                "      price{\n" +
                "        currency_id\n" +
                "        currency_text\n" +
                "        value\n" +
                "        value_idr\n" +
                "        text\n" +
                "        text_idr\n" +
                "        identifier\n" +
                "      }\n" +
                "    flags{\n" +
                "        isPreorder\n" +
                "        isWholesale\n" +
                "        isWishlist\n" +
                "        isSold\n" +
                "      }\n" +
                "      primary_image{\n" +
                "        original\n" +
                "        thumbnail\n" +
                "      }\n" +
                "    }\n" +
                "\t}\n" +
                "}"

    }



}