package com.tokopedia.loginHelper.domain.usecase

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.loginHelper.data.api.LoginHelperApiService
import com.tokopedia.loginHelper.data.mapper.toEnvString
import com.tokopedia.loginHelper.data.mapper.toLocalUserHeaderUiModel
import com.tokopedia.loginHelper.data.mapper.toLoginUiModel
import com.tokopedia.loginHelper.data.mapper.toUserDataUiModel
import com.tokopedia.loginHelper.data.response.UserDataResponse
import com.tokopedia.loginHelper.domain.LoginHelperEnvType
import com.tokopedia.loginHelper.domain.uiModel.UnifiedLoginHelperData
import com.tokopedia.loginHelper.domain.uiModel.users.LocalUsersDataUiModel
import com.tokopedia.loginHelper.domain.uiModel.users.LoginDataUiModel
import com.tokopedia.loginHelper.util.CacheConstants
import javax.inject.Inject

class GetUserDetailsRestUseCase @Inject constructor(private val api: LoginHelperApiService) {

    suspend fun makeNetworkCall(envType: LoginHelperEnvType): UnifiedLoginHelperData {
        var persistantCachedUserData: LoginDataUiModel? = null
        return try {
            val cacheManager = PersistentCacheManager.instance
            val savedData = getLocalData(cacheManager, envType)

            val decryptedLocalUserDetails = mutableListOf<UserDataResponse>()

            savedData?.userDataUiModel?.forEach {
                decryptedLocalUserDetails.add(
                    UserDataResponse(
                        it.email.toBlankOrString(),
                        it.password.toBlankOrString()
                    )
                )
            }

            persistantCachedUserData =
                LoginDataUiModel(
                    decryptedLocalUserDetails.size.toLocalUserHeaderUiModel(),
                    decryptedLocalUserDetails.toUserDataUiModel()
                )

            val response = api.getUserData(envType.toEnvString()).body()
            val decryptedRemoteUserDetails = response?.toLoginUiModel()
            UnifiedLoginHelperData(persistantCachedUserData, decryptedRemoteUserDetails)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            UnifiedLoginHelperData(persistantCachedUserData, LoginDataUiModel())
        }
    }

    private fun getLocalData(cacheManager: PersistentCacheManager, envType: LoginHelperEnvType): LocalUsersDataUiModel? {
        return if (envType == LoginHelperEnvType.STAGING) {
            cacheManager.get(
                CacheConstants.LOGIN_HELPER_LOCAL_USER_DATA_STAGING,
                LocalUsersDataUiModel::class.java,
                LocalUsersDataUiModel()
            )
        } else {
            cacheManager.get(
                CacheConstants.LOGIN_HELPER_LOCAL_USER_DATA_PROD,
                LocalUsersDataUiModel::class.java,
                LocalUsersDataUiModel()
            )
        }
    }
}
