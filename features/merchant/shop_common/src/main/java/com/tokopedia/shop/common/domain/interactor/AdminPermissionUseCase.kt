package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.data.source.cloud.query.AdminPermissionList
import com.tokopedia.shop.common.domain.interactor.model.adminrevamp.AdminPermissionResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class AdminPermissionUseCase @Inject constructor(
        gqlRepository: GraphqlRepository): GraphqlUseCase<AdminPermissionResponse>(gqlRepository) {

    companion object {

        private const val ERROR_MESSAGE = "Failed getting admin permission response"

        fun createRequestParams(shopId: Int) =
                RequestParams.create().apply {
                    putString(AdminInfoUseCase.SOURCE_KEY, AdminInfoUseCase.SOURCE)
                    putInt(AdminInfoUseCase.SHOP_ID_KEY, shopId)
                }
    }

    init {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        setCacheStrategy(cacheStrategy)

        setGraphqlQuery(AdminPermissionList.QUERY)
        setTypeClass(AdminPermissionResponse::class.java)
    }

    suspend fun execute(requestParams: RequestParams,
                        vararg permissionToCheck: String): Boolean? {
        setRequestParams(requestParams.parameters)
        val response = executeOnBackground()
        response.adminInfo?.adminData?.let { adminData ->
            adminData.firstOrNull()?.responseDetail?.errorMessage.let { error ->
                if (error.isNullOrEmpty()) {
                    return getIsPermissionValid(
                            permissionToCheck.toList(),
                            adminData.firstOrNull()?.permissionList?.map { it.id }
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