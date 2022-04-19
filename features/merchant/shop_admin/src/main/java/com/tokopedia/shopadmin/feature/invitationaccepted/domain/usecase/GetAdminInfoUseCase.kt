package com.tokopedia.shopadmin.feature.invitationaccepted.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.shopadmin.feature.invitationaccepted.domain.model.GetAdminPermissionResponse
import com.tokopedia.shopadmin.feature.invitationaccepted.domain.query.GetAdminInfoListQuery
import javax.inject.Inject

class GetAdminInfoUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<GetAdminPermissionResponse>
) {

    init {
        useCase.setGraphqlQuery(GetAdminInfoListQuery)
        useCase.setTypeClass(GetAdminPermissionResponse::class.java)
    }

    suspend fun execute(shopId: Long): List<GetAdminPermissionResponse.GetAdminInfo.AdminData.Permission> {
        useCase.setRequestParams(GetAdminInfoListQuery.createRequestParams(shopId))
        return useCase.executeOnBackground().getAdminInfo.adminData.firstOrNull()?.permissionList.orEmpty()
    }
}