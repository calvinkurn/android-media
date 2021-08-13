package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.data.source.cloud.query.GetAdminInfoShopLocation
import com.tokopedia.shop.common.domain.interactor.model.adminrevamp.AdminInfoResponse
import com.tokopedia.shop.common.domain.interactor.model.adminrevamp.ShopLocationResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetAdminInfoShopLocationUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
): GraphqlUseCase<AdminInfoResponse>(gqlRepository) {

    companion object {
        private const val DEFAULT_SOURCE = "android"

        private const val SOURCE_KEY = "source"
        private const val SHOP_ID_KEY = "shopId"

        private const val ERROR_MESSAGE = "Failed to get shop location"
    }

    init {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        setCacheStrategy(cacheStrategy)

        setGraphqlQuery(GetAdminInfoShopLocation.QUERY)
        setTypeClass(AdminInfoResponse::class.java)
    }

    suspend fun execute(shopId: Int, source: String = DEFAULT_SOURCE): List<ShopLocationResponse> {
        val requestParams = RequestParams.create().apply {
            putString(SOURCE_KEY, source)
            putInt(SHOP_ID_KEY, shopId)
        }

        setRequestParams(requestParams.parameters)
        val response = executeOnBackground()

        val adminData = response.adminInfo?.adminData?.firstOrNull()
        val errorMessage = adminData?.responseDetail?.errorMessage
        val shopLocation = adminData?.locationList

        return when {
            shopLocation != null -> shopLocation
            errorMessage != null -> throw MessageErrorException(errorMessage)
            else -> throw MessageErrorException(ERROR_MESSAGE)
        }
    }
}