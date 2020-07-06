package com.tokopedia.loginregister.registerinitial.domain.data

import com.tokopedia.sessioncommon.data.profile.ProfileInfo

data class ProfileInfoData(
        var isCreatePin: Boolean = false,
        var profileInfo: ProfileInfo = ProfileInfo()
)