package com.tokopedia.product.detail.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.carttype.CartRedirectionParams
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeResponse
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.product.detail.view.util.CacheStrategyUtil
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by Yehezkiel on 2020-03-16
 */
class GetCartTypeUseCase @Inject constructor(private val rawQueries: Map<String, String>,
                                             private val graphqlRepository: GraphqlRepository) : UseCase<CartTypeResponse>() {

    companion object {
        fun createParam(cartTypeParam: List<CartRedirectionParams>): RequestParams =
                RequestParams.create().apply {
                    putObject(ProductDetailCommonConstant.PARAMS, cartTypeParam)
                }
    }

    val requestParams = RequestParams.EMPTY
    var forceRefresh = false

    override suspend fun executeOnBackground(): CartTypeResponse {
        val cartTypeRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_CART_TYPE],
                CartTypeResponse::class.java, requestParams.parameters)

        val gqlResponse = graphqlRepository.getReseponse(listOf(cartTypeRequest), CacheStrategyUtil.getCacheStrategy(forceRefresh))
        val data = gqlResponse.getData<CartTypeResponse>(CartTypeResponse::class.java)
        val error = gqlResponse.getError(CartTypeResponse::class.java) ?: listOf()

        if (error.isNotEmpty() && error.firstOrNull()?.message?.isNotEmpty() == true) {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        } else if (data == null) {
            throw RuntimeException()
        }

        return data
    }

}