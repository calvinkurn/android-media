package com.tokopedia.shopdiscount.utils.preference

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

class SharedPreferenceDataStore @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        private const val PREFERENCE_FILE_NAME = "shop_discount_prefs"
        private const val PREFERENCE_KEY_TICKER_DISMISSED = "ticker_dismissed"
        private const val PREFERENCE_KEY_COACH_MARK_SUBSIDY_INFO_PARENT_LEVEL_SHOWN = "PREFERENCE_KEY_COACH_MARK_SUBSIDY_INFO_PARENT_LEVEL_SHOWN"
        private const val PREFERENCE_KEY_COACH_MARK_SUBSIDY_INFO_VARIANT_LEVEL_SHOWN = "PREFERENCE_KEY_COACH_MARK_SUBSIDY_INFO_VARIANT_LEVEL_SHOWN"
    }

    private val preference by lazy {
        context.getSharedPreferences(
            PREFERENCE_FILE_NAME,
            Context.MODE_PRIVATE
        )
    }

    fun markTickerAsDismissed() {
        val editor = preference.edit()
        with(editor) {
            putBoolean(PREFERENCE_KEY_TICKER_DISMISSED, true)
            apply()
        }
    }

    fun isTickerDismissed(): Boolean {
        return preference.getBoolean(PREFERENCE_KEY_TICKER_DISMISSED, false)
    }

    fun isCoachMarkSubsidyInfoOnParentAlreadyShown(): Boolean {
        return preference.getBoolean(PREFERENCE_KEY_COACH_MARK_SUBSIDY_INFO_PARENT_LEVEL_SHOWN, false)
    }

    fun setCoachMarkSubsidyInfoOnParentAlreadyShown() {
        val editor = preference.edit()
        with(editor) {
            putBoolean(PREFERENCE_KEY_COACH_MARK_SUBSIDY_INFO_PARENT_LEVEL_SHOWN, true)
            apply()
        }
    }

    fun isCoachMarkSubsidyInfoOnVariantAlreadyShown(): Boolean {
        return preference.getBoolean(PREFERENCE_KEY_COACH_MARK_SUBSIDY_INFO_VARIANT_LEVEL_SHOWN, false)
    }

    fun setCoachMarkSubsidyInfoOnVariantAlreadyShown() {
        val editor = preference.edit()
        with(editor) {
            putBoolean(PREFERENCE_KEY_COACH_MARK_SUBSIDY_INFO_VARIANT_LEVEL_SHOWN, true)
            apply()
        }
    }

}
