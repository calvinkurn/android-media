package com.tokopedia.gamification.data.entity

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResultStatusEntity(
    @SerializedName("code")
    @Expose
     var code: String = "",

    @SerializedName("message")
@Expose
 var message: List<String>? = null,

@SerializedName("status")
@Expose
 var status: String = "",

) : Parcelable{


    private constructor(@SuppressLint("EntityFieldAnnotation") p:Parcel) : this(
        p.readString() ?: "",
        p.createStringArrayList(),
        p.readString() ?: ""
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(code)
        dest.writeStringList(message)
        dest.writeString(status)
    }

    companion object{
        @JvmField
        val CREATOR = object : Parcelable.Creator<ResultStatusEntity>{
            override fun createFromParcel(parcel: Parcel): ResultStatusEntity {
                return ResultStatusEntity(parcel)
            }

            override fun newArray(size: Int): Array<ResultStatusEntity?> {
                return arrayOfNulls(size)
            }
        }
    }
}