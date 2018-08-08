package com.tokopedia.product.manage.item.video.view.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.item.video.view.adapter.ProductAddVideoRecommendationAdapterTypeFactory

class VideoRecommendationViewModel() : Visitable<ProductAddVideoRecommendationAdapterTypeFactory>, ProductAddVideoRecommendationBaseViewModel, Parcelable{

    var videoID: String? = null
    var snippetTitle: String? = null
    var snippetDescription: String? = null
    var snippetChannel: String? = null
    var thumbnailUrl: String? = null
    var width: Int = 0
    var height: Int = 0
    var duration: String? = null
    var chosen: Boolean = false

    constructor(parcel: Parcel) : this() {
        videoID = parcel.readString()
        snippetTitle = parcel.readString()
        snippetDescription = parcel.readString()
        snippetChannel = parcel.readString()
        thumbnailUrl = parcel.readString()
        width = parcel.readInt()
        height = parcel.readInt()
        duration = parcel.readString()
        chosen = parcel.readByte() != 0.toByte()
    }

    override fun type(typeFactory: ProductAddVideoRecommendationAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(videoID)
        parcel.writeString(snippetTitle)
        parcel.writeString(snippetDescription)
        parcel.writeString(snippetChannel)
        parcel.writeString(thumbnailUrl)
        parcel.writeInt(width)
        parcel.writeInt(height)
        parcel.writeString(duration)
        parcel.writeByte(if (chosen) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VideoRecommendationViewModel> {
        override fun createFromParcel(parcel: Parcel): VideoRecommendationViewModel {
            return VideoRecommendationViewModel(parcel)
        }

        override fun newArray(size: Int): Array<VideoRecommendationViewModel?> {
            return arrayOfNulls(size)
        }
    }
}