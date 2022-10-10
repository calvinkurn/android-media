package com.tokopedia.utils

import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.datastore.UserSessionDataStore
import kotlinx.coroutines.flow.first

data class SampleUserModel(
    val isLogin: Boolean,
    val userId: String,
    val name: String,
    val token: String,
    val refreshToken: String,
    val isShopOwner: Boolean = false
)

internal fun UserSession.setSample(model: SampleUserModel) {
    setIsLogin(model.isLogin)
    userId = model.userId
    name = model.name
    setToken(model.token, "Bearer", model.refreshToken)
    setIsShopOwner(model.isShopOwner)
}

internal suspend fun UserSessionDataStore.getSampleUser(): SampleUserModel {
    return SampleUserModel(
        isLoggedIn().first(),
        getUserId().first(),
        getName().first(),
        getAccessToken().first(),
        getRefreshToken().first(),
        isShopOwner().first()
    )
}