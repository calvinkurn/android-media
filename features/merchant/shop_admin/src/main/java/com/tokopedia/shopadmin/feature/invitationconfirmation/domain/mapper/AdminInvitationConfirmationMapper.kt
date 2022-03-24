package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.mapper

import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model.AdminConfirmationRegResponse
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model.GetAdminInfoResponse
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model.GetShopAdminInfoResponse
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model.ValidateAdminEmailResponse
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.model.AdminConfirmationRegUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.model.ShopAdminInfoUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.model.AdminTypeUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.model.ValidateEmailUiModel
import javax.inject.Inject

class AdminInvitationConfirmationMapper @Inject constructor() {

    fun mapToShopAdminInfoUiModel(shopAdminInfoResponse: GetShopAdminInfoResponse): ShopAdminInfoUiModel {
        val shop = shopAdminInfoResponse.shop
        val shopManageID = shopAdminInfoResponse.getAdminInfo.adminData.firstOrNull()?.shopManageId.orEmpty()
        return ShopAdminInfoUiModel(shopName = shop.shopName, shop.logo, shopManageID)
    }

    fun mapToValidateAdminUiModel(validateAdminEmailResponse: ValidateAdminEmailResponse.ValidateAdminEmail): ValidateEmailUiModel {
        return ValidateEmailUiModel(
            isSuccess = validateAdminEmailResponse.success,
            message = validateAdminEmailResponse.message,
            existsUser = validateAdminEmailResponse.data.existsUser
        )
    }

    fun mapToAdminConfirmationRegUiModel(adminConfirmationRegResponse: AdminConfirmationRegResponse): AdminConfirmationRegUiModel {
        return AdminConfirmationRegUiModel(
            isSuccess = adminConfirmationRegResponse.success,
            message = adminConfirmationRegResponse.message,
            acceptBecomeAdmin = adminConfirmationRegResponse.acceptBecomeAdmin
        )
    }

    fun mapToAdminTypeUiModel(adminInfoResponse: GetAdminInfoResponse): AdminTypeUiModel {
        val adminType = adminInfoResponse.getAdminType
//        return AdminTypeUiModel(adminType.adminData.status, adminType.shopID)
        return AdminTypeUiModel("2", adminType.shopID)
    }
}