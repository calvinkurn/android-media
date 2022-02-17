package com.tokopedia.gifting.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.gifting.domain.model.Basic
import com.tokopedia.gifting.domain.model.Inventory
import com.tokopedia.gifting.domain.model.Addon
import com.tokopedia.gifting.domain.model.Rules
import com.tokopedia.gifting.domain.model.GetAddOnByProductResponse
import com.tokopedia.gifting.domain.model.StaticInfo
import com.tokopedia.gifting.domain.model.AddOnByProductResponse

class StaticInfo {
    @SerializedName("InfoURL")
    @Expose
    var infoURL: String? = null
}