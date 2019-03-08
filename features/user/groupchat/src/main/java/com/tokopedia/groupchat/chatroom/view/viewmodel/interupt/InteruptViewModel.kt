package com.tokopedia.groupchat.chatroom.view.viewmodel.interupt

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 14/12/18.
 */
class InteruptViewModel : Parcelable {

    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("image_url")
    @Expose
    var imageUrl: String? = null
    @SerializedName("image_link")
    @Expose
    var imageLink: String? = null
    @SerializedName("btn_title")
    @Expose
    var btnTitle: String? = null
    @SerializedName("btn_link")
    @Expose
    var btnLink: String? = null

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

    constructor() {}

    constructor(title: String, description: String, imageUrl: String, imageLink: String, btnTitle: String, btnLink: String) {
        this.title = title
        this.description = description
        this.imageUrl = imageUrl
        this.imageLink = imageLink
        this.btnTitle = btnTitle
        this.btnLink = btnLink
    }

    protected constructor(`in`: Parcel) {
        this.title = `in`.readString()
        this.description = `in`.readString()
        this.imageUrl = `in`.readString()
        this.imageLink = `in`.readString()
        this.btnTitle = `in`.readString()
        this.btnLink = `in`.readString()
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<InteruptViewModel> = object : Parcelable.Creator<InteruptViewModel> {
            override fun createFromParcel(source: Parcel): InteruptViewModel {
                return InteruptViewModel(source)
            }

            override fun newArray(size: Int): Array<InteruptViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}
