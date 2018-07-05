package com.tokopedia.product.edit.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.edit.view.adapter.ProductAddVideoAdapterTypeFactory

class VideoViewModel : Visitable<ProductAddVideoAdapterTypeFactory>, ProductAddVideoBaseViewModel{

    var videoID: String? = null
    var snippetTitle: String? = null
    var snippetDescription: String? = null
    var snippetChannel: String? = null
    var thumbnailUrl: String? = null
    var width: Int = 0
    var height: Int = 0
    var duration: String? = null
    var recommendation: Boolean? = false


    override fun type(typeFactory: ProductAddVideoAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}