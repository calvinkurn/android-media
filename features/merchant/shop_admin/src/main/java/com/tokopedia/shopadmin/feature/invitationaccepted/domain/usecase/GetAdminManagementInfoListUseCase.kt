package com.tokopedia.shopadmin.feature.invitationaccepted.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.shopadmin.feature.invitationaccepted.domain.model.GetAdminManagementInfoListResponse
import com.tokopedia.shopadmin.feature.invitationaccepted.domain.query.GetAdminManagementInfoListQuery
import javax.inject.Inject

class GetAdminManagementInfoListUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<GetAdminManagementInfoListResponse>
) {

    init {
        useCase.setGraphqlQuery(GetAdminManagementInfoListQuery)
        useCase.setTypeClass(GetAdminManagementInfoListResponse::class.java)
    }

    suspend fun execute(shopId: String): List<GetAdminManagementInfoListResponse.GetAdminManagementInfoList.AllPermission> {
        useCase.setRequestParams(GetAdminManagementInfoListQuery.createRequestParams(shopId))
        return useCase.executeOnBackground().getAdminManagementInfoList.allPermissionList
    }
}