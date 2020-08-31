package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant.GQL_CHECK_WISHLIST
import com.tokopedia.shop.common.graphql.data.checkwishlist.CheckWishlistResult
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GQLCheckWishlistUseCase @Inject constructor(
        @Named(GQL_CHECK_WISHLIST)
        private var gqlQuery: String,
        private val gqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<List<CheckWishlistResult>>() {

    companion object {
        private const val KEY_PRODUCT_ID = "productID"

        @JvmStatic
        fun createParams(
                listProductIdString: String
        ): RequestParams = RequestParams.create().apply {
            putObject(KEY_PRODUCT_ID, listProductIdString)
        }
    }

    var params: RequestParams = RequestParams.EMPTY
    val request by lazy {
        GraphqlRequest(gqlQuery, CheckWishlistResult.Response::class.java, params.parameters)
    }

    override suspend fun executeOnBackground(): List<CheckWishlistResult> {
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(request)
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(CheckWishlistResult.Response::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData<CheckWishlistResult.Response>(CheckWishlistResult.Response::class.java).checkWishlist
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }
}