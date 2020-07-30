package com.tokopedia.salam.umrah.orderdetail.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.salam.umrah.common.data.UmrahItemEntity
import com.tokopedia.salam.umrah.common.data.UmrahValueLabelEntity

/**
 * @author by furqan on 11/10/2019
 */
class UmrahOrderDetailsEntity(
        @SerializedName("invoice")
        @Expose
        val invoice: Invoice = Invoice(),
        @SerializedName("beforeOrderId")
        @Expose
        val beforeOrderId: Int = 0,
        @SerializedName("title")
        @Expose
        val title: List<DataDetail> = arrayListOf(),
        @SerializedName("helpLink")
        @Expose
        val helpLink: String = "",
        @SerializedName("conditionalInfo")
        @Expose
        val conditionalInfo: ConditionalInfo = ConditionalInfo(),
        @SerializedName("actionButtons")
        @Expose
        val actionButtons: List<ActionButton> = arrayListOf(),
        @SerializedName("detail")
        @Expose
        val details: List<DataDetail> = arrayListOf(),
        @SerializedName("paymentsData")
        @Expose
        val paymentsData: List<DataDetail> = arrayListOf(),
        @SerializedName("pricing")
        @Expose
        val pricing: List<DataDetail> = arrayListOf(),
        @SerializedName("payMethod")
        @Expose
        val payMethod: List<UmrahValueLabelEntity> = arrayListOf(),
        @SerializedName("status")
        @Expose
        val status: Status = Status(),
        @SerializedName("contactUs")
        @Expose
        val contactUs: ContactUs = ContactUs(),
        @SerializedName("items")
        @Expose
        val items: List<UmrahItemEntity> = arrayListOf(),
        @SerializedName("passenger")
        @Expose
        val passenger: List<Passenger> = arrayListOf(),
        @SerializedName("promoCode")
        @Expose
        val promoCode: String = ""
) {

        class Response(
                @SerializedName("orderDetails")
                @Expose
                val orderDetails: UmrahOrderDetailsEntity = UmrahOrderDetailsEntity()
        )

        class Invoice(
                @SerializedName("invoiceRefNum")
                @Expose
                val invoiceRefNum: String = "",
                @SerializedName("invoiceUrl")
                @Expose
                val invoiceUrl: String = ""
        )

        class ConditionalInfo(
                @SerializedName("text")
                @Expose
                val text: String = "",
                @SerializedName("url")
                @Expose
                val url: String = "",
                @SerializedName("title")
                @Expose
                val title: String = ""
        )

        class ActionButton(
                @SerializedName("label")
                @Expose
                val label: String,
                @SerializedName("buttonType")
                @Expose
                val buttonType: String,
                @SerializedName("body")
                @Expose
                val body: Body = Body()
        ) {
                class Body(
                        @SerializedName("appURL")
                        @Expose
                        val appUrl: String = "",
                        @SerializedName("webURL")
                        @Expose
                        val webUrl: String = ""
                )
        }

        class DataDetail(
                @SerializedName("imageUrl")
                @Expose
                val imageUrl: String = "",
                @SerializedName("backgroundColor")
                @Expose
                val backgroundColor: String = "",
                @SerializedName("textColor")
                @Expose
                val textColor: String = "",
                @SerializedName("value")
                @Expose
                val value: String = "",
                @SerializedName("label")
                @Expose
                val label: String = ""
        )

        class Status(
                @SerializedName("status")
                @Expose
                val status: Int = 0,
                @SerializedName("statusText")
                @Expose
                val statusText: String = "",
                @SerializedName("statusLabel")
                @Expose
                val statusLabel: String = "",
                @SerializedName("fontSize")
                @Expose
                val fontSize: String = "",
                @SerializedName("backgroundColor")
                @Expose
                val backgroundColor: String = "",
                @SerializedName("textColor")
                @Expose
                val textColor: String = "",
                @SerializedName("iconUrl")
                @Expose
                val iconUrl: String = ""
        )

        class ContactUs(
                @SerializedName("helpUrl")
                @Expose
                val helpUrl: String = "",
                @SerializedName("helpText")
                @Expose
                val helpText: String = ""
        )

        class Passenger(
                @SerializedName("id")
                @Expose
                val id: Int = 0,
                @SerializedName("name")
                @Expose
                val name: String = ""
        )

}