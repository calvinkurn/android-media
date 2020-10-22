package com.tokopedia.shop.pageheader.domain.interactor

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant
import com.tokopedia.shop.common.graphql.data.isshopofficial.GetIsShopOfficialStore
import com.tokopedia.shop.common.graphql.data.isshoppowermerchant.GetIsShopPowerMerchant
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.pageheader.ShopPageHeaderConstant.SHOP_PAGE_GET_HOME_TYPE
import com.tokopedia.shop.pageheader.data.model.ShopPageGetHomeType
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GqlShopPageGetHomeType @Inject constructor(
        @Named(SHOP_PAGE_GET_HOME_TYPE)
        private var gqlQuery: String,
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
        GraphqlRequest(gqlQuery, ShopPageGetHomeType.Response::class.java, params.parameters)
    }

    override suspend fun executeOnBackground(): ShopPageGetHomeType {
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(request)
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(if (isFromCacheFirst) CacheType.CACHE_FIRST else CacheType.ALWAYS_CLOUD).build())
        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(ShopPageGetHomeType.Response::class.java)
        if (error == null || error.isEmpty()) {
            return (gqlResponse.getData(ShopPageGetHomeType.Response::class.java) as ShopPageGetHomeType.Response).shopPageGetHomeType
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }
}