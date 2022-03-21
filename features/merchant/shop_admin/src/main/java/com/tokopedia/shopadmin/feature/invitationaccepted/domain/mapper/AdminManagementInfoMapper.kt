package com.tokopedia.shopadmin.feature.invitationaccepted.domain.mapper

import com.tokopedia.shopadmin.feature.invitationaccepted.domain.model.GetAdminManagementInfoListResponse
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.model.AdminPermissionUiModel
import javax.inject.Inject

class AdminManagementInfoMapper @Inject constructor() {

    fun mapPermissionListUiModel(response: GetAdminManagementInfoListResponse): List<AdminPermissionUiModel> {
        return response.getAdminManagementInfoList.allPermissionList.map {
                AdminPermissionUiModel(
                    iconUrl = it.iconURL,
                    permissionName = it.permissionName
                )
            }
    }
}