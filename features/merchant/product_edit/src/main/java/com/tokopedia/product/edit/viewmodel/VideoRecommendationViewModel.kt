package com.tokopedia.product.edit.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.edit.adapter.ProductAddVideoAdapterTypeFactory
import com.tokopedia.product.edit.adapter.ProductAddVideoRecommendationAdapterTypeFactory

class VideoRecommendationViewModel : Visitable<ProductAddVideoRecommendationAdapterTypeFactory>, ProductAddVideoRecommendationBaseViewModel{

    var videoID: String? = null
    var snippetTitle: String? = null
    var snippetDescription: String? = null
    var snippetChannel: String? = null
    var thumbnailUrl: String? = null
    var width: Int = 0
    var height: Int = 0
    var duration: String? = null
    var choosen: Boolean = false

    override fun type(typeFactory: ProductAddVideoRecommendationAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}