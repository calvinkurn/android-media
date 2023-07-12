package com.tokopedia.content.common.util.coachmark

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 03, 2022
 */
interface ContentCoachMarkSharedPref {

    fun hasBeenShown(key: Key, id: String = ""): Boolean

    fun setHasBeenShown(key: Key, id: String = "")

    enum class Key(val sharedPrefKey: String) {
        SwitchAccount("switch_account"),
        PerformanceDashboardEntryPointBanner("performance_dashboard_entry_point_banner"),
        PlayShortsEntryPoint("play_shorts_entry_point"),
        PlayShortsPreparation("play_shorts_preparation"),
        ProductCommission("product_commission"),
        ProductSummaryCommission("product_summary_commission"),
        PlayBroadcasterFaceFilter("play_broadcaster_face_filter"),
        Unknown("")
    }
}
