package com.tokopedia.people.utils.remoteconfig

import com.tokopedia.people.di.UserProfileScope
import com.tokopedia.remoteconfig.RemoteConfig
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on July 11, 2023
 */
@UserProfileScope
class UserProfileRemoteConfig @Inject constructor(
    private val remoteConfig: RemoteConfig,
) {

    fun isEnableReviewTab(): Boolean {
        return remoteConfig.getBoolean(USER_PROFILE_ENABLE_REVIEW_TAB, true)
    }

    companion object {
        private const val USER_PROFILE_ENABLE_REVIEW_TAB = "android_enable_user_profile_review_tab"
    }
}
