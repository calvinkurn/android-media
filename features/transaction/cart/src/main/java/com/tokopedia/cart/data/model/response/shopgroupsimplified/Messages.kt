package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class Messages(
    @SerializedName("ErrorBOAffordability")
    val errorBoAffordability: String = ""
)
