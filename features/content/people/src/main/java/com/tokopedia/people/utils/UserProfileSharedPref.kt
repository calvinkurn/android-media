package com.tokopedia.people.utils

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 11, 2023
 */
class UserProfileSharedPref @Inject constructor(
    @ApplicationContext context: Context,
) {

    private val mSharedPrefs = context.getSharedPreferences(
        USER_PROFILE_PREFERENCE,
        Context.MODE_PRIVATE
    )

    fun hasBeenShown(key: Key, id: String = ""): Boolean {
        return mSharedPrefs.getBoolean(getSharedPrefKey(key, id), false)
    }

    fun setHasBeenShown(key: Key, id: String = "") {
        mSharedPrefs
            .edit()
            .putBoolean(getSharedPrefKey(key, id), true)
            .apply()
    }

    private fun getSharedPrefKey(key: Key, id: String): String {
        return if (id == "") key.sharedPrefKey else key.sharedPrefKey + "_" + id
    }

    companion object {
        private const val USER_PROFILE_PREFERENCE = "user_profile_preference"
    }

    enum class Key(val sharedPrefKey: String) {
        ReviewOnboarding("review_onboarding"),
        Unknown("")
    }
}
