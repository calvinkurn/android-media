package com.tokopedia.product.detail.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.carttype.CartRedirectionParams
import com.tokopedia.product.detail.common.data.model.carttype.CartRedirectionResponse
import com.tokopedia.product.detail.data.model.ProductInfoP3Variant
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.product.detail.view.util.CacheStrategyUtil
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.variant_common.constant.VariantConstant
import com.tokopedia.variant_common.model.VariantMultiOriginResponse
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Yehezkiel on 2020-03-17
 */
class GetP3VariantUseCase @Inject constructor(private val rawQueries: Map<String, String>,
                                              private val graphqlRepository: GraphqlRepository) : UseCase<ProductInfoP3Variant>() {

    companion object {
        fun createParams(listOfProductId: List<String>,
                         warehouseId: String?, cartTypeParam: List<CartRedirectionParams>): RequestParams = RequestParams.create().apply {
            putString(VariantConstant.PARAM_WAREHOUSE_ID, warehouseId)
            putObject(VariantConstant.PARAM_PRODUCT_IDS, listOfProductId)
            putObject(ProductDetailCommonConstant.PARAM_CART_TYPE, cartTypeParam)
        }
    }

    var requestParams = RequestParams.EMPTY
    var forceRefresh = false

    override suspend fun executeOnBackground(): ProductInfoP3Variant {
        val p3Variant = ProductInfoP3Variant()
        val warehouseProductIds = requestParams.getObject(VariantConstant.PARAM_PRODUCT_IDS)
        val cartTypeParams = requestParams.getObject(ProductDetailCommonConstant.PARAM_CART_TYPE)
        val warehouseId = requestParams.getString(ProductDetailCommonConstant.PARAM_WAREHOUSE_ID, null)

        val warehouseParam = mapOf(ProductDetailCommonConstant.PARAM_PRODUCT_IDS to warehouseProductIds,
                ProductDetailCommonConstant.PARAM_WAREHOUSE_ID to warehouseId)
        val warehouseRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_MULTI_ORIGIN],
                VariantMultiOriginResponse::class.java, warehouseParam)

        val getCartTypeParams = mapOf(ProductDetailCommonConstant.PARAMS to cartTypeParams)
        val getCartTypeRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_CART_TYPE],
                CartRedirectionResponse::class.java, getCartTypeParams)

        val request = mutableListOf(warehouseRequest, getCartTypeRequest)

        try {
            val gqlResponse = graphqlRepository.getReseponse(request, CacheStrategyUtil.getCacheStrategy(forceRefresh))

            if (gqlResponse.getError(CartRedirectionResponse::class.java)?.isNotEmpty() != true) {
                p3Variant.cartRedirectionResponse = gqlResponse.getData<CartRedirectionResponse>(CartRedirectionResponse::class.java)
            }

            if (gqlResponse.getError(VariantMultiOriginResponse::class.java)?.isNotEmpty() != true) {
                p3Variant.variantMultiOrigin = gqlResponse.getData<VariantMultiOriginResponse>(VariantMultiOriginResponse::class.java)
            }
        } catch (t: Throwable) {
            Timber.d(t)
        }

        return p3Variant
    }

}