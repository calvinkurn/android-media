package com.tokopedia.play.broadcaster.util.preference

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.play.broadcaster.di.ActivityRetainedScope
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by jegul on 09/07/20
 */
@ActivityRetainedScope
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

    fun isFirstSwitchAccount(): Boolean {
        return mSharedPrefs.getBoolean(
            String.format(KEY_FIRST_SWITCH_ACCOUNT, userSession.userId),
            true
        )
    }

    fun setNotFirstSwitchAccount() {
        mSharedPrefs.edit()
            .putBoolean(
                String.format(KEY_FIRST_SWITCH_ACCOUNT, userSession.userId),
                false
            ).apply()
    }

    fun isShowSetupCoverCoachMark(): Boolean {
        return mSharedPrefs.getBoolean(
            String.format(KEY_SETUP_COVER_COACH_MARK, userSession.userId),
            true
        )
    }

    fun setShowSetupCoverCoachMark() {
        mSharedPrefs.edit()
            .putBoolean(
                String.format(KEY_SETUP_COVER_COACH_MARK, userSession.userId),
                false
            ).apply()
    }

    fun setLastSelectedAccount(selectedAccount: String) {
        mSharedPrefs.edit()
            .putString(
                String.format(KEY_LAST_SELECTED_ACCOUNT, userSession.userId),
                selectedAccount
            ).apply()
    }

    fun getLastSelectedAccount(): String {
        return mSharedPrefs.getString(
            String.format(KEY_LAST_SELECTED_ACCOUNT, userSession.userId),
            ""
        ) ?: ""
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

    fun isFirstGameResult():Boolean {
        return mSharedPrefs.getBoolean(
            String.format(KEY_FIRST_GAME_RESULT, userSession.shopId),
            true
        )
    }

    fun setNotFirstGameResult(){
        mSharedPrefs.edit()
            .putBoolean(
                String.format(KEY_FIRST_GAME_RESULT, userSession.shopId),
                false
            ).apply()
    }

    fun isFirstSelectQuizOption(): Boolean {
        return mSharedPrefs.getBoolean(
            String.format(KEY_FIRST_SELECT_QUIZ_OPTION, userSession.shopId),
            true
        )
    }

    fun setNotFirstSelectQuizOption() {
        mSharedPrefs.edit()
            .putBoolean(
                String.format(KEY_FIRST_SELECT_QUIZ_OPTION, userSession.shopId),
                false
            ).apply()
    }

    companion object {

        private const val HYDRA_PREFERENCE_NAME = "hydra_preference"

        private const val KEY_FIRST_STREAMING = "first_streaming_%s_%s"
        private const val KEY_FIRST_INTERACTIVE = "first_interactive_%s"
        private const val KEY_PERMISSION = "permission_%s"
        private const val KEY_FIRST_SELECT_QUIZ_OPTION = "first_select_quiz_option_%s"
        private const val KEY_FIRST_GAME_RESULT = "first_game_result_%s"
        private const val KEY_FIRST_SWITCH_ACCOUNT = "first_switch_account_%s"
        private const val KEY_SETUP_COVER_COACH_MARK = "setup_cover_coach_mark_%s"
        private const val KEY_LAST_SELECTED_ACCOUNT = "last_selected_account_%s"
    }
}
