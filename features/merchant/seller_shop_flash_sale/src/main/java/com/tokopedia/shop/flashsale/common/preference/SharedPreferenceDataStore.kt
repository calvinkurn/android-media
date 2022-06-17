package com.tokopedia.shop.flashsale.common.preference

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

class SharedPreferenceDataStore@Inject constructor(@ApplicationContext context: Context) {

    companion object {
        private const val PREFERENCE_FILE_NAME = "shop_flash_sale_prefs"
        private const val PREFERENCE_KEY_CAMPAIGN_INFO_TICKER_DISMISSED = "campaign_info_ticker_dismissed"
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
            putBoolean(PREFERENCE_KEY_CAMPAIGN_INFO_TICKER_DISMISSED, true)
            apply()
        }
    }

    fun isCampaignInfoCoachMarkDismissed(): Boolean {
        return preference.getBoolean(PREFERENCE_KEY_CAMPAIGN_INFO_TICKER_DISMISSED, false)
    }
}