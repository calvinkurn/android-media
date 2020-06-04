package com.tokopedia.product.addedit.description.presentation.model

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.addedit.description.presentation.adapter.VideoLinkTypeFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VideoLinkModel(
        var inputUrl : String = "",
        var inputTitle : String = "",
        var inputDescription : String = "",
        var inputImage : String = "",
        var errorMessage : String = "") : Visitable<VideoLinkTypeFactory>, Parcelable {
    override fun type(typeFactory: VideoLinkTypeFactory): Int = typeFactory.type(this)
}