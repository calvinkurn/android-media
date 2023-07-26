package com.tokopedia.content.common.navigation.people

import android.content.Intent

/**
 * Created By : Jonathan Darwin on July 26, 2023
 */
object UserProfileActivityResult {

    private const val EXTRA_IS_FOLLOW = "EXTRA_IS_FOLLOW"
    private const val EXTRA_USER_ID = "EXTRA_USER_ID"

    fun createResult(
        userId: String,
        isFollow: Boolean
    ): Intent {
        return Intent().apply {
            putExtra(EXTRA_IS_FOLLOW, isFollow)
            putExtra(EXTRA_USER_ID, userId)
        }
    }

    fun isFollow(intent: Intent): Boolean {
        return intent.getBooleanExtra(EXTRA_IS_FOLLOW, false)
    }

    fun getUserId(intent: Intent): String {
        return intent.getStringExtra(EXTRA_USER_ID).orEmpty()
    }
}
