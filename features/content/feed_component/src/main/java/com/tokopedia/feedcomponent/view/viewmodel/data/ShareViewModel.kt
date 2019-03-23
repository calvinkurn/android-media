package com.tokopedia.feedcomponent.view.viewmodel.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ShareViewModel(

        val description: String = "",
        val imageUrl: String = "",
        val text: String = "",
        val title: String = "",
        val url: String = ""
) : Parcelable {
        constructor(source: Parcel) : this(
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                writeString(description)
                writeString(imageUrl)
                writeString(text)
                writeString(title)
                writeString(url)
        }

        companion object {
                @JvmField
                val CREATOR: Parcelable.Creator<ShareViewModel> = object : Parcelable.Creator<ShareViewModel> {
                        override fun createFromParcel(source: Parcel): ShareViewModel = ShareViewModel(source)
                        override fun newArray(size: Int): Array<ShareViewModel?> = arrayOfNulls(size)
                }
        }
}