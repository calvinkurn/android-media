package com.tokopedia.loginHelper.domain

sealed class LoginHelperDataSourceType {
    object REMOTE : LoginHelperDataSourceType()
    object LOCAL : LoginHelperDataSourceType()
}
