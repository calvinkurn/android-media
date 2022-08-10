package com.tokopedia.shop.flashsale.common.preference

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

class SharedPreferenceDataStore@Inject constructor(@ApplicationContext context: Context) {

    companion object {
        private const val PREFERENCE_FILE_NAME = "shop_flash_sale_prefs"
        private const val PREFERENCE_KEY_MANAGE_PRODUCT_COACHMARK_DISMISSED = "manage_product_coachmark_dismissed"
        private const val PREFERENCE_KEY_EDIT_PRODUCT_COACHMARK_DISMISSED = "edit_product_coachmark_dismissed"
        private const val PREFERENCE_KEY_CAMPAIGN_INFO_COACH_MARK_DISMISSED = "campaign_info_coachmark_dismiss"
        private const val PREFERENCE_KEY_CAMPAIGN_PRODUCT_HIGHLIGHT_COACH_MARK_DISMISSED = "manage_product_highlight_coach_mark_dismissed"
    }

    private val preference by lazy {
        context.getSharedPreferences(
            PREFERENCE_FILE_NAME,
            Context.MODE_PRIVATE
        )
    }

    fun markCampaignInfoCoachMarkComplete() {
        val editor = preference.edit()
        with(editor) {
            putBoolean(PREFERENCE_KEY_CAMPAIGN_INFO_COACH_MARK_DISMISSED, true)
            apply()
        }
    }

    fun isCampaignInfoCoachMarkDismissed(): Boolean {
        return preference.getBoolean(PREFERENCE_KEY_CAMPAIGN_INFO_COACH_MARK_DISMISSED, false)
    }

    fun markHighlightCampaignProductComplete() {
        val editor = preference.edit()
        with(editor) {
            putBoolean(PREFERENCE_KEY_CAMPAIGN_PRODUCT_HIGHLIGHT_COACH_MARK_DISMISSED, true)
            apply()
        }
    }

    fun isHighlightCampaignProductDismissed(): Boolean {
        return preference.getBoolean(PREFERENCE_KEY_CAMPAIGN_PRODUCT_HIGHLIGHT_COACH_MARK_DISMISSED, false)
    }

    fun markManageProductCoachMarkComplete() {
        val editor = preference.edit()
        with(editor) {
            putBoolean(PREFERENCE_KEY_MANAGE_PRODUCT_COACHMARK_DISMISSED, true)
            apply()
        }
    }

    fun isManageProductCoachMarkDismissed(): Boolean {
        return preference.getBoolean(PREFERENCE_KEY_MANAGE_PRODUCT_COACHMARK_DISMISSED, false)
    }

    fun markEditProductCoachMarkComplete() {
        val editor = preference.edit()
        with(editor) {
            putBoolean(PREFERENCE_KEY_EDIT_PRODUCT_COACHMARK_DISMISSED, true)
            apply()
        }
    }

    fun isEditProductCoachMarkDismissed(): Boolean {
        return preference.getBoolean(PREFERENCE_KEY_EDIT_PRODUCT_COACHMARK_DISMISSED, false)
    }
}