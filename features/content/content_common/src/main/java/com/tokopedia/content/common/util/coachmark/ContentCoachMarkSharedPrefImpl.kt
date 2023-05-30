package com.tokopedia.content.common.util.coachmark

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on April 12, 2023
 */
class ContentCoachMarkSharedPrefImpl @Inject constructor(
    @ApplicationContext context: Context,
) : ContentCoachMarkSharedPref {

    private val mSharedPrefs = context.getSharedPreferences(
        CONTENT_COACHMARK_PREFERENCE,
        Context.MODE_PRIVATE
    )

    override fun hasBeenShown(key: ContentCoachMarkSharedPref.Key, id: String): Boolean {
        return mSharedPrefs.getBoolean(getSharedPrefKey(key, id), false)
    }

    override fun setHasBeenShown(key: ContentCoachMarkSharedPref.Key, id: String) {
        mSharedPrefs
            .edit()
            .putBoolean(getSharedPrefKey(key, id), true)
            .apply()
    }

    private fun getSharedPrefKey(key: ContentCoachMarkSharedPref.Key, id: String): String {
        return if (id == "") key.sharedPrefKey else key.sharedPrefKey + "_" + id
    }

    companion object {
        private const val CONTENT_COACHMARK_PREFERENCE = "content_coachmark_preference"
    }
}
