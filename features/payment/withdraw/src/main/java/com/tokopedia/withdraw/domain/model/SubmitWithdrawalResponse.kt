package com.tokopedia.withdraw.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SubmitWithdrawalResponse(
        @SerializedName("process_time")
        @Expose
        val processTime: Float?,
        @SerializedName("message")
        @Expose
        val messageArrayList: ArrayList<String>?,
        @SerializedName("status")
        @Expose
        val status: String?,
        @SerializedName("message_error")
        @Expose
        val messageErrorStr: String?,
        @SerializedName("withdrawalNote")
        @Expose
        val withdrawalNote: String?,
        @SerializedName("errorCode")
        @Expose
        val errorCode: String?)

data class GQLSubmitWithdrawalResponse(
        @SerializedName("richieSubmitWithdrawal")
        @Expose
        val submitWithdrawalResponse: SubmitWithdrawalResponse
)