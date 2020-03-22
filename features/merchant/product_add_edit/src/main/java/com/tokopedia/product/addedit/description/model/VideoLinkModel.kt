package com.tokopedia.product.addedit.description.model

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.addedit.description.adapter.VideoLinkTypeFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VideoLinkModel(
        var inputId : Int = 0,
        var inputUrl : String = "",
        var inputImage : String = "") : Visitable<VideoLinkTypeFactory>, Parcelable {
    override fun type(typeFactory: VideoLinkTypeFactory): Int = typeFactory.type(this)
}