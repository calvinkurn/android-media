package com.tokopedia.loginHelper.data.repository

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.encryption.security.AESEncryptorCBC
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.loginHelper.data.api.LoginHelperApiService
import com.tokopedia.loginHelper.data.mapper.toEnvString
import com.tokopedia.loginHelper.data.mapper.toLocalUserHeaderUiModel
import com.tokopedia.loginHelper.data.mapper.toLoginUiModel
import com.tokopedia.loginHelper.data.mapper.toRemoteUserHeaderUiModel
import com.tokopedia.loginHelper.data.mapper.toUserDataUiModel
import com.tokopedia.loginHelper.data.response.LoginDataResponse
import com.tokopedia.loginHelper.data.response.UserDataResponse
import com.tokopedia.loginHelper.domain.LoginHelperEnvType
import com.tokopedia.loginHelper.domain.repository.LoginHelperRepository
import com.tokopedia.loginHelper.domain.uiModel.UnifiedLoginHelperData
import com.tokopedia.loginHelper.domain.uiModel.users.LocalUsersDataUiModel
import com.tokopedia.loginHelper.domain.uiModel.users.LoginDataUiModel
import com.tokopedia.loginHelper.domain.uiModel.users.UserDataUiModel
import com.tokopedia.loginHelper.util.CacheConstants
import com.tokopedia.usecase.coroutines.Success
import javax.crypto.SecretKey

class LoginHelperRepositoryImpl(
    private val api: LoginHelperApiService,
    private val aesEncryptorCBC: AESEncryptorCBC,
    private val secretKey: SecretKey
) : LoginHelperRepository {

    override suspend fun getUnifiedLoginData(envType: LoginHelperEnvType): UnifiedLoginHelperData {
        var persistantCachedUserData: LoginDataUiModel? = null
        val data = try {
            persistantCachedUserData = getLocalLoginData(envType)
            val decryptedRemoteUserDetails = getRemoteLoginData(envType)
            UnifiedLoginHelperData(persistantCachedUserData, decryptedRemoteUserDetails)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            UnifiedLoginHelperData(persistantCachedUserData, LoginDataUiModel())
        }

        val persistanceData = decryptUserData(data.persistentCacheUserData)
        val remoteData = decryptUserData(data.remoteUserData)

        return UnifiedLoginHelperData(
            LoginDataUiModel(
                data.persistentCacheUserData?.count,
                persistanceData
            ),
            LoginDataUiModel(
                data.remoteUserData?.count,
                remoteData
            )
        )
    }

    private fun decryptUserData(data: LoginDataUiModel?): MutableList<UserDataUiModel> {
        val list = mutableListOf<UserDataUiModel>()
        data?.users?.forEach {
            list.add(
                UserDataUiModel(
                    decrypt(it.email),
                    decrypt(it.password),
                    it.tribe,
                    it.id
                )
            )
        }
        return list
    }

    override suspend fun getRemoteLoginData(envType: LoginHelperEnvType): LoginDataUiModel? {
        return try {
            val response = api.getUserData(envType.toEnvString()).body()
            response?.toLoginUiModel()
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            LoginDataUiModel()
        }
    }

    // Data from Local cache
    override fun getLocalLoginData(envType: LoginHelperEnvType): LoginDataUiModel {
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

        return LoginDataUiModel(
            decryptedLocalUserDetails.size.toLocalUserHeaderUiModel(),
            decryptedLocalUserDetails.toUserDataUiModel()
        )
    }

    override fun getLoginDataFromAssets(loginData: LoginDataResponse): com.tokopedia.usecase.coroutines.Result<UnifiedLoginHelperData> {
        val decryptedUserDetails = mutableListOf<UserDataResponse>()
        loginData.users?.forEach {
            decryptedUserDetails.add(
                UserDataResponse(
                    decrypt(it.email.toBlankOrString()),
                    decrypt(it.password.toBlankOrString()),
                    it.tribe
                )
            )
        }
        val sortedUserList = decryptedUserDetails.sortedBy {
            it.email
        }

        val userList =
            LoginDataUiModel(
                sortedUserList.size.toLocalUserHeaderUiModel(),
                sortedUserList.toUserDataUiModel()
            )
        return Success(
            UnifiedLoginHelperData(
                persistentCacheUserData = userList,
                remoteUserData = null
            )
        )
    }

    private fun decrypt(text: String?): String {
        text?.let {
            return aesEncryptorCBC.decrypt(text, secretKey)
        }
        return ""
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

    override suspend fun getRemoteOnlyData(envType: LoginHelperEnvType): LoginDataUiModel {
        val response = getRemoteLoginData(envType)
        val decryptedResponse = decryptUserData(response)
        return LoginDataUiModel(
            decryptedResponse.size.toRemoteUserHeaderUiModel(),
            decryptedResponse
        )
    }
}
