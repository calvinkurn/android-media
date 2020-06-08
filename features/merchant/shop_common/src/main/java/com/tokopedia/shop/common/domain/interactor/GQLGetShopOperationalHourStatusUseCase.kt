package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant.GQL_GET_SHOP_OPERATIONAL_HOUR_STATUS
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopoperationalhourstatus.ShopOperationalHourStatus
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GQLGetShopOperationalHourStatusUseCase @Inject constructor(
        @Named(GQL_GET_SHOP_OPERATIONAL_HOUR_STATUS)
        private var gqlQuery: String,
        private val gqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<ShopOperationalHourStatus>() {

    var params: RequestParams = RequestParams.EMPTY
    val request by lazy {
        GraphqlRequest(gqlQuery, ShopOperationalHourStatus.Response::class.java, params.parameters)
    }

    override suspend fun executeOnBackground(): ShopOperationalHourStatus {
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(request)
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(ShopOperationalHourStatus.Response::class.java)
        if (error == null || error.isEmpty()) {
            return (gqlResponse.getData(ShopOperationalHourStatus.Response::class.java) as ShopOperationalHourStatus.Response).shopOperationalHourStatus
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    companion object {
        private const val PARAM_SHOP_ID = "shopID"
        private const val PARAM_TYPE = "type"
        private const val DEFAULT_VALUE_TYPE = 1
        @JvmStatic
        fun createParams(
                shopIds: String,
                type: Int = DEFAULT_VALUE_TYPE
        ): RequestParams = RequestParams.create().apply {
            putObject(PARAM_SHOP_ID, shopIds)
            putObject(PARAM_TYPE, type)
        }
    }
}