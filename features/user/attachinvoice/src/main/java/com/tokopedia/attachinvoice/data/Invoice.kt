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

    override fun type(typeFactory: AttachInvoiceTypeFactory?): Int {
        return -1
    }

}