package com.tokopedia.chatbot.attachinvoice.view.resultmodel

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.chatbot.attachinvoice.view.model.InvoiceViewModel
import com.tokopedia.chatbot.attachinvoice.view.model.TransactionInvoiceUiModel
import com.tokopedia.kotlin.extensions.view.toLongOrZero

/**
 * Created by Hendri on 27/03/18.
 */
class SelectedInvoice() : Parcelable {
    var invoiceId: Long? = null
    var invoiceNo: String? = null
    var invoiceTypeStr: String? = null
    var invoiceType: Int? = null
    var topProductName: String? = null
    var topProductImage: String? = null
    var description: String? = null
    var amount: String? = null
    var date: String? = null
    var invoiceUrl: String? = null
    var status: String? = null
    var statusId: Int = 0

    constructor(invoiceId: Long?, invoiceNo: String, invoiceTypeStr: String,
                invoiceType: Int?, topProductName: String, topProductImage: String,
                description: String, amount: String, date: String,
                invoiceUrl: String, status: String, statusId: Int) : this() {
        this.invoiceId = invoiceId
        this.invoiceNo = invoiceNo
        this.invoiceTypeStr = invoiceTypeStr
        this.invoiceType = invoiceType
        this.topProductName = topProductName
        this.topProductImage = topProductImage
        this.description = description
        this.amount = amount
        this.date = date
        this.invoiceUrl = invoiceUrl
        this.status = status
        this.statusId = statusId
    }

    constructor(viewModel: InvoiceViewModel) : this() {
        this.invoiceId = viewModel.invoiceId
        this.invoiceNo = viewModel.invoiceNumber
        this.invoiceTypeStr = viewModel.invoiceTypeStr
        this.invoiceType = viewModel.invoiceType
        this.topProductName = viewModel.productTopName
        this.topProductImage = viewModel.productTopImage
        this.description = viewModel.description
        this.amount = viewModel.total
        this.date = viewModel.date
        this.invoiceUrl = viewModel.invoiceUrl
        this.status = viewModel.status
        this.statusId = viewModel.statusId
    }

    constructor(viewModel: TransactionInvoiceUiModel) : this() {
        this.invoiceId = viewModel.invoiceId.toLongOrZero()
        this.invoiceNo = viewModel.invoiceNumber
        this.invoiceTypeStr = viewModel.invoiceTypeStr
        this.invoiceType = viewModel.invoiceType
        this.topProductName = viewModel.title
        this.topProductImage = viewModel.imageUrl
        this.description = viewModel.description
        this.amount = viewModel.amount
        this.date = viewModel.createdTime
        this.invoiceUrl = viewModel.invoiceUrl
        this.status = viewModel.status
        this.statusId = viewModel.statusId
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(this.invoiceId!!)
        dest.writeString(this.invoiceNo)
        dest.writeString(this.invoiceTypeStr)
        dest.writeValue(this.invoiceType)
        dest.writeString(this.topProductName)
        dest.writeString(this.topProductImage)
        dest.writeString(this.description)
        dest.writeString(this.amount)
        dest.writeString(this.date)
        dest.writeString(this.invoiceUrl)
        dest.writeString(this.status)
        dest.writeInt(this.statusId)
    }

    constructor(parcel: Parcel) : this() {
        this.invoiceId = parcel.readLong()
        this.invoiceNo = parcel.readString()
        this.invoiceTypeStr = parcel.readString()
        this.invoiceType = parcel.readValue(Int::class.java.classLoader) as Int
        this.topProductName = parcel.readString()
        this.topProductImage = parcel.readString()
        this.description = parcel.readString()
        this.amount = parcel.readString()
        this.date = parcel.readString()
        this.invoiceUrl = parcel.readString()
        this.status = parcel.readString()
        this.statusId = parcel.readInt()
    }

    companion object CREATOR : Parcelable.Creator<SelectedInvoice> {
        override fun createFromParcel(parcel: Parcel): SelectedInvoice {
            return SelectedInvoice(parcel)
        }

        override fun newArray(size: Int): Array<SelectedInvoice?> {
            return arrayOfNulls(size)
        }
    }
}
