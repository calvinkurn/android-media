package com.tokopedia.sessioncommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class GetUserInfoAndSaveSessionUseCase @Inject constructor(
    @ApplicationContext val repository: GraphqlRepository,
    private val userSession: UserSessionInterface,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Unit, Result<ProfilePojo>>(dispatchers.io){

    override fun graphqlQuery(): String = """
        query profile() {
          profile(skipCache: true) {
            user_id
            full_name
            first_name
            email
            bday
            gender
            phone
            phone_masked
            phone_verified
            profile_picture
            created_password
            isLoggedIn
          }
          shopBasicData(){
            result{
              shopID
              name
              domain
              level
              logo
              avatarOriginal
            }
          }
        }
        """.trimIndent()

    override suspend fun execute(params: Unit): Result<ProfilePojo> {
        val response: ProfilePojo = repository.request(graphqlQuery(), params)

        return onSuccessGetInfo(response)
    }

    private fun onSuccessGetInfo(profilePojo: ProfilePojo): Result<ProfilePojo> {
        val profile = profilePojo.profileInfo
        val isProfileValid = profile.userId.isNotBlank() && profile.userId != "0"

        return if (isProfileValid) {
            saveProfileData(profilePojo)
            Success(profilePojo)
        } else {
            Fail(Throwable())
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
            userSession.shopAvatarOriginal = shopInfo.shopData.shopAvatarOriginal
        }
    }

    private fun isShopGold(shopLevel: Int): Boolean {
        return shopLevel == LEVEL_GOLD || shopLevel == LEVEL_OFFICIAL_STORE
    }

    private fun isOfficialStore(shopLevel: Int): Boolean {
        return shopLevel == LEVEL_OFFICIAL_STORE
    }

    companion object {
        private const val LEVEL_GOLD = 1
        private const val LEVEL_OFFICIAL_STORE = 2
    }
}
