package com.tokopedia.addon.domain.model

import com.google.gson.annotations.SerializedName

data class GetAddOnByProductResponse (
    @SerializedName("GetAddOnByProduct" )
    var getAddOnByProduct: GetAddOnByProduct = GetAddOnByProduct()
)
