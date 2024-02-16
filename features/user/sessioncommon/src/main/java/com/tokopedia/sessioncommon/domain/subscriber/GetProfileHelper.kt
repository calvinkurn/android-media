package com.tokopedia.sessioncommon.domain.subscriber

import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.admin.AdminDataResponse
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.domain.usecase.GetAdminTypeUseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber

/**
 * @author by nisie on 11/06/19.
 */
class GetProfileHelper(
    val userSession: UserSessionInterface,
    val onSuccessGetProfile: (pojo: ProfilePojo) -> Unit,
    val onErrorGetProfile: (e: Throwable) -> Unit,
    val getAdminTypeUseCase: GetAdminTypeUseCase? = null,
    val showLocationAdminPopUp: (() -> Unit)? = null,
    val onLocationAdminRedirection: (() -> Unit)? = null,
    val showErrorGetAdminType: ((e: Throwable) -> Unit)? = null,
    val onFinished: () -> Unit? = {}
) :
    Subscriber<GraphqlResponse>() {

    companion object {
        private const val GET_ADMIN_TYPE_SOURCE = "kevin_user-loginregister"
    }

    override fun onNext(response: GraphqlResponse) {
        onSuccessGetUserProfile(response)
    }

    private fun onSuccessGetUserProfile(response: GraphqlResponse) {
        val profile = response.getData<ProfilePojo>(ProfilePojo::class.java)
        val errors = response.getError(ProfilePojo::class.java)
        val isProfileValid = profile.profileInfo.userId.isNotBlank() &&
            profile.profileInfo.userId != "0"
        val shouldGetAdminType = getAdminTypeUseCase != null

        when {
            isProfileValid && shouldGetAdminType -> {
                getAdminType(profile)
            }
            isProfileValid -> {
                saveProfileData(profile)
                onSuccessGetProfile(profile)
            }
            errors.isNotEmpty() -> {
                val message = errors.firstOrNull()?.message.orEmpty()
                val exception = MessageErrorException(message)
                onErrorGetProfile(exception)
            }
            else -> onErrorGetProfile(Throwable())
        }

        onFinished.invoke()
    }

//    private fun getAdminType(profile: ProfilePojo) {
//        getAdminTypeUseCase?.execute(GetAdminTypeSubscriber(
//                userSession,
//                onSuccessGetAdminType(profile),
//                onErrorGetAdminType()
//        ), GET_ADMIN_TYPE_SOURCE)
//    }

    private fun onSuccessGetAdminType(profile: ProfilePojo): (AdminDataResponse) -> Unit {
        return {
            val shopId = profile.shopInfo.shopData.shopId
            val isLocationAdmin = it.data.detail.roleType.isLocationAdmin
            val isAdminActive = it.data.isShopActive()

            // If shopId in profile is empty, set shopId from admin data response
            // for user other than location admin.
            // Also, if shop is inactive, set shopId to zero
            val shouldSetShopIdFromAdminData =
                (!isLocationAdmin && shopId.isEmpty()) || !it.data.isShopActive()
            val userProfile = if (shouldSetShopIdFromAdminData) {
//                setShopIdFromAdminData(profile, it)
            } else {
                profile
            }

            val isAdminRedirection = it.data.isAdminInvitation()

            if (GlobalConfig.isSellerApp() && isLocationAdmin && isAdminActive) {
                showLocationAdminPopUp?.invoke()
            } else if (GlobalConfig.isSellerApp() && isLocationAdmin && isAdminRedirection) {
                saveProfileData(userProfile)
                onLocationAdminRedirection?.invoke()
            } else {
                saveProfileData(userProfile)
                onSuccessGetProfile(userProfile)
            }
        }
    }

    private fun onErrorGetAdminType(): (Throwable) -> Unit = {
        showErrorGetAdminType?.invoke(it)
    }

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

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        e?.run {
            onErrorGetProfile(this)
        }
        onFinished.invoke()
    }
}
