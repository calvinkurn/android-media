package com.tokopedia.loginregister.redefine_register_email.input_phone.abstraction

import com.tokopedia.loginregister.redefine_register_email.input_phone.domain.GetUserInfoUseCase
import com.tokopedia.loginregister.redefine_register_email.input_phone.domain.data.GetUserInfoModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface

//this abstraction exclude getAdminType
abstract class GetProfileInfoAbstraction(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val userSession: UserSessionInterface
) {

    private suspend fun result(): Result<GetUserInfoModel> {
        val response = getUserInfoUseCase(Unit)

        val profile = response.profileInfo
        val isProfileValid = profile.userId.isNotBlank() && profile.userId != "0"

        return when {
            isProfileValid -> {
                saveProfileData(response)
                Success(response)
            }
            else -> Fail(Throwable())
        }
    }

    private fun saveProfileData(pojo: GetUserInfoModel) {
        pojo.run {
            userSession.setHasPassword(profileInfo.isCreatedPassword)
            userSession.profilePicture = profileInfo.profilePicture
            userSession.setIsMSISDNVerified(profileInfo.isPhoneVerified)

            userSession.setLoginSession(
                true,
                profileInfo.userId,
                profileInfo.fullName,
                shopInfo.shopData.shopId,
                profileInfo.isPhoneVerified,
                shopInfo.shopData.shopName,
                profileInfo.email,
                isShopGold(shopInfo.shopData.shopLevel),
                profileInfo.phone
            )
            userSession.setIsShopOfficialStore(isOfficialStore(shopInfo.shopData.shopLevel))
            userSession.shopAvatar = shopInfo.shopData.shopAvatar
        }
    }

    private fun isShopGold(shopLevel: Int): Boolean {
        return shopLevel == LEVEL_GOLD || shopLevel == LEVEL_OFFICIAL_STORE
    }

    private fun isOfficialStore(shopLevel: Int): Boolean {
        return shopLevel == LEVEL_OFFICIAL_STORE
    }

    suspend fun data(): Result<GetUserInfoModel> = result()

    companion object {
        private const val LEVEL_GOLD = 1
        private const val LEVEL_OFFICIAL_STORE = 2
    }

}