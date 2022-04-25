package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.mapper

import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model.AdminConfirmationRegResponse
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model.GetShopAdminInfoResponse
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model.ValidateAdminEmailResponse
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.AdminConfirmationRegUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.ShopAdminInfoUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.ValidateAdminEmailUiModel
import javax.inject.Inject

class AdminInvitationConfirmationMapper @Inject constructor() {

    fun mapToShopAdminInfoUiModel(shopAdminInfoResponse: GetShopAdminInfoResponse): ShopAdminInfoUiModel {
        val shop = shopAdminInfoResponse.shop
        val shopManageID =
            shopAdminInfoResponse.getAdminInfo.adminData.firstOrNull()?.shopManageId.orEmpty()
        return ShopAdminInfoUiModel(shopName = shop.shopName, shop.logo, shopManageID)
    }

    fun mapToValidateAdminUiModel(validateAdminEmailResponse: ValidateAdminEmailResponse): ValidateAdminEmailUiModel {
        return ValidateAdminEmailUiModel(
            isSuccess = validateAdminEmailResponse.validateAdminEmail.success,
            message = validateAdminEmailResponse.validateAdminEmail.message,
            existsUser = validateAdminEmailResponse.validateAdminEmail.data.existsUser
        )
    }

    fun mapToAdminConfirmationRegUiModel(adminConfirmationRegResponse: AdminConfirmationRegResponse): AdminConfirmationRegUiModel {
        return AdminConfirmationRegUiModel(
            isSuccess = adminConfirmationRegResponse.success,
            message = adminConfirmationRegResponse.message,
            acceptBecomeAdmin = adminConfirmationRegResponse.acceptBecomeAdmin
        )
    }
}