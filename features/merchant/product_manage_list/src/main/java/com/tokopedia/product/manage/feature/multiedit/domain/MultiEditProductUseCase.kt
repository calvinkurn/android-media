package com.tokopedia.product.manage.feature.multiedit.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.product.manage.feature.multiedit.data.param.ProductParam
import com.tokopedia.product.manage.feature.multiedit.data.query.BulkProductEditV3
import com.tokopedia.product.manage.feature.multiedit.data.response.MultiEditProduct
import com.tokopedia.usecase.RequestParams

class MultiEditProductUseCase(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<MultiEditProduct>(graphqlRepository) {

    companion object {
        private const val PRODUCT_ID_PARAM = "productID"
        private const val SHOP_PARAM = "shop"
        private const val STOCK_PARAM = "stock"
        private const val PRICE_PARAM = "price"
        private const val MENU_PARAM = "menu"
        private const val STATUS_PARAM = "status"
        private const val INPUT_PARAM = "input"

        fun createRequestParam(productParams: List<ProductParam>): RequestParams {
            val params = productParams.map { product ->
                RequestParams().apply {
                    putString(PRODUCT_ID_PARAM, product.productId)
                    putObject(SHOP_PARAM, product.shop)

                    product.stock?.let { stock ->
                        putInt(STOCK_PARAM, stock)
                    }

                    product.price?.let { price ->
                        putInt(PRICE_PARAM, price)
                    }

                    product.menu?.let { menu ->
                        putObject(MENU_PARAM, menu)
                    }

                    product.status?.let { status ->
                        putString(STATUS_PARAM, status.name)
                    }
                }.parameters
            }
            return RequestParams().apply { putObject(INPUT_PARAM, params) }
        }
    }

    init {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        setCacheStrategy(cacheStrategy)

        setGraphqlQuery(BulkProductEditV3.QUERY)
        setTypeClass(MultiEditProduct::class.java)
    }

    suspend fun execute(requestParams: RequestParams): MultiEditProduct {
        setRequestParams(requestParams.parameters)
        return executeOnBackground()
    }
}