package com.tokopedia.homenav.mainnav.view.datamodel.account

data class ProfileDataModel(
    var userName: String = "",
    var userImage: String = "",

    /**
     * Status
     */
    var isProfileLoading: Boolean = false,
    var isGetUserNameError: Boolean = false
)
