package com.tokopedia.homenav

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

/**
 * Created by frenzel on 09/05/22.
 */
object MePageRollenceController {
    private var rollenceMePageValue: String = ""

    fun fetchMePageRollenceValue() {
        rollenceMePageValue = try {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.ME_PAGE_EXP)
        } catch (e: Exception) {
            ""
        }
    }

    fun isUsingMePageRollenceVariant(): Boolean {
        return rollenceMePageValue == RollenceKey.ME_PAGE_VARIANT
    }
}
