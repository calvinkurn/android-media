package com.tokopedia.shopadmin.feature.invitationaccepted.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.shopadmin.feature.invitationaccepted.domain.mapper.AdminManagementInfoMapper
import com.tokopedia.shopadmin.feature.invitationaccepted.domain.model.GetAdminPermissionResponse
import com.tokopedia.shopadmin.feature.invitationaccepted.domain.query.GetAdminManagementInfoListQuery
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.model.AdminPermissionUiModel
import javax.inject.Inject

class GetAdminPermissionListUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<GetAdminPermissionResponse>,
    private val adminManagementInfoMapper: AdminManagementInfoMapper
) {

    init {
        useCase.setGraphqlQuery(GetAdminManagementInfoListQuery)
        useCase.setTypeClass(GetAdminPermissionResponse::class.java)
    }

    suspend fun execute(shopId: Long): List<AdminPermissionUiModel> {
        useCase.setRequestParams(GetAdminManagementInfoListQuery.createRequestParams(shopId))
        return adminManagementInfoMapper.mapPermissionListUiModel(useCase.executeOnBackground().getAdminInfo)
    }
}