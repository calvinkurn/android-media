package com.tokopedia.sessioncommon.domain.commonaction

import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface

abstract class GetProfileInfoSaveSession(
    private val profilePojo: ProfilePojo,
    private val userSession: UserSessionInterface
) {

    private fun result(): Result<ProfilePojo> {
        
        val profile = profilePojo.profileInfo
        val isProfileValid = profile.userId.isNotBlank() && profile.userId != "0"

        return when {
            isProfileValid -> {
                saveProfileData(profilePojo)
                Success(profilePojo)
            }
            else -> Fail(Throwable())
        }
    }

    private fun saveProfileData(pojo: ProfilePojo) {
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

    fun data(): Result<ProfilePojo> = result()

    companion object {
        private const val LEVEL_GOLD = 1
        private const val LEVEL_OFFICIAL_STORE = 2
    }

}