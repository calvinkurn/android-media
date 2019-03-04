package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 26/02/19
 */
data class FreezeViewModel (
        @SerializedName("category")
        @Expose
        var freezeCategory: String = "",
        @SerializedName("title")
        @Expose
        var freezeTitle: String = "",
        @SerializedName("desc")
        @Expose
        var freezeDesc: String = "",
        @SerializedName("btn_title")
        @Expose
        var freezeButtonTitle: String = "",
        @SerializedName("btn_app_link")
        @Expose
        var freezeButtonUrl: String = ""
) : Parcelable {

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.freezeCategory)
        dest.writeString(this.freezeTitle)
        dest.writeString(this.freezeDesc)
        dest.writeString(this.freezeButtonTitle)
        dest.writeString(this.freezeButtonUrl)
    }

    protected constructor(`in`: Parcel):this() {
        this.freezeCategory = `in`.readString()
        this.freezeTitle = `in`.readString()
        this.freezeDesc = `in`.readString()
        this.freezeButtonTitle = `in`.readString()
        this.freezeButtonUrl = `in`.readString()
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<FreezeViewModel> = object : Parcelable.Creator<FreezeViewModel> {
            override fun createFromParcel(source: Parcel): FreezeViewModel {
                return FreezeViewModel(source)
            }

            override fun newArray(size: Int): Array<FreezeViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}
