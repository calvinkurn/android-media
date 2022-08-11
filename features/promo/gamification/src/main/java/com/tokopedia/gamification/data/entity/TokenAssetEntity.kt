package com.tokopedia.gamification.data.entity

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class TokenAssetEntity(
    @SerializedName("smallImgUrl")
    var smallImgUrl: String = "",

    @SerializedName("floatingImgUrl")
var floatingImgUrl: String = "",

@SerializedName("imageUrls")
var imageUrls: List<String>? = null,

@SerializedName("name")
var name: String = "",

@SerializedName("imagev2Urls")
var imagev2Urls: List<String>? = null,

@SerializedName("spriteUrl")
var spriteUrl: String = "",

@SerializedName("version")
var version:Int = 0,

@SerializedName("smallImgv2Url")
var smallImgv2Url: String = "",

@SerializedName("tokenSourceUrl")
var tokenSourceUrl: String = "",
) : Parcelable{


   protected constructor(@SuppressLint("EntityFieldAnnotation") p:Parcel) : this(
       p.readString() ?: "",
       p.readString() ?: "",
       p.createStringArrayList(),
       p.readString() ?: "",
       p.createStringArrayList(),
       p.readString() ?: "",
       p.readInt(),
       p.readString() ?: "",
       p.readString() ?: ""
   )


    override fun describeContents() = 0

    companion object {
        @JvmField
       val CREATOR =object: Parcelable.Creator<TokenAssetEntity> {
           override fun createFromParcel(`in`: Parcel): TokenAssetEntity {
               return TokenAssetEntity(`in`)
           }

           override fun newArray(size: Int): Array<TokenAssetEntity?> {
               return arrayOfNulls(size)
           }
       }
    }


    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(smallImgUrl)
        dest?.writeString(floatingImgUrl)
        dest?.writeStringList(imageUrls)
        dest?.writeString(name)
        dest?.writeStringList(imagev2Urls)
        dest?.writeString(spriteUrl)
        dest?.writeInt(version)
        dest?.writeString(smallImgv2Url)
        dest?.writeString(tokenSourceUrl)
    }
}
