package com.tokopedia.createpost.common.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import android.view.View
import com.tokopedia.createpost.common.data.feedrevamp.FeedXMediaTagging

/**
 * Created by isfaaghyth on 20/03/19.
 * github: @isfaaghyth
 */
data class MediaModel(
    val path: String = "",
    var type: String = "image",
    var isPlaying: Boolean = false,
    var tags: MutableList<FeedXMediaTagging> = arrayListOf(),
    var products: MutableList<RelatedProductItem> = arrayListOf(),
    @Transient
    var videoView: View? = null,
    @Transient
    var imageView: View? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString()?: "",
            parcel.readString()?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        writeString(path)
        writeString(type)
    }

    override fun describeContents(): Int = 0

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MediaModel> = object : Parcelable.Creator<MediaModel> {
            override fun createFromParcel(source: Parcel): MediaModel = MediaModel(source)
            override fun newArray(size: Int): Array<MediaModel?> = arrayOfNulls(size)
        }
    }
}

class MediaType {
    companion object {
        const val IMAGE = "image"
        const val VIDEO = "video"
    }
}