package com.tokopedia.review.feature.inbox.common.presentation

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform

object InboxUnifiedRemoteConfig {
    fun isInboxUnified(): Boolean  {
        val useNewInbox = RemoteConfigInstance.getInstance().abTestPlatform.getString(
                AbTestPlatform.KEY_AB_INBOX_REVAMP, AbTestPlatform.VARIANT_OLD_INBOX
        ) == AbTestPlatform.VARIANT_NEW_INBOX
        val useNewNav = RemoteConfigInstance.getInstance().abTestPlatform.getString(
                AbTestPlatform.NAVIGATION_EXP_TOP_NAV, AbTestPlatform.NAVIGATION_VARIANT_OLD
        ) == AbTestPlatform.NAVIGATION_VARIANT_REVAMP
        return useNewInbox && useNewNav
    }
}