package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant
import com.tokopedia.shop.common.graphql.data.isshopofficial.GetIsShopOfficialStore
import com.tokopedia.shop.common.graphql.data.isshoppowermerchant.GetIsShopPowerMerchant
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GqlGetIsShopPmUseCase @Inject constructor(
        @Named(GQLQueryNamedConstant.GET_IS_POWER_MERCHANT)
        private var gqlQuery: String,
        private val gqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<GetIsShopPowerMerchant>() {

    companion object {
        private const val PARAM_SHOP_ID = "shopID"
        private const val PARAM_INCLUDE_OS = "includeOS"
        @JvmStatic
        fun createParams(shopId: Int): RequestParams = RequestParams.create().apply {
            putObject(PARAM_SHOP_ID, shopId)
            putObject(PARAM_INCLUDE_OS, false)
        }
    }

    var params: RequestParams = RequestParams.EMPTY
    var isFromCacheFirst: Boolean = true
    val request by lazy {
        GraphqlRequest(gqlQuery, GetIsShopPowerMerchant.Response::class.java, params.parameters)
    }

    override suspend fun executeOnBackground(): GetIsShopPowerMerchant {
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(request)
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(if (isFromCacheFirst) CacheType.CACHE_FIRST else CacheType.ALWAYS_CLOUD).build())
        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(GetIsShopPowerMerchant.Response::class.java)
        if (error == null || error.isEmpty()) {
            return (gqlResponse.getData(GetIsShopPowerMerchant.Response::class.java) as GetIsShopPowerMerchant.Response).result
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }
}