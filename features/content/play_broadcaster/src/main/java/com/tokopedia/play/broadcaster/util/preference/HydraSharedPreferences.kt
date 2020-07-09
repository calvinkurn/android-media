package com.tokopedia.play.broadcaster.util.preference

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by jegul on 09/07/20
 */
class HydraSharedPreferences @Inject constructor(
        @ApplicationContext context: Context,
        private val userSession: UserSessionInterface
){

    private val mSharedPrefs = context.getSharedPreferences(
            HYDRA_PREFERENCE_NAME,
            Context.MODE_PRIVATE
    )

    fun isFirstStreaming(): Boolean {
        return mSharedPrefs.getBoolean(
                String.format(KEY_FIRST_STREAMING, userSession.userId, userSession.shopId),
                true
        )
    }

    fun setNotFirstStreaming() {
        mSharedPrefs.edit()
                .putBoolean(
                        String.format(KEY_FIRST_STREAMING, userSession.userId, userSession.shopId),
                        true
                ).apply()
    }

    companion object {

        private const val HYDRA_PREFERENCE_NAME = "hydra_preference"

        private const val KEY_FIRST_STREAMING = "first_streaming_%s_%s"
    }
}