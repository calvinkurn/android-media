package com.tokopedia.shopadmin.invitationaccepted.domain.mapper

import com.tokopedia.shopadmin.invitationaccepted.domain.model.GetAdminManagementInfoListResponse
import com.tokopedia.shopadmin.invitationaccepted.presentation.model.AdminPermissionUiModel
import com.tokopedia.shopadmin.invitationaccepted.presentation.model.InvitationAcceptedUiModel
import javax.inject.Inject

class AdminManagementInfoMapper @Inject constructor() {

    fun mapInvitationAcceptedUiModel(response: GetAdminManagementInfoListResponse): InvitationAcceptedUiModel {
        val getAdminManagementInfoList = response.getAdminManagementInfoList
        return InvitationAcceptedUiModel(
            adminPermissionUiModel = getAdminManagementInfoList.allPermissionList.map {
                AdminPermissionUiModel(
                    iconUrl = it.iconURL,
                    permissionName = it.permissionName
                )
            }
        )
    }
}