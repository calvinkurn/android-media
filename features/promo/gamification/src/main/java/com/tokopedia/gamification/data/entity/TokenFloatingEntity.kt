package com.tokopedia.gamification.data.entity

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokenFloatingEntity(
    @SerializedName("tokenId") @Expose
    var tokenId: Int? = null,

    @SerializedName("tokenAsset")
@Expose
var tokenAsset: TokenAssetEntity? = null,

@SerializedName("pageUrl")
@Expose
 var pageUrl: String = "",

@SerializedName("applink")
@Expose
var applink: String = "",

@SerializedName("timeRemainingSeconds")
@Expose
var timeRemainingSeconds: Int? = null,

@SerializedName("isShowTime")
@Expose
var isShowTime: Boolean? = null,

@SerializedName("unixTimestamp")
@Expose
var unixTimestamp: Int? = null,
) : Parcelable {

    protected constructor(@SuppressLint("EntityFieldAnnotation") p:Parcel) : this(){
        tokenId = if (p.readByte().toInt() == 0) null
        else p.readInt()
        tokenAsset = p.readParcelable(TokenAssetEntity::class.java.classLoader)
        pageUrl = p.readString() ?: ""
        applink = p.readString() ?: ""
        timeRemainingSeconds = if (p.readByte().toInt() == 0) null
        else p.readInt()
        val tmpIsShowTime: Byte = p.readByte()
        isShowTime = if (tmpIsShowTime.toInt() == 0) null else tmpIsShowTime.toInt() == 1
        unixTimestamp = if (p.readByte().toInt() == 0) null
        else p.readInt()
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        if (tokenId == null) {
            dest?.writeByte(0)
        } else {
            dest?.writeByte(1)
            dest?.writeInt(tokenId!!)
        }
        dest?.writeParcelable(tokenAsset, flags)
        dest?.writeString(pageUrl)
        dest?.writeString(applink)
        if (timeRemainingSeconds == null) {
            dest?.writeByte(0)
        } else {
            dest?.writeByte(1)
            dest?.writeInt(timeRemainingSeconds!!)
        }
        dest?.writeByte((if (isShowTime == null) 0 else if (isShowTime!!) 1 else 2))
        if (unixTimestamp == null) {
            dest?.writeByte(0)
        } else {
            dest?.writeByte(1)
            dest?.writeInt(unixTimestamp!!)
        }
    }

    companion object{
        @JvmField
        val CREATOR=object : Parcelable.Creator<TokenFloatingEntity>{
            override fun createFromParcel(p: Parcel): TokenFloatingEntity {
                return TokenFloatingEntity(p)
            }

            override fun newArray(size: Int): Array<TokenFloatingEntity?> {
                return arrayOfNulls(size)
            }
        }
    }
}
