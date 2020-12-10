package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.data.source.cloud.query.AdminInfo
import com.tokopedia.shop.common.domain.interactor.model.adminrevamp.AdminInfoResponse
import com.tokopedia.shop.common.domain.interactor.model.adminrevamp.AdminInfoResult
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class AdminInfoUseCase @Inject constructor(
        gqlRepository: GraphqlRepository): GraphqlUseCase<AdminInfoResponse>(gqlRepository) {

    companion object {
        const val SOURCE = "akw-testing"

        const val SOURCE_KEY = "source"
        const val SHOP_ID_KEY = "shopId"

        private const val ERROR_MESSAGE = "Failed getting admin info response"

        fun createRequestParams(shopId: Int) =
                RequestParams.create().apply {
                    putString(SOURCE_KEY, SOURCE)
                    putInt(SHOP_ID_KEY, shopId)
                }
    }

    init {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        setCacheStrategy(cacheStrategy)

        setGraphqlQuery(AdminInfo.QUERY)
        setTypeClass(AdminInfoResponse::class.java)
    }

    suspend fun execute(requestParams: RequestParams): AdminInfoResult {
        setRequestParams(requestParams.parameters)
        try {
            val response = executeOnBackground()
            response.adminInfo?.adminData?.let { adminData ->
                adminData.firstOrNull()?.responseDetail?.errorMessage.let { error ->
                    if (error.isNullOrEmpty()) {
                        return AdminInfoResult.Success(adminData.firstOrNull())
                    } else {
                        throw MessageErrorException(error)
                    }
                }
            }
            throw MessageErrorException(ERROR_MESSAGE)
        } catch (ex: Exception) {
            return AdminInfoResult.Fail(ex)
        }
    }

}