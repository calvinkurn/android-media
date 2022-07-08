package com.tokopedia.shopadmin.common.domain.mapper

import com.tokopedia.shopadmin.common.domain.model.GetAdminTypeResponse
import com.tokopedia.shopadmin.common.presentation.uimodel.AdminTypeUiModel
import javax.inject.Inject

class AdminTypeMapper @Inject constructor() {

    fun mapToAdminTypeUiModel(adminInfoResponse: GetAdminTypeResponse): AdminTypeUiModel {
        val adminType = adminInfoResponse.getAdminType
        val adminRoleType = adminType.adminData.detailInformation.adminRoleType
        return AdminTypeUiModel(
            adminType.adminData.status,
            adminType.shopID,
            adminRoleType.isShopAdmin
        )
    }
}