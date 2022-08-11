package com.tokopedia.gamification.data.entity

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokenBackgroundAssetEntity(
    @SerializedName("name") @Expose
     var name: String = "",

    @SerializedName("version")
@Expose
var version: String = "",

@SerializedName("backgroundImgUrl")
@Expose
 var backgroundImgUrl: String = "",
) : Parcelable{

    constructor(@SuppressLint("EntityFieldAnnotation") parcel : Parcel) : this(){
        name = parcel.readString() ?: ""
        version = parcel.readString() ?: ""
        backgroundImgUrl = parcel.readString() ?: ""
    }

    override fun describeContents() = 0
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(name)
        dest?.writeString(version)
        dest?.writeString(backgroundImgUrl)
    }

    companion object{
        @JvmField
        val CREATOR = object : Parcelable.Creator<TokenBackgroundAssetEntity>{
            override fun createFromParcel(p: Parcel): TokenBackgroundAssetEntity {
                return TokenBackgroundAssetEntity(p)
            }

            override fun newArray(size: Int): Array<TokenBackgroundAssetEntity?> {
                return arrayOfNulls(size)
            }
        }
    }
}
