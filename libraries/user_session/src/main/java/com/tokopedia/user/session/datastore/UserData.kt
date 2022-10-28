package com.tokopedia.user.session.datastore

data class UserData(
    val name: String = "",
    val accessToken: String = "",
    val refreshToken: String = "",
    val email: String = "",
    val phoneNumber: String = ""
)