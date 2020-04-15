package com.tokopedia.product.detail.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.carttype.CartRedirectionParams
import com.tokopedia.product.detail.common.data.model.carttype.CartRedirectionResponse
import com.tokopedia.product.detail.data.model.ProductInfoP3Variant
import com.tokopedia.product.detail.data.model.UserCodStatus
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
class GetProductInfoP3VariantUseCase @Inject constructor(private val rawQueries: Map<String, String>,
                                                         private val graphqlRepository: GraphqlRepository) : UseCase<ProductInfoP3Variant>() {

    companion object {
        fun createParams(isVariant: Boolean, needRequestCod: Boolean,
                         cartTypeParam: List<CartRedirectionParams>): RequestParams = RequestParams.create().apply {
            putBoolean(VariantConstant.PARAM_IS_VARIANT, isVariant)
            putBoolean(ProductDetailCommonConstant.PARAM_NEED_REQUEST_COD, needRequestCod)
            putObject(ProductDetailCommonConstant.PARAM_CART_TYPE, cartTypeParam)
        }
    }

    var requestParams = RequestParams.EMPTY
    var forceRefresh = false

    override suspend fun executeOnBackground(): ProductInfoP3Variant {
        val p3VariantResponse = ProductInfoP3Variant()
        val cartTypeParams = requestParams.getObject(ProductDetailCommonConstant.PARAM_CART_TYPE)
        val isVariant = requestParams.getBoolean(VariantConstant.PARAM_IS_VARIANT, false)
        val needRequestCod = requestParams.getBoolean(ProductDetailCommonConstant.PARAM_NEED_REQUEST_COD, false)

        val p3VariantRequest = mutableListOf<GraphqlRequest>()

        if (isVariant) {
            val getCartTypeParams = mapOf(ProductDetailCommonConstant.PARAMS to cartTypeParams)
            val getCartTypeRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_CART_TYPE],
                    CartRedirectionResponse::class.java, getCartTypeParams)
            p3VariantRequest.add(getCartTypeRequest)
        }


        if (needRequestCod) {
            val userCodParams = mapOf(ProductDetailCommonConstant.PARAM_IS_PDP to true)
            val userCodRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_USER_COD_STATUS],
                    UserCodStatus.Response::class.java, userCodParams)
            p3VariantRequest.add(userCodRequest)
        }

        try {
            val gqlResponse = graphqlRepository.getReseponse(p3VariantRequest, CacheStrategyUtil.getCacheStrategy(forceRefresh))

            if (gqlResponse.getError(CartRedirectionResponse::class.java)?.isNotEmpty() != true) {
                p3VariantResponse.cartRedirectionResponse = gqlResponse.getData<CartRedirectionResponse>(CartRedirectionResponse::class.java)
            }

            if (needRequestCod && gqlResponse.getError(UserCodStatus.Response::class.java)?.isNotEmpty() != true) {
                p3VariantResponse.userCod = gqlResponse.getData<UserCodStatus.Response>(UserCodStatus.Response::class.java)
                        .result.userCodStatus.isCod
            }

        } catch (t: Throwable) {
            Timber.d(t)
        }

        return p3VariantResponse
    }

}