package com.tokopedia.buyerorder.unifiedhistory.list.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 08/08/20.
 */
data class TrainResendEmailParam (
        @SerializedName("BookCode")
        val bookCode: String = "",

        @SerializedName("Email")
        val email: String = ""
)