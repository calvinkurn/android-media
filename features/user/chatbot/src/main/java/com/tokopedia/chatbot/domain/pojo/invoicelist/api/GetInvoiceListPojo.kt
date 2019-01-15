package com.tokopedia.chatbot.domain.pojo.invoicelist.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 04/01/19.
 */

data class GetInvoiceListPojo(
        @SerializedName("getInvoiceList")
        @Expose
        val getInvoiceList: ArrayList<GetInvoiceList> = ArrayList()
)

data class GetInvoiceList(
        @SerializedName("TypeID")
        @Expose
        val typeId: Int = 0,
        @SerializedName("Type")
        @Expose
        val type: String = "",
        @SerializedName("IsError")
        @Expose
        val isError: Boolean = false,
        @SerializedName("Attributes")
        @Expose
        val attributes: Attributes = Attributes(),
        @SerializedName("Status")
        @Expose
        val status: Status = Status()
)

data class Attributes(
        @SerializedName("ID")
        @Expose
        val id: String = "0",
        @SerializedName("PaymentID")
        @Expose
        val paymentId: String = "0",
        @SerializedName("Code")
        @Expose
        val code: String = "0",
        @SerializedName("Title")
        @Expose
        val title: String = "",
        @SerializedName("Description")
        @Expose
        val description: String = "",
        @SerializedName("CreateTime")
        @Expose
        val createTime: String = "",
        @SerializedName("CreateTimeSort")
        @Expose
        val createTimeSort: String = "",
        @SerializedName("StatusID")
        @Expose
        val statusId: Int = 0,
        @SerializedName("Status")
        @Expose
        val status: String = "",
        @SerializedName("StatusTime")
        @Expose
        val statusTime: String = "",
        @SerializedName("TotalAmount")
        @Expose
        val totalAmount: String = "",
        @SerializedName("InvoiceURL")
        @Expose
        val invoiceUrl: String = "",
        @SerializedName("ImageURL")
        @Expose
        val imageUrl: String = "",
        @SerializedName("FailedTime")
        @Expose
        val failedTime: String = ""
)


data class Status(
        @SerializedName("Code")
        @Expose
        val code: Int = 0,
        @SerializedName("ErrorDetails")
        @Expose
        val errorDetails: String = ""
)