package com.tokopedia.common_wallet.balance.view

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by nabillasabbaha on 9/10/19.
 */

class WalletBalanceModel(
        var titleText: String = "",
        var actionBalanceModel: ActionBalanceModel? = null,
        var balance: String = "",
        var rawBalance: Long = 0,
        var totalBalance: String = "",
        var rawTotalBalance: Long = 0,
        var holdBalance: String = "",
        var rawHoldBalance: Long = 0,
        var applinks: String = "",
        var redirectUrl: String = "",
        var link: Boolean = false,
        val rawThreshold: Long = 0,
        val threshold: String = "",
        var abTags: List<String>? = null,
        var pointBalance: String = "",
        var rawPointBalance: Int = 0,
        var cashBalance: String = "",
        var rawCashBalance: Int = 0,
        var walletType: String = "",
        var pendingCashback: String = "",
        var amountPendingCashback: Int = 0,
        var helpApplink: String = "",
        var tncApplink: String = "",
        var isShowAnnouncement: Boolean = false)

    : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readParcelable(ActionBalanceModel::class.java.classLoader),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readLong(),
            parcel.readString(),
            parcel.createStringArrayList(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(titleText)
        parcel.writeParcelable(actionBalanceModel, flags)
        parcel.writeString(balance)
        parcel.writeLong(rawBalance)
        parcel.writeString(totalBalance)
        parcel.writeLong(rawTotalBalance)
        parcel.writeString(holdBalance)
        parcel.writeLong(rawHoldBalance)
        parcel.writeString(applinks)
        parcel.writeString(redirectUrl)
        parcel.writeByte(if (link) 1 else 0)
        parcel.writeLong(rawThreshold)
        parcel.writeString(threshold)
        parcel.writeStringList(abTags)
        parcel.writeString(pointBalance)
        parcel.writeInt(rawPointBalance)
        parcel.writeString(cashBalance)
        parcel.writeInt(rawCashBalance)
        parcel.writeString(walletType)
        parcel.writeString(pendingCashback)
        parcel.writeInt(amountPendingCashback)
        parcel.writeString(helpApplink)
        parcel.writeString(tncApplink)
        parcel.writeByte(if (isShowAnnouncement) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WalletBalanceModel> {
        override fun createFromParcel(parcel: Parcel): WalletBalanceModel {
            return WalletBalanceModel(parcel)
        }

        override fun newArray(size: Int): Array<WalletBalanceModel?> {
            return arrayOfNulls(size)
        }
    }
}