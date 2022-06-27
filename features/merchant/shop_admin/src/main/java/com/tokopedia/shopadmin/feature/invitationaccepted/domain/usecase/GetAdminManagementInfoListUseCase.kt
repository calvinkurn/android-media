package com.tokopedia.shopadmin.feature.invitationaccepted.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.shopadmin.feature.invitationaccepted.domain.model.GetAdminManagementInfoListResponse
import com.tokopedia.shopadmin.feature.invitationaccepted.domain.query.ADMIN_MANAGEMENT_INFO_LIST_QUERY
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("GetAdminManagementInfoListQuery", ADMIN_MANAGEMENT_INFO_LIST_QUERY)
class GetAdminManagementInfoListUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<GetAdminManagementInfoListResponse>
) {

    init {
        useCase.setGraphqlQuery(GetAdminManagementInfoListQuery())
        useCase.setTypeClass(GetAdminManagementInfoListResponse::class.java)
    }

    suspend fun execute(shopId: String): List<GetAdminManagementInfoListResponse.GetAdminManagementInfoList.AllPermission> {
        useCase.setRequestParams(createRequestParams(shopId))
        return useCase.executeOnBackground().getAdminManagementInfoList.allPermissionList
    }

    fun createRequestParams(shopId: String): Map<String, Any> {
        return RequestParams.create().apply {
            putString(SHOP_ID_KEY, shopId)
        }.parameters
    }

    companion object {
        private const val SHOP_ID_KEY = "shop_id"
    }
}