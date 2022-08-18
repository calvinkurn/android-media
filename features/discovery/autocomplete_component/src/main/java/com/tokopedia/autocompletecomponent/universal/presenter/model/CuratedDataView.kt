package com.tokopedia.autocompletecomponent.universal.presenter.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CuratedDataView(
    val id: String = "",
    val url: String = "",
    val applink: String = "",
    val imageUrl: String = "",
    val title: String = "",
    val componentId: String = "",
    val trackingOption: Int = 0,
    val campaignCode: String = "",
)