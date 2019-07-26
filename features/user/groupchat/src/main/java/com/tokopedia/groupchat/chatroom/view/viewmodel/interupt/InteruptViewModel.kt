package com.tokopedia.groupchat.chatroom.view.viewmodel.interupt

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 14/12/18.
 */
class InteruptViewModel(
        @SerializedName("title")
        @Expose
        var title: String? = null,
        @SerializedName("description")
        @Expose
        var description: String? = null,
        @SerializedName("image_url")
        @Expose
        var imageUrl: String? = null,
        @SerializedName("image_link")
        @Expose
        var imageLink: String? = null,
        @SerializedName("btn_title")
        @Expose
        var btnTitle: String? = null,
        @SerializedName("btn_link")
        @Expose
        var btnLink: String? = null
) : Parcelable {


    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.title)
        dest.writeString(this.description)
        dest.writeString(this.imageUrl)
        dest.writeString(this.imageLink)
        dest.writeString(this.btnTitle)
        dest.writeString(this.btnLink)
    }

    companion object CREATOR : Parcelable.Creator<InteruptViewModel> {
        override fun createFromParcel(parcel: Parcel): InteruptViewModel {
            return InteruptViewModel(parcel)
        }

        override fun newArray(size: Int): Array<InteruptViewModel?> {
            return arrayOfNulls(size)
        }
    }

}
