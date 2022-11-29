package com.tokopedia.content.common.util.coachmark

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 03, 2022
 */
class ContentCoachMarkSharedPref @Inject constructor(
    @ApplicationContext context: Context,
) {

    private val mSharedPrefs = context.getSharedPreferences(
        CONTENT_COACHMARK_PREFERENCE,
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
        private const val CONTENT_COACHMARK_PREFERENCE = "content_coachmark_preference"
    }

    enum class Key(val sharedPrefKey: String) {
        PlayShortsEntryPoint("play_shorts_entry_point"),
        PlayShortsPreparation("play_shorts_preparation"),
        Unknown("")
    }
}
