package com.tokopedia.home_account.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 18/07/20.
 */
data class UohCounterDataModel (
            @SerializedName("onProcess")
            val onProcess: String = "",
            @SerializedName("onProcessText")
            val onProcessText: String = "",
            @SerializedName("activeTickets")
            val activeTickets: String = "",
            @SerializedName("activeTicketsText")
            val activeTicketsText: String = ""
    )