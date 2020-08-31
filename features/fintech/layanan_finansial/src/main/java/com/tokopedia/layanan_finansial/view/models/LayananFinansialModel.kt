package com.tokopedia.layanan_finansial.view.models

import com.google.gson.annotations.SerializedName

data class LayananFinansialModel (
        @SerializedName("financial_widget")
        val sectionList: List<LayananSectionModel>? = null
)