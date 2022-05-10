package com.tokopedia.homenav

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

/**
 * Created by frenzel on 09/05/22.
 */
object MePageRollenceController {

    private var rollenceMePageValue: String = ""

    fun fetchMePageRollenceValue() {
        rollenceMePageValue = try{
            RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.ME_PAGE_REVAMP)
        } catch (e: Exception){
            ""
        }
    }

    private fun getRollenceValueMePage(): String {
        return if (rollenceMePageValue.isNotEmpty()) rollenceMePageValue else RollenceKey.ME_PAGE_OLD
    }

    fun isMePageUsingRollenceVariant(): Boolean {
        return getRollenceValueMePage() == RollenceKey.ME_PAGE_REVAMP_VARIANT
    }
}