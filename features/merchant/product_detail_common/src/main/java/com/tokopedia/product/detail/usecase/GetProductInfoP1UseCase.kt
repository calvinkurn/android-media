package com.tokopedia.product.detail.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.constant.RawQueryKeyConstant.QUERY_PRODUCT_INFO
import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.product.detail.common.data.model.product.ProductInfoP1
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetProductInfoP1UseCase @Inject constructor(private val rawQueries: Map<String, String>,
                                                  private val gqlUseCase: MultiRequestGraphqlUseCase) : UseCase<ProductInfo.Response>() {

    var params: RequestParams = RequestParams.EMPTY
    var isFromCacheFirst: Boolean = false
    val request by lazy {
        GraphqlRequest(rawQueries[QUERY_PRODUCT_INFO], ProductInfo.Response::class.java, params.parameters)
    }

    override suspend fun executeOnBackground(): ProductInfo.Response {
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(request)
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(if (isFromCacheFirst) CacheType.CACHE_FIRST else CacheType.ALWAYS_CLOUD).build())

        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(ProductInfoP1::class.java) ?: listOf()
        val data = gqlResponse.getData<ProductInfo.Response>(ProductInfo.Response::class.java)

        if (data == null) {
            throw RuntimeException()
        } else if (error.isNotEmpty()) {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }

        return data
    }

    companion object {
        fun createParams(productId: Int, shopDomain: String, productName: String): RequestParams =
                RequestParams.create().apply {
                    putInt(ProductDetailCommonConstant.PARAM_PRODUCT_ID, productId)
                    putString(ProductDetailCommonConstant.PARAM_SHOP_DOMAIN, shopDomain)
                    putString(ProductDetailCommonConstant.PARAM_PRODUCT_KEY, productName)
                }
    }

}