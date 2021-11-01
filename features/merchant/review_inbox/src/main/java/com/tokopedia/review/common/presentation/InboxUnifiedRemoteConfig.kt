package com.tokopedia.review.common.presentation

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

object InboxUnifiedRemoteConfig {
    fun isInboxUnified(): Boolean  {
        val useNewInbox = RemoteConfigInstance.getInstance().abTestPlatform.getString(
                RollenceKey.KEY_AB_INBOX_REVAMP, RollenceKey.VARIANT_OLD_INBOX
        ) == RollenceKey.VARIANT_NEW_INBOX
        val useNewNav = true
        return useNewInbox && useNewNav
    }
}