package com.tokopedia.gm.common.utils

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 20/05/21
 */

class PMCommonRemoteConfig @Inject constructor(
        @ApplicationContext private val context: Context
) {

    companion object {
        private const val KEY_PM_SHOP_SCORE_ENABLED = "android_enable_pm_shop_score_interrupt"
    }

    fun getPmShopScoreInterruptEnabled(): Boolean {
        return FirebaseRemoteConfigImpl(context).getBoolean(KEY_PM_SHOP_SCORE_ENABLED, false)
    }
}