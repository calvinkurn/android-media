package com.tokopedia.product.detail.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductDetailLayout
import com.tokopedia.product.detail.di.RawQueryKeyConstant.QUERY_GET_PDP_LAYOUT
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetPdpLayoutUseCase @Inject constructor(private val rawQueries: Map<String, String>,
                                              private val gqlUseCase: MultiRequestGraphqlUseCase) : UseCase<ProductDetailLayout>() {

    companion object {
        fun createParams(productId: String): RequestParams =
                RequestParams.create().apply {
                    putString(ProductDetailCommonConstant.PARAM_PRODUCT_ID, productId)
                }
    }

    var requestParams = RequestParams.EMPTY
    var isFromCacheFirst = false
    val request by lazy {
        GraphqlRequest(rawQueries[QUERY_GET_PDP_LAYOUT], ProductDetailLayout::class.java, requestParams.parameters)
    }

    override suspend fun executeOnBackground(): ProductDetailLayout {
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(request)
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(if (isFromCacheFirst) CacheType.CACHE_FIRST else CacheType.ALWAYS_CLOUD).build())

        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(ProductDetailLayout::class.java) ?: listOf()
        val data = gqlResponse.getData<ProductDetailLayout>(ProductDetailLayout::class.java)

        if (data == null) {
            throw RuntimeException()
        } else if (error.isNotEmpty() || error.first().message.isNotEmpty()) {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }

        return data
    }
}