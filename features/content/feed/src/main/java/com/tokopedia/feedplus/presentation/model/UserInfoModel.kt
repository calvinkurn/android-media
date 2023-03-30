package com.tokopedia.feedplus.presentation.model

import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by kenny.hadisaputra on 17/03/23
 */
data class UserInfoModel(
    val isLoggedIn: Boolean,
    val name: String,
) {
    companion object {
        fun from(userSession: UserSessionInterface): UserInfoModel {
            return UserInfoModel(
                isLoggedIn = userSession.isLoggedIn,
                name = userSession.name,
            )
        }
    }
}
