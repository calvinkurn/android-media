package com.tokopedia.play.broadcaster.util.preference

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.play.broadcaster.di.broadcast.PlayBroadcastScope
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by jegul on 09/07/20
 */
@PlayBroadcastScope
class HydraSharedPreferences @Inject constructor(
        @ApplicationContext context: Context,
        private val userSession: UserSessionInterface
) : PermissionSharedPreferences {

    private val mSharedPrefs = context.getSharedPreferences(
            HYDRA_PREFERENCE_NAME,
            Context.MODE_PRIVATE
    )

    override fun hasBeenAsked(permission: String): Boolean {
        return mSharedPrefs.getBoolean(
                String.format(KEY_PERMISSION, permission),
                false
        )
    }

    override fun setHasBeenAsked(permission: String) {
        mSharedPrefs.edit()
                .putBoolean(
                        String.format(KEY_PERMISSION, permission),
                        true
                ).apply()
    }

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
                        false
                ).apply()
    }

    fun isFirstInteractive(): Boolean {
        return mSharedPrefs.getBoolean(
                String.format(KEY_FIRST_INTERACTIVE, userSession.shopId),
                true
        )
    }

    fun setNotFirstInteractive() {
        mSharedPrefs.edit()
                .putBoolean(
                        String.format(KEY_FIRST_INTERACTIVE, userSession.shopId),
                        false
                ).apply()
    }

    companion object {

        private const val HYDRA_PREFERENCE_NAME = "hydra_preference"

        private const val KEY_FIRST_STREAMING = "first_streaming_%s_%s"
        private const val KEY_FIRST_INTERACTIVE = "first_interactive_%s"
        private const val KEY_PERMISSION = "permission_%s"
    }
}