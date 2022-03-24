package com.tokopedia.shopadmin.feature.invitationaccepted.domain.mapper

import com.tokopedia.shopadmin.feature.invitationaccepted.domain.model.GetAdminPermissionResponse
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.model.AdminPermissionUiModel
import javax.inject.Inject

class AdminManagementInfoMapper @Inject constructor() {

    fun mapPermissionListUiModel(response: GetAdminPermissionResponse.GetAdminInfo): List<AdminPermissionUiModel> {
        return response.adminData.firstOrNull()?.permissionList?.map {
            AdminPermissionUiModel(
                iconUrl = it.iconUrl,
                permissionName = it.permissionName
            )
        } ?: emptyList()
    }
}