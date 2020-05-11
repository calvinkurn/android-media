package com.tokopedia.home.beranda.presentation.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class HomeHeaderWalletAction(
        var labelTitle: String = "",
        val balance: String = "",
        val redirectUrlBalance: String = "",
        val appLinkBalance: String = "",
        val typeAction: Int = 0,
        val isVisibleActionButton: Boolean = false,
        val labelActionButton: String = "",
        val appLinkActionButton: String = "",
        val isLinked: Boolean = false,
        val abTags: List<String> = ArrayList<String>(),
        val pointBalance: String = "",
        val rawPointBalance: Int = 0,
        val cashBalance: String = "",
        val rawCashBalance: Int = 0,
        val walletType: String = "",
        val isShowAnnouncement: Boolean = false
) : Parcelable{
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.createStringArrayList(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(labelTitle)
        parcel.writeString(balance)
        parcel.writeString(redirectUrlBalance)
        parcel.writeString(appLinkBalance)
        parcel.writeInt(typeAction)
        parcel.writeByte(if (isVisibleActionButton) 1 else 0)
        parcel.writeString(labelActionButton)
        parcel.writeString(appLinkActionButton)
        parcel.writeByte(if (isLinked) 1 else 0)
        parcel.writeStringList(abTags)
        parcel.writeString(pointBalance)
        parcel.writeInt(rawPointBalance)
        parcel.writeString(cashBalance)
        parcel.writeInt(rawCashBalance)
        parcel.writeString(walletType)
        parcel.writeByte(if (isShowAnnouncement) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HomeHeaderWalletAction) return false

        if (labelTitle != other.labelTitle) return false
        if (balance != other.balance) return false
        if (redirectUrlBalance != other.redirectUrlBalance) return false
        if (appLinkBalance != other.appLinkBalance) return false
        if (typeAction != other.typeAction) return false
        if (isVisibleActionButton != other.isVisibleActionButton) return false
        if (labelActionButton != other.labelActionButton) return false
        if (appLinkActionButton != other.appLinkActionButton) return false
        if (isLinked != other.isLinked) return false
        if (abTags != other.abTags) return false
        if (pointBalance != other.pointBalance) return false
        if (rawPointBalance != other.rawPointBalance) return false
        if (cashBalance != other.cashBalance) return false
        if (rawCashBalance != other.rawCashBalance) return false
        if (walletType != other.walletType) return false
        if (isShowAnnouncement != other.isShowAnnouncement) return false

        return true
    }

    override fun hashCode(): Int {
        var result = labelTitle.hashCode()
        result = 31 * result + balance.hashCode()
        result = 31 * result + redirectUrlBalance.hashCode()
        result = 31 * result + appLinkBalance.hashCode()
        result = 31 * result + typeAction
        result = 31 * result + isVisibleActionButton.hashCode()
        result = 31 * result + labelActionButton.hashCode()
        result = 31 * result + appLinkActionButton.hashCode()
        result = 31 * result + isLinked.hashCode()
        result = 31 * result + abTags.hashCode()
        result = 31 * result + pointBalance.hashCode()
        result = 31 * result + rawPointBalance
        result = 31 * result + cashBalance.hashCode()
        result = 31 * result + rawCashBalance
        result = 31 * result + walletType.hashCode()
        result = 31 * result + isShowAnnouncement.hashCode()
        return result
    }

    companion object CREATOR : Parcelable.Creator<HomeHeaderWalletAction> {
        override fun createFromParcel(parcel: Parcel): HomeHeaderWalletAction {
            return HomeHeaderWalletAction(parcel)
        }

        override fun newArray(size: Int): Array<HomeHeaderWalletAction?> {
            return arrayOfNulls(size)
        }
    }



}