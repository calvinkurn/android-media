package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.mapper

import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model.GetAdminInfoResponse
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model.GetShopAdminInfoResponse
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.model.AdminDataUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.model.AdminInfoUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.model.ShopAdminInfoUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.model.AdminTypeUiModel
import javax.inject.Inject

class AdminInvitationConfirmationMapper @Inject constructor() {

    fun mapToAdminInfoUiModel(getAdminInfoResponse: GetAdminInfoResponse): AdminInfoUiModel {
        return AdminInfoUiModel(
            adminTypeUiModel = mapToAdminTypeUiModel(getAdminInfoResponse),
            adminDataUiModel = mapToAdminDataUiModel(getAdminInfoResponse)
        )
    }

    fun mapToShopAdminInfoUiModel(shopAdminInfoResponse: GetShopAdminInfoResponse): ShopAdminInfoUiModel {
        val shop = shopAdminInfoResponse.shop
        return ShopAdminInfoUiModel(shopName = shop.shopName, shop.logo)
    }

    private fun mapToAdminTypeUiModel(adminInfoResponse: GetAdminInfoResponse): AdminTypeUiModel {
        return AdminTypeUiModel(adminInfoResponse.getAdminType.adminData.status)
    }

    private fun mapToAdminDataUiModel(getAdminInfoResponse: GetAdminInfoResponse): AdminDataUiModel {
        return AdminDataUiModel(
            getAdminInfoResponse.getAdminInfo.adminData.firstOrNull()?.shopManageId.orEmpty()
        )
    }
}