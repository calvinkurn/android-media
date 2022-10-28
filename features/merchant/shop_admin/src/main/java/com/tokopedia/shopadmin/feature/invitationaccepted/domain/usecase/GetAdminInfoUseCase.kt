package com.tokopedia.shopadmin.feature.invitationaccepted.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.shopadmin.feature.invitationaccepted.domain.model.GetAdminPermissionResponse
import com.tokopedia.shopadmin.feature.invitationaccepted.domain.query.GET_ADMIN_INFO_QUERY
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("GetAdminInfoQuery", GET_ADMIN_INFO_QUERY)
class GetAdminInfoUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<GetAdminPermissionResponse>
) {

    init {
        useCase.setGraphqlQuery(GetAdminInfoQuery())
        useCase.setTypeClass(GetAdminPermissionResponse::class.java)
    }

    suspend fun execute(shopId: Long): List<GetAdminPermissionResponse.GetAdminInfo.AdminData.Permission> {
        useCase.setRequestParams(createRequestParams(shopId))
        return useCase.executeOnBackground().getAdminInfo.adminData.firstOrNull()?.permissionList.orEmpty()
    }

    private fun createRequestParams(shopId: Long): Map<String, Any> {
        return RequestParams.create().apply {
            putLong(SHOP_ID_KEY, shopId)
            putString(SOURCE_KEY, SOURCE)
        }.parameters
    }

    companion object {
        private const val SHOP_ID_KEY = "shop_id"
        private const val SOURCE_KEY = "source"
        private const val SOURCE = "admin-info-permission-android"
    }
}