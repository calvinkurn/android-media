package com.tokopedia.sessioncommon.data.admin

import com.tokopedia.sessioncommon.data.profile.ProfilePojo

sealed class AdminResult {
    data class AdminResultOnErrorGetAdmin(val error: Throwable) : AdminResult()
    data class AdminResultOnErrorGetProfile(val error: Throwable) : AdminResult()
    object AdminResultShowLocationPopup : AdminResult()
    object AdminResultOnLocationAdminRedirection : AdminResult()
    data class AdminResultOnSuccessGetProfile(val profile: ProfilePojo) : AdminResult()
}
