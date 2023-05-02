package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class BeliButtonConfig(
    @SerializedName("button_type")
    val buttonType: Int = 0,
    @SerializedName("button_wording")
    val buttonWording: String = ""
) {
    companion object {
        const val BUTTON_TYPE_OCS = 1
        const val BUTTON_TYPE_OCC = 2
    }
}
