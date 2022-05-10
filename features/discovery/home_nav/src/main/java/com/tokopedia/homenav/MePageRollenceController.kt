package com.tokopedia.homenav

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

/**
 * Created by frenzel on 09/05/22.
 */
object MePageRollenceController {

    private var rollenceMePageValue: String? = null

    fun fetchMePageRollenceValue() {
        rollenceMePageValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.ME_PAGE_REVAMP)
    }

    fun isMePageUsingRollenceVariant(): Boolean {
        if(rollenceMePageValue == null){
            fetchMePageRollenceValue()
        }
        return rollenceMePageValue == RollenceKey.ME_PAGE_REVAMP_VARIANT
    }
}