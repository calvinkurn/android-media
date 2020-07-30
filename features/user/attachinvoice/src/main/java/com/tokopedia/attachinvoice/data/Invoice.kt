package com.tokopedia.attachinvoice.data


import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.attachinvoice.view.adapter.AttachInvoiceTypeFactory

data class Invoice(
    @SerializedName("attributes")
    val attributes: Attributes = Attributes(),
    @SerializedName("type")
    val type: String = "",
    @SerializedName("typeId")
    val typeId: Int = 0
): Visitable<AttachInvoiceTypeFactory> {

    val url get() = attributes.hrefURL
    val id get() = attributes.id
    val thumbnailUrl get() = attributes.imageURL
    val status get() = attributes.status
    val statusId get() = attributes.statusId
    val timeStamp get() = attributes.createTime
    val code get() = attributes.code
    val productName get() = attributes.title
    val productPrice get() = attributes.totalAmount

    override fun type(typeFactory: AttachInvoiceTypeFactory): Int {
        return typeFactory.type(this)
    }

}