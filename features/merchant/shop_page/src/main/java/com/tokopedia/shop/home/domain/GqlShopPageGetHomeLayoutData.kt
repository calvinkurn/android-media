package com.tokopedia.shop.home.domain

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.constant.ShopPageGetHomeTypeQuery
import com.tokopedia.shop.pageheader.data.model.ShopPageGetHomeType
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GqlShopPageGetHomeLayoutData @Inject constructor(
        private val gqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<ShopPageGetHomeType>() {

    companion object {
        private const val PARAM_SHOP_ID = "shopID"
        @JvmStatic
        fun createParams(shopId: Int): RequestParams = RequestParams.create().apply {
            putObject(PARAM_SHOP_ID, shopId)
        }
    }

    var params: RequestParams = RequestParams.EMPTY
    var isFromCacheFirst: Boolean = true
    val request by lazy {
        GraphqlRequest(
                ShopPageGetHomeTypeQuery.getShopHomeLayoutDataQuery(),
                ShopPageGetHomeType.Response::class.java, params.parameters
        )
    }

    override suspend fun executeOnBackground(): ShopPageGetHomeType {
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(request)
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(ShopPageGetHomeType.Response::class.java)
        if (error == null || error.isEmpty()) {
            return (gqlResponse.getData(ShopPageGetHomeType.Response::class.java) as ShopPageGetHomeType.Response).shopPageGetHomeType
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

}