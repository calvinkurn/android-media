package com.tokopedia.sellerhomedrawer.data.header

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class SellerHomeHeaderWalletAction() : Parcelable {

    constructor(parcel: Parcel): this() {
        labelTitle = parcel.readString()
        balance = parcel.readString()
        redirectUrlBalance = parcel.readString()
        appLinkBalance = parcel.readString()
        typeAction = parcel.readInt()
        isVisibleActionButton = parcel.readByte().toInt() != 0
        labelActionButton = parcel.readString()
        appLinkActionButton = parcel.readString()
        redirectUrlActionButton = parcel.readString()
        isLinked = parcel.readByte().toInt() != 0
        abTags = parcel.createStringArrayList()
        pointBalance = parcel.readString()
        rawPointBalance = parcel.readInt()
        cashBalance = parcel.readString()
        rawCashBalance = parcel.readInt()
        walletType = parcel.readString()
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SellerHomeHeaderWalletAction> = object : Parcelable.Creator<SellerHomeHeaderWalletAction> {
            override fun createFromParcel(parcel: Parcel): SellerHomeHeaderWalletAction {
                return SellerHomeHeaderWalletAction(parcel)
            }

            override fun newArray(size: Int): Array<SellerHomeHeaderWalletAction?> {
                return arrayOfNulls(size)
            }
        }
    }

    var labelTitle: String? = null
    var balance: String? = null
    var redirectUrlBalance: String? = null
    var appLinkBalance: String? = null
    var typeAction: Int = 0
    var isVisibleActionButton: Boolean = false
    var labelActionButton: String? = null
    var appLinkActionButton: String? = null
    var redirectUrlActionButton: String? = null
    var isLinked: Boolean = false
    var abTags: List<String>? = ArrayList()
    var pointBalance: String? = null
    var rawPointBalance: Int = 0
    var cashBalance: String? = null
    var rawCashBalance: Int = 0
    var walletType: String? = null

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(labelTitle)
        dest.writeString(balance)
        dest.writeString(redirectUrlBalance)
        dest.writeString(appLinkBalance)
        dest.writeInt(typeAction)
        dest.writeByte((if (isVisibleActionButton) 1 else 0).toByte())
        dest.writeString(labelActionButton)
        dest.writeString(appLinkActionButton)
        dest.writeString(redirectUrlActionButton)
        dest.writeByte((if (isLinked) 1 else 0).toByte())
        dest.writeStringList(abTags)
        dest.writeString(pointBalance)
        dest.writeInt(rawPointBalance)
        dest.writeString(cashBalance)
        dest.writeInt(rawCashBalance)
        dest.writeString(walletType)
    }

    override fun describeContents(): Int {
        return 0
    }
}
