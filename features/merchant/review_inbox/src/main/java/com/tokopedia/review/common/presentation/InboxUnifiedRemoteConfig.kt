package com.tokopedia.review.common.presentation

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

object InboxUnifiedRemoteConfig {
    fun isInboxUnified(): Boolean  {
        val useNewInbox = RemoteConfigInstance.getInstance().abTestPlatform.getString(
                RollenceKey.KEY_AB_INBOX_REVAMP, RollenceKey.VARIANT_OLD_INBOX
        ) == RollenceKey.VARIANT_NEW_INBOX
        val useNewNav = RemoteConfigInstance.getInstance().abTestPlatform.getString(
            RollenceKey.NAVIGATION_EXP_TOP_NAV, RollenceKey.NAVIGATION_VARIANT_OLD
        ) == RollenceKey.NAVIGATION_VARIANT_REVAMP || RemoteConfigInstance.getInstance().abTestPlatform.getString(
            RollenceKey.NAVIGATION_EXP_TOP_NAV2, RollenceKey.NAVIGATION_VARIANT_OLD
        ) == RollenceKey.NAVIGATION_VARIANT_REVAMP2
        return useNewInbox && useNewNav
    }
}