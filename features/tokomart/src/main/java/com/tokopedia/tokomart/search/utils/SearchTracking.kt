package com.tokopedia.tokomart.search.utils

import com.tokopedia.track.TrackApp

object SearchTracking {

    object Action {
        const val GENERAL_SEARCH = "general search"
    }

    object Category {
        const val TOKONOW_TOP_NAV = "tokonow - top nav"
    }

    object Misc {
        const val HASIL_PENCARIAN_DI_TOKONOW = "Hasil pencarian di TokoNOW!"
    }

    fun sendGeneralEvent(dataLayer: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(dataLayer)
    }
}