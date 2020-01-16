package com.tokopedia.product.detail.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductDetailLayout
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailDataModel
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper
import com.tokopedia.product.detail.data.util.TobacoErrorException
import com.tokopedia.product.detail.di.RawQueryKeyConstant.QUERY_GET_PDP_LAYOUT
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import timber.log.Timber
import javax.inject.Inject

open class GetPdpLayoutUseCase @Inject constructor(private val rawQueries: Map<String, String>,
                                                   private val gqlUseCase: MultiRequestGraphqlUseCase) : UseCase<ProductDetailDataModel>() {

    companion object {
        fun createParams(productId: String, shopDomain: String, productKey: String): RequestParams =
                RequestParams.create().apply {
                    putString(ProductDetailCommonConstant.PARAM_PRODUCT_ID, productId)
                    putString(ProductDetailCommonConstant.PARAM_SHOP_DOMAIN, shopDomain)
                    putString(ProductDetailCommonConstant.PARAM_PRODUCT_KEY, productKey)
                }
    }

    var requestParams = RequestParams.EMPTY
    var forceRefresh = false
    val request by lazy {
        GraphqlRequest(rawQueries[QUERY_GET_PDP_LAYOUT], ProductDetailLayout::class.java, requestParams.parameters)
    }

    override suspend fun executeOnBackground(): ProductDetailDataModel {
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(request)
        gqlUseCase.setCacheStrategy(getCacheStrategy())

        val cacheStrategyString = if (forceRefresh) "P1#PDP_CACHE#CACHE_FALSE" else "P1#PDP_CACHE#CACHE_TRUE"
        val gqlResponse = gqlUseCase.executeOnBackground()
        val error: List<GraphqlError>? = gqlResponse.getError(ProductDetailLayout::class.java)
        val data = gqlResponse.getData<ProductDetailLayout>(ProductDetailLayout::class.java)
        val blacklistMessage = data.data.basicInfo.blacklistMessage

        if (error != null && error.isNotEmpty()) {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "), error.firstOrNull()?.extensions?.code.toString())
        } else if (data == null) {
            throw RuntimeException()
        } else if (data.data.basicInfo.isBlacklisted) {
            gqlUseCase.clearCache()
            throw TobacoErrorException(blacklistMessage.description, blacklistMessage.title, blacklistMessage.button, blacklistMessage.url)
        }

        Timber.d(cacheStrategyString)
        return mapIntoModel(data)
    }

    private fun mapIntoModel(data: ProductDetailLayout): ProductDetailDataModel {
        val initialLayoutData = DynamicProductDetailMapper.mapIntoVisitable(data.data.components)
        val getDynamicProductInfoP1 = DynamicProductDetailMapper.mapToDynamicProductDetailP1(data.data)
        return ProductDetailDataModel(getDynamicProductInfoP1, initialLayoutData)
    }

    private fun getCacheStrategy(): GraphqlCacheStrategy {
        return GraphqlCacheStrategy.Builder(if (forceRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST)
                .setExpiryTime(5 * GraphqlConstant.ExpiryTimes.MINUTE_1.`val`())
                .setSessionIncluded(true)
                .build()
    }
}