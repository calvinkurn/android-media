package com.tokopedia.review.feature.inboxreview.util

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

class InboxReviewPreference @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        private const val INBOX_REVIEW_PREFERENCE = "inbox_review_preference"
        private const val FORMAT_KEJAR_ULASAN = "kejar_ulasan_%s"
    }

    private val sharedPrefs: SharedPreferences

    init {
        sharedPrefs = context.getSharedPreferences(
                INBOX_REVIEW_PREFERENCE,
                Context.MODE_PRIVATE
        )
    }

    fun isFirstTimeSeeKejarUlasan(userId: String?): Boolean {
        return sharedPrefs.getBoolean(String.format(FORMAT_KEJAR_ULASAN, userId), true)
    }

    fun setFirstTimeSeeKejarUlasan(userId: String?) {
        sharedPrefs.edit().putBoolean(String.format(FORMAT_KEJAR_ULASAN, userId), false).apply()
    }


}