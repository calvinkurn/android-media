package com.tokopedia.imagepicker_insta.usecase

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.imagepicker_insta.R

/**
 * Created by meyta.taliti on 17/04/23.
 */
class FeedVideoDepreciationUseCase(
    private val mContext: Context
) {
    private val mPreferences: SharedPreferences by lazy {
        mContext.getSharedPreferences(
            mContext.getString(R.string.pref_feed_new_video_uploader),
            Context.MODE_PRIVATE
        )
    }

    private val keyPref = mContext.getString(R.string.pref_key_feed_new_video_uploader)

    fun isFirstTimeVisit(): Boolean {
        return mPreferences.getBoolean(
            keyPref,
            true
        )
    }

    fun setFirstTimeVisit() {
        mPreferences.edit()
            .putBoolean(keyPref, false)
            .apply()
    }
}
