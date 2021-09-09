package com.tokopedia.sellerhome.settings.analytics

import com.tokopedia.track.interfaces.Analytics

class NewOtherMenuTracker(private val analytics: Analytics) {

    companion object {
        const val OTHER_MENU_SHARE_BOTTOM_SHEET_PAGE_NAME = "Lainnya"
        const val OTHER_MENU_SHARE_BOTTOM_SHEET_FEATURE_NAME = "Share"
    }

}