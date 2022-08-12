package com.tokopedia.gamification.data.entity

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class TokenUserEntity(
    @SerializedName("tokenUserIDstr") @Expose
    var tokenUserID: String? = null,

    @SerializedName("campaignID")
    @Expose
    var campaignID: String = "",

    @SerializedName("title")
    @Expose
    var title: String = "",

    @SerializedName("unixTimestampFetch")
    @Expose
   var unixTimestampFetch: Int? = null,

    @SerializedName("timeRemainingSeconds")
    @Expose
    var timeRemainingSeconds: Int? = null,

    @SerializedName("isShowTime")
    @Expose
     var isShowTime: Boolean? = null,

    @SerializedName("backgroundAsset")
    @Expose
    var backgroundAsset: TokenBackgroundAssetEntity? = null,

    @SerializedName("tokenAsset")
    @Expose
    var tokenAsset: TokenAssetEntity? = null,
) : Parcelable{

    protected constructor(@SuppressLint("EntityFieldAnnotation") p : Parcel) : this(){
        tokenUserID = if (p.readByte().toInt() == 0) null
        else p.readString()

        campaignID =  p.readString() ?: ""

        title = p.readString() ?: ""
        unixTimestampFetch = if(p.readByte().toInt() == 0) null else p.readInt()

        timeRemainingSeconds = if(p.readByte().toInt() == 0) null else p.readInt()

        val tmpIsShowTime= p.readByte()
        isShowTime = if (tmpIsShowTime.toInt() == 0) null else tmpIsShowTime.toInt() == 1
        backgroundAsset = p.readParcelable(TokenBackgroundAssetEntity::class.java.classLoader)
        tokenAsset = p.readParcelable(TokenAssetEntity::class.java.classLoader)
    }


    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        if (tokenUserID == null) {
            dest?.writeByte(0)
        } else {
            dest?.writeByte(1)
            dest?.writeString(tokenUserID)
        }
        dest?.writeString(campaignID)
        dest?.writeString(title)
        if (unixTimestampFetch == null) {
            dest?.writeByte(0)
        } else {
            dest?.writeByte(1)
            dest?.writeInt(unixTimestampFetch!!)
        }
        if (timeRemainingSeconds == null) {
            dest?.writeByte(0)
        } else {
            dest?.writeByte(1)
            dest?.writeInt(timeRemainingSeconds!!)
        }
        dest?.writeByte((if (isShowTime == null) 0 else if (isShowTime!!) 1 else 2))
        dest?.writeParcelable(backgroundAsset, flags)
        dest?.writeParcelable(tokenAsset, flags)
    }

    companion object{
        @JvmField
        val CREATOR = object : Parcelable.Creator<TokenUserEntity>{
            override fun createFromParcel(p: Parcel): TokenUserEntity {
                return TokenUserEntity(p)
            }

            override fun newArray(size: Int): Array<TokenUserEntity?> {
                return arrayOfNulls(size)
            }
        }
    }
}