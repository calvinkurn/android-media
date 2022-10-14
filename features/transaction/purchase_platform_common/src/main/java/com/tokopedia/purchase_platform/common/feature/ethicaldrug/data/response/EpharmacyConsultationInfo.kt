package com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.response

import com.google.gson.annotations.SerializedName

class EpharmacyConsultationInfo(
    @SerializedName("ticker_text")
    val tickerText: String = "",
    @SerializedName("icon_url")
    val iconUrl: String = "",
)
