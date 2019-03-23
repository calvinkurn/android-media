package com.tokopedia.feedcomponent.view.viewmodel.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 23/03/19.
 */
data class ButtonCtaViewModel(
        var text: String = "",
        var appLink: String = "",
        var webLink: String = ""
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(text)
        writeString(appLink)
        writeString(webLink)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ButtonCtaViewModel> = object : Parcelable.Creator<ButtonCtaViewModel> {
            override fun createFromParcel(source: Parcel): ButtonCtaViewModel = ButtonCtaViewModel(source)
            override fun newArray(size: Int): Array<ButtonCtaViewModel?> = arrayOfNulls(size)
        }
    }
}