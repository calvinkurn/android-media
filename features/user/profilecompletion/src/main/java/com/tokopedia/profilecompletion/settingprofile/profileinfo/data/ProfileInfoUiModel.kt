package com.tokopedia.profilecompletion.settingprofile.profileinfo.data

data class ProfileInfoUiModel(
    val profileInfoData: ProfileInfoData = ProfileInfoData(),
    val profileRoleData: ProfileRoleData = ProfileRoleData(),
    val profileFeedData: ProfileFeedData = ProfileFeedData()
) : ProfileInfoMainUI()
