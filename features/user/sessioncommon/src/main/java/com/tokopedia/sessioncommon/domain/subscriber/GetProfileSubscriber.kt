package com.tokopedia.sessioncommon.domain.subscriber

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.domain.usecase.GetAdminInfoUseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber

/**
 * @author by nisie on 11/06/19.
 */
class GetProfileSubscriber(val userSession: UserSessionInterface,
                           val onSuccessGetProfile: (pojo: ProfilePojo) -> Unit,
                           val onErrorGetProfile: (e: Throwable) -> Unit,
                           val getAdminInfoUseCase: GetAdminInfoUseCase? = null,
                           val showLocationAdminPopUp: (() -> Unit)? = null,
                           val showLocationAdminError: ((e: Throwable) -> Unit)? = null,
                           val onFinished: () -> Unit? = {}) :
        Subscriber<GraphqlResponse>() {

    override fun onNext(response: GraphqlResponse) {
        if(GlobalConfig.isSellerApp()) {
            getLocationAdmin(response)
        } else {
            onSuccessGetUserProfile(response)
        }
    }

    private fun onSuccessGetUserProfile(response: GraphqlResponse) {
        val pojo = response.getData<ProfilePojo>(ProfilePojo::class.java)
        val errors = response.getError(ProfilePojo::class.java)
        if (pojo.profileInfo.userId.isNotBlank()
                && pojo.profileInfo.userId!= "0") {
            saveProfileData(pojo)
            onSuccessGetProfile(pojo)
        } else if (errors.isNotEmpty()){
            onErrorGetProfile(MessageErrorException(errors[0].message))
        } else {
            onErrorGetProfile(Throwable())
        }
        onFinished.invoke()
    }

    private fun getLocationAdmin(response: GraphqlResponse) {
        val error = response.getError(ProfilePojo::class.java)

        when {
            error.isNullOrEmpty() -> {
                val profile = response.getData<ProfilePojo>(ProfilePojo::class.java)
                val shopId = profile.shopInfo.shopData.shopId.toIntOrNull() ?: 0

                if(getAdminInfoUseCase != null) {
                    getAdminInfoUseCase.execute(shopId, GetAdminInfoSubscriber(
                        userSession,
                        { onSuccessGetUserProfile(response) },
                        showLocationAdminPopUp,
                        showLocationAdminError
                    ))
                } else {
                    onSuccessGetUserProfile(response)
                }
            }
            error.isNotEmpty() -> {
                val message = error.firstOrNull()?.message.orEmpty()
                onErrorGetProfile(MessageErrorException(message))
            }
            else -> onErrorGetProfile(Throwable())
        }
    }

    private fun saveProfileData(pojo: ProfilePojo?) {
        pojo?.profileInfo?.run {
            userSession.setHasPassword(pojo.profileInfo.isCreatedPassword)
            userSession.profilePicture = pojo.profileInfo.profilePicture
            userSession.setIsMSISDNVerified(pojo.profileInfo.isPhoneVerified)

            userSession.setLoginSession(true,
                    pojo.profileInfo.userId,
                    pojo.profileInfo.fullName,
                    pojo.shopInfo.shopData.shopId,
                    pojo.profileInfo.isPhoneVerified,
                    pojo.shopInfo.shopData.shopName,
                    pojo.profileInfo.email,
                    isShopGold(pojo.shopInfo.shopData.shopLevel),
                    pojo.profileInfo.phone)
            userSession.setIsShopOfficialStore(isOfficialStore(pojo.shopInfo.shopData.shopLevel))
            userSession.shopAvatar = pojo.shopInfo.shopData.shopAvatar
        }
    }

    private fun isShopGold(shopLevel: Int): Boolean {
        val LEVEL_GOLD = 1
        val LEVEL_OFFICIAL_STORE = 2
        return shopLevel == LEVEL_GOLD  ||  shopLevel == LEVEL_OFFICIAL_STORE
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