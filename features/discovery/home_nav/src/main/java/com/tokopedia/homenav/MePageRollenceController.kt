package com.tokopedia.homenav

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

/**
 * Created by frenzel on 09/05/22.
 */
object MePageRollenceController {

    const val ME_PAGE_OLD = ""
    private var rollenceMePageValue: String = ""

    fun fetchMePageRollenceValue() {
        rollenceMePageValue = try{
            RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.ME_PAGE_REVAMP)
        } catch (e: Exception){
            ME_PAGE_OLD
        }
    }

    private fun getRollenceValueMePage(): String {
        return if (rollenceMePageValue.isNotEmpty()) rollenceMePageValue else ME_PAGE_OLD
    }

    fun isMePageUsingRollenceVariant(): Boolean {
        return true
        return getRollenceValueMePage() == RollenceKey.ME_PAGE_REVAMP_VARIANT
    }
}