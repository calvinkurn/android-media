package com.tokopedia.gamification.data.entity

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokenDataEntity(
    @SerializedName("resultStatus") @Expose
    var resultStatus: ResultStatusEntity? = null,

    @SerializedName("offFlag")
@Expose
var offFlag: Boolean? = null,

@SerializedName("sumToken")
@Expose
var sumToken: Int? = 0,

@SerializedName("sumTokenStr")
@Expose
var sumTokenStr: String = "",

@SerializedName("tokenUnit")
@Expose
var tokenUnit: String = "",

@SerializedName("floating")
@Expose
var floating: TokenFloatingEntity? = null,

@SerializedName("home")
@Expose
var home: TokenHomeEntity? = null,
) : Parcelable{

    protected constructor(@SuppressLint("EntityFieldAnnotation") p : Parcel):this(){
        resultStatus = p.readParcelable(ResultStatusEntity::class.java.classLoader)
        val tmpOffFlag = p.readByte()
        offFlag = if (tmpOffFlag.toInt() == 0) null
                  else tmpOffFlag.toInt() == 1

        sumToken = if (p.readByte().toInt() == 0) null
                   else p.readInt()

        sumTokenStr = p.readString() ?: ""
        tokenUnit = p.readString() ?: ""
        floating = p.readParcelable(TokenFloatingEntity::class.java.classLoader)
        home = p.readParcelable(TokenHomeEntity::class.java.classLoader)
    }


    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeParcelable(resultStatus, flags)
        val ans = if(offFlag==null) 0
                  else if(offFlag!!) 1
                  else 2
        dest?.writeByte(ans.toByte())
        if(sumToken==null) dest?.writeByte(0.toByte())
        else{
            dest?.writeByte(1.toByte())
            dest?.writeInt(sumToken!!)
        }
        dest?.writeString(sumTokenStr)
        dest?.writeString(tokenUnit)
        dest?.writeParcelable(floating, flags)
        dest?.writeParcelable(home, flags)
    }

    companion object{
        @JvmField
        val CREATOR = object : Parcelable.Creator<TokenDataEntity>{
            override fun createFromParcel(parcel: Parcel): TokenDataEntity {
                return TokenDataEntity(parcel)
            }

            override fun newArray(size: Int): Array<TokenDataEntity?> {
                return arrayOfNulls(size)
            }
        }
    }
}
