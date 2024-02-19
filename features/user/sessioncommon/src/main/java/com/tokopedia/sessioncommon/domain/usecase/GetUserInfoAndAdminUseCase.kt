package com.tokopedia.sessioncommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.sessioncommon.data.admin.AdminDataResponse
import com.tokopedia.sessioncommon.data.admin.AdminResult
import com.tokopedia.sessioncommon.data.admin.AdminTypeResponse
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.util.GetProfileUtils
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class GetUserInfoAndAdminUseCase @Inject constructor(
    private val userInfoUseCase: GetUserInfoAndSaveSessionUseCase,
    private val getAdminTypeUseCase: GetAdminTypeUseCase,
    private val getProfileUtils: GetProfileUtils,
    private val userSession: UserSessionInterface,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Unit, AdminResult>(dispatchers.io) {

    private val resource = "kevin_user-loginregister"

    override fun graphqlQuery(): String {
        return ""
    }

    override suspend fun execute(params: Unit): AdminResult {
        val profileResponse = try {
            userInfoUseCase(Unit)
        } catch (e: Exception) {
            return AdminResult.AdminResultOnErrorGetProfile(e)
        }
        return when (profileResponse) {
            is Success -> {
                getAdmin(profileResponse.data)
            } is Fail -> {
                AdminResult.AdminResultOnErrorGetProfile(profileResponse.throwable)
            }
        }
    }

    private suspend fun getAdmin(profile: ProfilePojo): AdminResult {
        return try {
            val adminResponse = getAdminTypeUseCase(resource)
            onSuccessGetAdminType(adminResponse, profile)
        } catch (e: Exception) {
            AdminResult.AdminResultOnErrorGetAdmin(e)
        }
    }

    private fun onSuccessGetAdminType(
        adminResponse: AdminTypeResponse,
        profile: ProfilePojo
    ): AdminResult {
        val adminDataResponse = adminResponse.response
        val adminData = adminDataResponse.data
        val roleType = adminData.detail.roleType
        val isShopOwner = roleType.isShopOwner
        val isLocationAdmin = roleType.isLocationAdmin
        val isShopAdmin = roleType.isShopAdmin
        val isMultiLocationShop = adminDataResponse.isMultiLocationShop

        userSession.apply {
            setIsShopOwner(isShopOwner)
            setIsLocationAdmin(isLocationAdmin)
            setIsShopAdmin(isShopAdmin)
            setIsMultiLocationShop(isMultiLocationShop)
        }

        val shopId = profile.shopInfo.shopData.shopId
        val isAdminActive = adminDataResponse.data.isShopActive()

        // If shopId in profile is empty, set shopId from admin data response
        // for user other than location admin.
        // Also, if shop is inactive, set shopId to zero
        val shouldSetShopIdFromAdminData =
            (!isLocationAdmin && shopId.isEmpty()) || !adminDataResponse.data.isShopActive()
        val userProfile = if (shouldSetShopIdFromAdminData) {
            setShopIdFromAdminData(profile, adminDataResponse)
        } else {
            profile
        }

        val isAdminRedirection = adminDataResponse.data.isAdminInvitation()

        return if (GlobalConfig.isSellerApp() && isLocationAdmin && isAdminActive) {
            AdminResult.AdminResultShowLocationPopup
        } else if (GlobalConfig.isSellerApp() && isLocationAdmin && isAdminRedirection) {
            getProfileUtils.saveProfileData(userProfile)
            AdminResult.AdminResultOnLocationAdminRedirection
        } else {
            getProfileUtils.saveProfileData(userProfile)
            AdminResult.AdminResultOnSuccessGetProfile(userProfile)
        }
    }

    private fun setShopIdFromAdminData(profile: ProfilePojo, adminData: AdminDataResponse): ProfilePojo {
        val isShopActive = adminData.data.isShopActive()
        val shopId =
            if (isShopActive) {
                adminData.shopId
            } else {
                ""
            }
        val shopInfo = profile.shopInfo
        val shopData = shopInfo.shopData.copy(shopId = shopId)
        val shopBasicData = shopInfo.copy(shopData = shopData)
        return profile.copy(shopInfo = shopBasicData)
    }
}
