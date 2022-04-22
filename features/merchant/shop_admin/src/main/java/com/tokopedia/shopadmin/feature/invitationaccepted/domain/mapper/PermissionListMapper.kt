package com.tokopedia.shopadmin.feature.invitationaccepted.domain.mapper

import com.tokopedia.shopadmin.feature.invitationaccepted.domain.model.GetAdminManagementInfoListResponse
import com.tokopedia.shopadmin.feature.invitationaccepted.domain.model.GetAdminPermissionResponse
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.model.AdminPermissionUiModel
import javax.inject.Inject

class PermissionListMapper @Inject constructor() {

    fun mapAdminPermissionListUiModel(
        allPermissionList: List<GetAdminManagementInfoListResponse.GetAdminManagementInfoList.AllPermission>,
        adminPermissionList: List<GetAdminPermissionResponse.GetAdminInfo.AdminData.Permission>
    ): List<AdminPermissionUiModel> {
        val adminPermissionResultList =
            hashMapOf<String, GetAdminManagementInfoListResponse.GetAdminManagementInfoList.AllPermission>()

        allPermissionList.forEach allPermission@{ allPermission ->
            adminPermissionList.forEach adminPermission@{ adminPermission ->
                if (adminPermission.permissionId == allPermission.permissionId) {
                    adminPermissionResultList[allPermission.permissionId] = allPermission
                } else {
                    if (allPermission.permissionRecursive.isNotEmpty()) {
                        allPermission.permissionRecursive.forEach permissionRecursive@{ permissionRecursive ->
                            if (adminPermissionResultList[allPermission.permissionId] == null) {
                                if (permissionRecursive.permissionId == adminPermission.permissionId) {
                                    adminPermissionResultList[allPermission.permissionId] = allPermission
                                }
                            } else {
                                return@allPermission
                            }
                        }
                    }
                }
            }
        }


        return adminPermissionResultList.map {
            AdminPermissionUiModel(iconUrl = it.value.iconURL, permissionName = it.value.permissionName)
        }
    }
}