package com.tokopedia.createpost.view.util

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 09/02/21
 */

class FeedSellerAppReviewHelper @Inject constructor(
        @ApplicationContext private val context: Context,
        private val userSession: UserSessionInterface
) {

    companion object {
        private const val PREFERENCE_NAME = "CACHE_SELLER_IN_APP_REVIEW"
        private const val KEY_HAS_POSTED_FEED = "KEY_SIR_HAS_POSTED_FEED"
    }

    fun savePostFeedFlag() {
        val sharedPref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(KEY_HAS_POSTED_FEED + userSession.userId, true)
        editor.apply()
    }
}