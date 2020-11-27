package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.shop.common.data.source.cloud.query.AdminInfo
import com.tokopedia.shop.common.domain.interactor.model.AdminInfoData
import com.tokopedia.shop.common.domain.interactor.model.AdminInfoResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class AdminInfoUseCase @Inject constructor(
        gqlRepository: GraphqlRepository): GraphqlUseCase<AdminInfoResponse>(gqlRepository) {

    companion object {
        private const val SOURCE = "akw-testing"

        private const val SOURCE_KEY = "source"
        private const val SHOP_ID_KEY = "shopId"

        fun createRequestParams(shopId: Int) =
                RequestParams.create().apply {
                    putString(SOURCE_KEY, SOURCE)
                    putString(SHOP_ID_KEY, shopId.toString())
                }
    }

    init {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        setCacheStrategy(cacheStrategy)

        setGraphqlQuery(AdminInfo.QUERY)
        setTypeClass(AdminInfoResponse::class.java)
    }

    suspend fun execute(requestParams: RequestParams): AdminInfoData? {
        setRequestParams(requestParams.parameters)
        val response = executeOnBackground()
        return response.adminInfo?.adminData
    }

}