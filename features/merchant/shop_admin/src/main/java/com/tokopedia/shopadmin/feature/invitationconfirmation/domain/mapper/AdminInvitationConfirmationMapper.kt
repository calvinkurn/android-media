package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.mapper

import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model.AdminConfirmationRegResponse
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model.GetShopAdminInfoResponse
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model.UserProfileUpdateResponse
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model.ValidateAdminEmailResponse
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.AdminConfirmationRegUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.ShopAdminInfoUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.UserProfileUpdateUiModel
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.ValidateAdminEmailUiModel
import javax.inject.Inject

class AdminInvitationConfirmationMapper @Inject constructor() {

    fun mapToShopAdminInfoUiModel(shopAdminInfoResponse: GetShopAdminInfoResponse): ShopAdminInfoUiModel {
        val shop = shopAdminInfoResponse.shopInfoByID.result.firstOrNull()
        val shopManageID =
            shopAdminInfoResponse.getAdminInfo.adminData.firstOrNull()?.shopManageId.orEmpty()
        return ShopAdminInfoUiModel(
            shopName = shop?.shopCore?.name.orEmpty(),
            shop?.shopAssets?.avatar.orEmpty(),
            shopManageID
        )
    }

    fun mapToValidateAdminUiModel(validateAdminEmailResponse: ValidateAdminEmailResponse): ValidateAdminEmailUiModel {
        return ValidateAdminEmailUiModel(
            isSuccess = validateAdminEmailResponse.validateAdminEmail.success,
            message = validateAdminEmailResponse.validateAdminEmail.message,
            existsUser = validateAdminEmailResponse.validateAdminEmail.data.existsUser
        )
    }

    fun mapToAdminConfirmationRegUiModel(adminConfirmationReg: AdminConfirmationRegResponse.AdminConfirmationReg): AdminConfirmationRegUiModel {
        return AdminConfirmationRegUiModel(
            isSuccess = adminConfirmationReg.success,
            message = adminConfirmationReg.message,
            acceptBecomeAdmin = adminConfirmationReg.acceptBecomeAdmin
        )
    }

    fun mapToUserProfileUpdateUiModel(
        userProfileUpdateResponse: UserProfileUpdateResponse.UserProfileUpdate
    ): UserProfileUpdateUiModel {
        val errorMessage = userProfileUpdateResponse.errors.getOrNull(Int.ZERO).orEmpty()
        val isSuccess = userProfileUpdateResponse.isSuccess == Int.ONE
        return UserProfileUpdateUiModel(
            isSuccess = isSuccess,
            errorMessage = errorMessage
        )
    }
}