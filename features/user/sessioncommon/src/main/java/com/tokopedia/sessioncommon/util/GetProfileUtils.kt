package com.tokopedia.sessioncommon.util

import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by nisie on 11/06/19.
 */
class GetProfileUtils @Inject constructor(
    val userSession: UserSessionInterface
) {
    fun saveProfileData(pojo: ProfilePojo?) {
        pojo?.profileInfo?.run {
            userSession.setHasPassword(pojo.profileInfo.isCreatedPassword)
            userSession.profilePicture = pojo.profileInfo.profilePicture
            userSession.setIsMSISDNVerified(pojo.profileInfo.isPhoneVerified)

            userSession.setLoginSession(
                true,
                pojo.profileInfo.userId,
                pojo.profileInfo.fullName,
                pojo.shopInfo.shopData.shopId,
                pojo.profileInfo.isPhoneVerified,
                pojo.shopInfo.shopData.shopName,
                pojo.profileInfo.email,
                isShopGold(pojo.shopInfo.shopData.shopLevel),
                pojo.profileInfo.phone
            )
            userSession.setIsShopOfficialStore(isOfficialStore(pojo.shopInfo.shopData.shopLevel))
            userSession.shopAvatar = pojo.shopInfo.shopData.shopAvatar
            userSession.shopAvatarOriginal = pojo.shopInfo.shopData.shopAvatarOriginal
        }
    }

    private fun isShopGold(shopLevel: Int): Boolean {
        val LEVEL_GOLD = 1
        val LEVEL_OFFICIAL_STORE = 2
        return shopLevel == LEVEL_GOLD || shopLevel == LEVEL_OFFICIAL_STORE
    }

    private fun isOfficialStore(shopLevel: Int): Boolean {
        val LEVEL_OFFICIAL_STORE = 2
        return shopLevel == LEVEL_OFFICIAL_STORE
    }
}
