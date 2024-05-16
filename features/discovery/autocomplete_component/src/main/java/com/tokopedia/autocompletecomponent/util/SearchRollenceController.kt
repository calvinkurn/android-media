package com.tokopedia.autocompletecomponent.util

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

/**
 * Created by @ilhamsuaib on 5/16/24.
 */

object SearchRollenceController {

    private var searchBtnRollence = String.EMPTY

    fun fetchRollenceData() {
        fetchInboxNotifTopNavValue()
    }

    fun isSearchBtnEnabled(): Boolean {
        return searchBtnRollence == RollenceKey.HOME_COMBINE_INBOX_NOTIF
    }

    private fun fetchInboxNotifTopNavValue() {
        searchBtnRollence = try {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(
                RollenceKey.HOME_COMBINE_INBOX_NOTIF_KEY, String.EMPTY
            )
        } catch (e: Exception) {
            String.EMPTY
        }
    }
}
