package com.tokopedia.loginHelper.domain.pojo

data class LoginHelperAddUserPojo(
    val email: String,
    val password: String,
    val tribe: String? = null 
)
