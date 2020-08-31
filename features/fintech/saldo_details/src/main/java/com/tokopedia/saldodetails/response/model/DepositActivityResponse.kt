package com.tokopedia.saldodetails.response.model

import com.google.gson.annotations.SerializedName

data class DepositActivityResponse(

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("have_error")
    var isHaveError: Boolean = false,

    @SerializedName("have_next_page")
    var isHaveNextPage: Boolean = false,

    @SerializedName("deposit_history_list")
    var depositHistoryList: List<DepositHistoryList>? = null
)
