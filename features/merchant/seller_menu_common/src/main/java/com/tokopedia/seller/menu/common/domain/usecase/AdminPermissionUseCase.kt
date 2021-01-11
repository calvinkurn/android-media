package com.tokopedia.seller.menu.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.menu.common.domain.entity.AdminPermissionResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class AdminPermissionUseCase @Inject constructor(gqlRepository: GraphqlRepository): GraphqlUseCase<AdminPermissionResponse>(gqlRepository) {

    companion object {
        private const val ERROR_MESSAGE = "Failed getting permissions"

        private const val QUERY = "query AdminType() {\n" +
                "  getAdminType(source: \"akw-testing\"){\n" +
                "    admin_data {\n" +
                "      permission_list {\n" +
                "        permission_id\n" +
                "      }\n" +
                "    }\n" +
                "    response_detail {\n" +
                "      error_message\n" +
                "    }\n" +
                "  }\n" +
                "}"
    }

    init {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        setCacheStrategy(cacheStrategy)

        setGraphqlQuery(QUERY)
        setTypeClass(AdminPermissionResponse::class.java)
    }

    var permissionIds: List<String> = listOf()

    suspend fun execute(): Boolean {
        setRequestParams(RequestParams.EMPTY.parameters)
        executeOnBackground().let { response ->
            response.adminPermissionData?.responseDetail?.errorMessage.let { error ->
                if (error.isNullOrEmpty()) {
                    response.adminPermissionData?.adminData?.permissionList?.let { permissions ->
                        return permissions.map { it.permissionId }.containsAll(permissionIds)
                    }
                    throw MessageErrorException(ERROR_MESSAGE)
                } else {
                    throw MessageErrorException(error)
                }
            }
        }
    }

}