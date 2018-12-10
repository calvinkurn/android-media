package com.tokopedia.chatbot.attachinvoice.view.resultmodel

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.chatbot.attachinvoice.view.model.InvoiceViewModel

/**
 * Created by Hendri on 27/03/18.
 */
class SelectedInvoice : Parcelable {
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
                invoiceUrl: String, status: String, statusId: Int) {
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

    constructor(viewModel: InvoiceViewModel) {
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

    protected constructor(`in`: Parcel) {
        this.invoiceId = `in`.readLong()
        this.invoiceNo = `in`.readString()
        this.invoiceTypeStr = `in`.readString()
        this.invoiceType = `in`.readValue(Int::class.java.classLoader) as Int
        this.topProductName = `in`.readString()
        this.topProductImage = `in`.readString()
        this.description = `in`.readString()
        this.amount = `in`.readString()
        this.date = `in`.readString()
        this.invoiceUrl = `in`.readString()
        this.status = `in`.readString()
        this.statusId = `in`.readInt()
    }

    companion object {

        val CREATOR: Parcelable.Creator<SelectedInvoice> = object : Parcelable.Creator<SelectedInvoice> {
            override fun createFromParcel(source: Parcel): SelectedInvoice {
                return SelectedInvoice(source)
            }

            override fun newArray(size: Int): Array<SelectedInvoice> {
                return arrayOfNulls(size)
            }
        }
    }
}
