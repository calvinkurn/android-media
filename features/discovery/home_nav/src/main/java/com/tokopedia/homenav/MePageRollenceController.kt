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
            RollenceKey.ME_PAGE_CONTROL
        }
    }

    private fun getMePageRollenceValue(): String {
        return rollenceMePageValue.ifEmpty { RollenceKey.ME_PAGE_CONTROL }
    }

    fun isUsingMePageRollenceVariant(): Boolean {
        return getMePageRollenceValue() == RollenceKey.ME_PAGE_VARIANT_1 ||
            getMePageRollenceValue() == RollenceKey.ME_PAGE_VARIANT_2
    }

    fun isUsingMePageRollenceVariant1(): Boolean {
        return getMePageRollenceValue() == RollenceKey.ME_PAGE_VARIANT_1
    }

    fun isUsingMePageRollenceVariant2(): Boolean {
        return getMePageRollenceValue() == RollenceKey.ME_PAGE_VARIANT_2
    }
}
