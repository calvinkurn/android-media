package com.tokopedia.gamification.data.entity

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokenEmptyStateEntity(
    @SerializedName("title") @Expose
    var title: String? = null,

    @SerializedName("buttonText")
@Expose
var buttonText: String = "",

@SerializedName("buttonApplink")
@Expose
var buttonApplink: String = "",

@SerializedName("buttonURL")
@Expose
var buttonURL: String = "",

@SerializedName("backgroundImgUrl")
@Expose
 var backgroundImgUrl: String = "",

@SerializedName("imageUrl")
@Expose
 var imageUrl: String = "",

@SerializedName("version")
@Expose
 var version: Int? = null,
): Parcelable {

    protected constructor(@SuppressLint("EntityFieldAnnotation") p:Parcel) : this(){
        title = p.readString() ?: ""
        buttonText = p.readString() ?: ""
        buttonApplink = p.readString() ?: ""
        buttonURL = p.readString() ?: ""
        backgroundImgUrl = p.readString() ?: ""
        imageUrl = p.readString() ?: ""
        version = if (p.readByte().toInt() == 0) null
        else p.readInt()
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(title)
        dest?.writeString(buttonText)
        dest?.writeString(buttonApplink)
        dest?.writeString(buttonURL)
        dest?.writeString(backgroundImgUrl)
        dest?.writeString(imageUrl)
        if (version == null) {
            dest?.writeByte(0.toByte())
        } else {
            dest?.writeByte(1.toByte())
            dest?.writeInt(version!!)
        }
    }

    companion object{
        @JvmField
        val CREATOR = object : Parcelable.Creator<TokenEmptyStateEntity>{
            override fun createFromParcel(p: Parcel): TokenEmptyStateEntity {
                return TokenEmptyStateEntity(p)
            }

            override fun newArray(size: Int): Array<TokenEmptyStateEntity?> {
                return arrayOfNulls(size)
            }
        }
    }
}
