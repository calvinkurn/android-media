package com.tokopedia.seller.active.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.active.common.data.query.AdminInfo
import com.tokopedia.seller.active.common.domain.model.AdminInfoResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class AdminPermissionUseCase @Inject constructor(
        gqlRepository: GraphqlRepository): GraphqlUseCase<AdminInfoResponse>(gqlRepository) {

    companion object {

        private const val ERROR_MESSAGE = "Failed getting admin permission response"

        fun createRequestParams(shopId: String) =
                RequestParams.create().apply {
                    putString(AdminInfoUseCase.SOURCE_KEY, AdminInfoUseCase.SOURCE)
                    putString(AdminInfoUseCase.SHOP_ID_KEY, shopId)
                }
    }

    init {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        setCacheStrategy(cacheStrategy)

        setGraphqlQuery(AdminInfo.QUERY)
        setTypeClass(AdminInfoResponse::class.java)
    }

    suspend fun execute(requestParams: RequestParams,
                        vararg permissionToCheck: String): Boolean? {
        setRequestParams(requestParams.parameters)
        val response = executeOnBackground()
        response.adminInfo?.adminData?.let { adminData ->
            adminData.responseDetail?.errorMessage.let { error ->
                if (error.isNullOrEmpty()) {
                    return getIsPermissionValid(
                            permissionToCheck.toList(),
                            adminData.permissionList?.map { it.id }
                    )
                } else {
                    throw MessageErrorException(error)
                }
            }
        }
        throw MessageErrorException(ERROR_MESSAGE)
    }

    private fun getIsPermissionValid(permissionToCheckList: List<String>,
                                     permissionList: List<String?>?): Boolean =
            permissionList?.containsAll(permissionToCheckList) ?: false

}