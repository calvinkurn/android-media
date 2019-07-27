package com.tokopedia.sessioncommon.domain.subscriber

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber

/**
 * @author by nisie on 11/06/19.
 */
class GetProfileSubscriber(val userSession: UserSessionInterface,
                           val onSuccessGetProfile: (pojo: ProfilePojo) -> Unit,
                           val onErrorGetProfile: (e: Throwable) -> Unit,
                           val onGoToCreatePassword: (fullName : String, userId : String) -> Unit,
                           private val canGoToCreatePassword : Boolean = true) :
        Subscriber<GraphqlResponse>() {

    override fun onNext(response: GraphqlResponse) {

        val pojo = response.getData<ProfilePojo>(ProfilePojo::class.java)
        val errors = response.getError(ProfilePojo::class.java)
        if (pojo.profileInfo.userId.isNotBlank()
                && pojo.profileInfo.userId!= "0") {
            saveProfileData(pojo)
            when{
                shouldGoToCreatePassword(pojo, canGoToCreatePassword) -> onGoToCreatePassword(pojo.profileInfo.fullName, pojo.profileInfo.userId)
                else -> onSuccessGetProfile(pojo)
            }
        } else if (errors.isNotEmpty()){
            onErrorGetProfile(MessageErrorException(errors[0].message))
        } else {
            onErrorGetProfile(Throwable())
        }
    }

    private fun shouldGoToCreatePassword(pojo: ProfilePojo?, canGoToCreatePassword: Boolean): Boolean {
        pojo?.run{
           return canGoToCreatePassword && !profileInfo.isCreatedPassword
        }
        return false
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
            userSession.shopAvatar = pojo.shopInfo.shopData.shopAvatar
        }
    }

    private fun isShopGold(shopLevel: Int): Boolean {
        val LEVEL_GOLD = 1
        return shopLevel == LEVEL_GOLD
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {
        e?.run {
            onErrorGetProfile(this)
        }
    }


}