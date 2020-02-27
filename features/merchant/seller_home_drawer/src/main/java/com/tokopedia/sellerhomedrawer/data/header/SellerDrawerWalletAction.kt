package com.tokopedia.sellerhomedrawer.data.header

import android.os.Parcel
import android.os.Parcelable

class SellerDrawerWalletAction() : Parcelable {

    constructor(parcel: Parcel): this() {
        this.labelTitle = parcel.readString()
        this.balance = parcel.readString()
        this.redirectUrlBalance = parcel.readString()
        this.appLinkBalance = parcel.readString()
        this.typeAction = parcel.readInt()
        this.isVisibleActionButton = parcel.readByte().toInt() != 0
        this.labelActionButton = parcel.readString()
        this.appLinkActionButton = parcel.readString()
        this.redirectUrlActionButton = parcel.readString()
    }

    companion object {
        val TYPE_ACTION_ACTIVATION = 1
        val TYPE_ACTION_BALANCE = 2

        @JvmField
        val CREATOR: Parcelable.Creator<SellerDrawerWalletAction> = object : Parcelable.Creator<SellerDrawerWalletAction> {
            override fun createFromParcel(source: Parcel): SellerDrawerWalletAction {
                return SellerDrawerWalletAction(source)
            }

            override fun newArray(size: Int): Array<SellerDrawerWalletAction?> {
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

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.labelTitle)
        dest.writeString(this.balance)
        dest.writeString(this.redirectUrlBalance)
        dest.writeString(this.appLinkBalance)
        dest.writeInt(this.typeAction)
        dest.writeByte(if (this.isVisibleActionButton) 1.toByte() else 0.toByte())
        dest.writeString(this.labelActionButton)
        dest.writeString(this.appLinkActionButton)
        dest.writeString(this.redirectUrlActionButton)
    }

}
