package com.tokopedia.product.detail.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductDetailLayout
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductLastSeenDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductOpenShopDataModel
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper
import com.tokopedia.product.detail.di.RawQueryKeyConstant.QUERY_GET_PDP_LAYOUT
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetPdpLayoutUseCase @Inject constructor(private val rawQueries: Map<String, String>,
                                              private val gqlUseCase: MultiRequestGraphqlUseCase) : UseCase<ProductDetailDataModel>() {

    companion object {
        fun createParams(productId: String, isUserActive: Boolean, isUserHasShop: Boolean): RequestParams =
                RequestParams.create().apply {
                    putString("productID", productId)
                    putBoolean("isUserActive", isUserActive)
                    putBoolean("isUserHasShop", isUserHasShop)
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
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(if (forceRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST).build())

        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(ProductDetailLayout::class.java) ?: listOf()
        val data = gqlResponse.getData<ProductDetailLayout>(ProductDetailLayout::class.java)
        if (data == null) {
            throw RuntimeException()
        } else if (error.isNotEmpty()) {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }

        return mapIntoModel(data)
    }

    private fun mapIntoModel(data: ProductDetailLayout): ProductDetailDataModel {
        val initialLayoutData = DynamicProductDetailMapper.mapIntoVisitable(data.data.components)
        if (requestParams.getBoolean("isUserActive", false) && !requestParams.getBoolean("isUserHasShop", false)) {
            initialLayoutData.add(ProductOpenShopDataModel())
        }
        initialLayoutData.add(ProductLastSeenDataModel())

        val getDynamicProductInfoP1 = DynamicProductDetailMapper.mapToDynamicProductDetailP1(data.data)
        return ProductDetailDataModel(getDynamicProductInfoP1, initialLayoutData)
    }
}