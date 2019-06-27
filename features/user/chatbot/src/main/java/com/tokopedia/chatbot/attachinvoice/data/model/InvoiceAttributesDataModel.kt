package com.tokopedia.chatbot.attachinvoice.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Hendri on 28/03/18.
 */

class InvoiceAttributesDataModel(@field:SerializedName("id")
                                 @field:Expose
                                 var invoiceId: Long = 0,
                                 @field:SerializedName("code")
                                 @field:Expose
                                 var invoiceNo: String = "",
                                 @field:SerializedName("title")
                                 @field:Expose
                                 var title: String = "",
                                 @field:SerializedName("description")
                                 @field:Expose
                                 var description: String = "",
                                 @field:SerializedName("create_time")
                                 @field:Expose
                                 var invoiceDate: String = "",
                                 @field:SerializedName("image_url")
                                 @field:Expose
                                 var imageUrl: String = "",
                                 @field:SerializedName("status_id")
                                 @field:Expose
                                 var statusId: Int,
                                 @field:SerializedName("status")
                                 @field:Expose
                                 var status: String = "",
                                 @field:SerializedName("total_amount")
                                 @field:Expose
                                 var amount: String = "",
                                 @field:SerializedName("invoice_url")
                                 @field:Expose
                                 var url: String = "")
