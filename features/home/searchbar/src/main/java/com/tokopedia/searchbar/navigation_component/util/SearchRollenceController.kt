package com.tokopedia.searchbar.navigation_component.util

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

/**
 * Created by @ilhamsuaib on 5/24/24.
 */

object SearchRollenceController {

    private var inboxNotifValue = String.EMPTY

    fun fetchInboxNotifTopNavValue() {
        inboxNotifValue = try {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(
                RollenceKey.HOME_COMBINE_INBOX_NOTIF_KEY, String.EMPTY
            )
        } catch (e: Exception) {
            String.EMPTY
        }
    }

    fun shouldCombineInboxNotif(): Boolean {
        return inboxNotifValue == RollenceKey.HOME_COMBINE_INBOX_NOTIF
    }
}
