package com.tokopedia.product.addedit.description.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.addedit.description.adapter.VideoLinkTypeFactory

data class VideoLinkModel(
        var inputId : Int = 0,
        var inputName : String = "",
        var inputImage : String = "") : Visitable<VideoLinkTypeFactory>{
    override fun type(typeFactory: VideoLinkTypeFactory): Int = typeFactory.type(this)
}