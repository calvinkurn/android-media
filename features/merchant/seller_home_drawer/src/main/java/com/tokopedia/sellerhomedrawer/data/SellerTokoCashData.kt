package com.tokopedia.sellerhomedrawer.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class SellerTokoCashData(): Parcelable {

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SellerTokoCashData> = object : Parcelable.Creator<SellerTokoCashData> {
            override fun createFromParcel(parcel: Parcel): SellerTokoCashData {
                return SellerTokoCashData(parcel)
            }

            override fun newArray(size: Int): Array<SellerTokoCashData?> {
                return arrayOfNulls(size)
            }
        }
    }

    protected constructor(parcel: Parcel): this() {

        action = parcel.readParcelable(Action::class.java.classLoader)
        balance = parcel.readString()
        redirectUrl = parcel.readString()
        appLinks = parcel.readString()
        text = parcel.readString()
        walletId = parcel.readLong()
        if (parcel.readByte().toInt() == 0)
            walletId = null
        link = parcel.readByte().toInt() != 0
        abTags = parcel.createStringArrayList()
        raw_balance = parcel.readLong()
        totalBalance = parcel.readString()
        rawTotalBalance = parcel.readLong()
        holdBalance = parcel.readString()
        rawHoldBalance = parcel.readLong()
        rawThreshold = parcel.readLong()
        threshold = parcel.readString()
    }

    @SerializedName("action")
    @Expose
    var action: Action? = null
    @SerializedName("balance")
    @Expose
    var balance: String? = null
    @SerializedName("redirect_url")
    @Expose
    var redirectUrl: String? = null
    @SerializedName("applinks")
    @Expose
    var appLinks: String? = null
    @SerializedName("text")
    @Expose
    var text: String? = null
    @SerializedName("wallet_id")
    @Expose
    var walletId: Long? = null
    @SerializedName("link")
    @Expose
    var link: Boolean = false
    @SerializedName("ab_tags")
    @Expose
    var abTags: List<String>? = null
    @SerializedName("raw_balance")
    @Expose
    var raw_balance: Long = 0
    @SerializedName("total_balance")
    @Expose
    var totalBalance: String? = null
    @SerializedName("raw_total_balance")
    @Expose
    var rawTotalBalance: Long = 0
    @SerializedName("hold_balance")
    @Expose
    var holdBalance: String? = null
    @SerializedName("raw_hold_balance")
    @Expose
    var rawHoldBalance: Long = 0
    @SerializedName("raw_threshold")
    @Expose
    var rawThreshold: Long = 0
    @SerializedName("threshold")
    @Expose
    var threshold: String? = null

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeParcelable(action, flags)
        dest?.writeString(balance)
        dest?.writeString(redirectUrl)
        dest?.writeString(appLinks)
        dest?.writeString(text)
        if (walletId == null) {
            dest?.writeByte(0.toByte())
        } else {
            dest?.writeByte(1.toByte())
            val walletId = walletId
            if (walletId != null)
                dest?.writeLong(walletId)
        }
        dest?.writeByte((if (link) 1 else 0).toByte())
        dest?.writeStringList(abTags)
        dest?.writeLong(raw_balance)
        dest?.writeString(totalBalance)
        dest?.writeLong(rawTotalBalance)
        dest?.writeString(holdBalance)
        dest?.writeLong(rawHoldBalance)
        dest?.writeLong(rawThreshold)
        dest?.writeString(threshold)
    }

    override fun describeContents(): Int = 0


}