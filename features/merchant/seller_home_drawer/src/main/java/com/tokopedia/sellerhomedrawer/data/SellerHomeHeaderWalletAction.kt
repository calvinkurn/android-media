package com.tokopedia.sellerhomedrawer.data

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class SellerHomeHeaderWalletAction(): Parcelable {

    var labelTitle: String? = null
    var balance: String? = null
    var redirectUrlBalance: String? = null
    var appLinkBalance: String? = null
    var typeAction: Int = 0
    var visibleActionButton: Boolean = false
    var labelActionButton: String? = null
    var appLinkActionButton: String? = null
    var redirectUrlActionButton: String? = null
    var linked: Boolean = false
    var abTags: List<String>? = ArrayList()
    var pointBalance: String? = null
    var rawPointBalance: Int = 0
    var cashBalance: String? = null
    var rawCashBalance: Int = 0
    var walletType: String? = null

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

    protected constructor(parcel: Parcel): this() {

    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(labelTitle)
        dest?.writeString(balance)
        dest?.writeString(redirectUrlBalance)
        dest?.writeString(appLinkBalance)
        dest?.writeInt(typeAction)
        dest?.writeByte((if (visibleActionButton) 1 else 0).toByte())
        dest?.writeString(labelActionButton)
        dest?.writeString(appLinkActionButton)
        dest?.writeString(redirectUrlActionButton)
        dest?.writeByte((if (linked) 1 else 0).toByte())
        dest?.writeStringList(abTags)
        dest?.writeString(pointBalance)
        dest?.writeInt(rawPointBalance)
        dest?.writeString(cashBalance)
        dest?.writeInt(rawCashBalance)
        dest?.writeString(walletType)
    }

    override fun describeContents(): Int = 0
}