package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant
import com.tokopedia.shop.common.graphql.data.isshopofficial.GetIsShopOfficialStore
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GqlGetIsShopOsUseCase @Inject constructor(
        @Named(GQLQueryNamedConstant.GET_IS_OFFICIAL)
        private var gqlQuery: String,
        private val gqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<GetIsShopOfficialStore>() {

    companion object {
        private const val PARAM_SHOP_ID = "shop_id"
        @JvmStatic
        fun createParams(shopId: Int): RequestParams = RequestParams.create().apply {
            putObject(PARAM_SHOP_ID, shopId)
        }
    }

    var params: RequestParams = RequestParams.EMPTY
    var isFromCacheFirst: Boolean = true
    val request by lazy {
        GraphqlRequest(gqlQuery, GetIsShopOfficialStore.Response::class.java, params.parameters)
    }

    override suspend fun executeOnBackground(): GetIsShopOfficialStore {
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(request)
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(if (isFromCacheFirst) CacheType.CACHE_FIRST else CacheType.ALWAYS_CLOUD).build())
        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(GetIsShopOfficialStore.Response::class.java)
        if (error == null || error.isEmpty()) {
            return (gqlResponse.getData(GetIsShopOfficialStore.Response::class.java) as GetIsShopOfficialStore.Response).result
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }
}