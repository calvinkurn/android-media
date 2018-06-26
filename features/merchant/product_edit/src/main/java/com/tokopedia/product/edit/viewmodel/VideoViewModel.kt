package com.tokopedia.product.edit.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.edit.adapter.ProductAddVideoAdapterTypeFactory

class VideoViewModel : Visitable<ProductAddVideoAdapterTypeFactory>, ProductAddVideoBaseViewModel{

    var videoID: String? = null
    var snippetTitle: String? = null
    var snippetDescription: String? = null
    var thumbnailUrl: String? = null
    var width: Int = 0
    var height: Int = 0


    override fun type(typeFactory: ProductAddVideoAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}