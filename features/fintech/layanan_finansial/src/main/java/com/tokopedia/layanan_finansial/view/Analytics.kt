package com.tokopedia.layanan_finansial.view

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

object Analytics {
    const val EVENT =""
    const val LAYANAN_FINANSIAL_CATEGORY = "layanan finansial page"
    const val LAYANAN_FINANSILA_VIEW_ACTION = "view layanan finansial product"
    const val LAYANAN_FINANSILA_click_ACTION = "click layanan finansial product"

    fun sendEvent(event: String, category: String, action: String, label: String){
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(event,category,action,label))
    }
}