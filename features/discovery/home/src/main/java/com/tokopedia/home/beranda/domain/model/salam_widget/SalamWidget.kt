package com.tokopedia.home.beranda.domain.model.salam_widget

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by firman on 09-06-2020
 */

data class SalamWidget(
        @Expose
        @SerializedName("salamWidget")
        val salamWidget : SalamWidgetData = SalamWidgetData()
)