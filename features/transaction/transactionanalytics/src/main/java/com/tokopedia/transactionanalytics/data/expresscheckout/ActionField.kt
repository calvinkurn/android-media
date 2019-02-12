package com.tokopedia.transactionanalytics.data.expresscheckout

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 07/02/19.
 */

data class ActionField(
        @SerializedName("step")
        var step: Int? = 0,

        @SerializedName("option")
        var option: String? = ""
) {
    companion object {
        const val STEP_1 = 1
        const val STEP_2 = 2
        const val OPTION_CLICK_CHECKOUT = "click checkout"
        const val OPTION_CLICK_BAYAR = "click bayar"
    }
}