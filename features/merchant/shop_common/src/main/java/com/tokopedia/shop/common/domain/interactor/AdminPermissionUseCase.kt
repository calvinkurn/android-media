package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.data.source.cloud.query.AdminPermissionList
import com.tokopedia.shop.common.domain.interactor.model.adminrevamp.AdminPermissionResponse
import javax.inject.Inject

class AdminPermissionUseCase @Inject constructor(
        gqlRepository: GraphqlRepository): GraphqlUseCase<AdminPermissionResponse>(gqlRepository) {

    companion object {
        private const val ERROR_MESSAGE = "Failed getting admin permission response"
    }

    init {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        setCacheStrategy(cacheStrategy)

        setGraphqlQuery(AdminPermissionList.QUERY)
        setTypeClass(AdminPermissionResponse::class.java)
    }

    suspend fun execute(vararg permissionToCheck: String): Boolean {
        val response = executeOnBackground()
        response.adminInfo?.let { info ->
            info.responseDetail?.errorMessage.let { error ->
                if (error.isNullOrEmpty()) {
                    return getIsPermissionValid(
                            permissionToCheck.toList(),
                            info.adminData?.firstOrNull()?.permissionList?.map { it.id }
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