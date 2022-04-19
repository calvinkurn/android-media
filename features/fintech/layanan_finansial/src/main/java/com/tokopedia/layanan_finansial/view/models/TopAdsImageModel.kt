package com.tokopedia.layanan_finansial.view.models

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.layanan_finansial.view.utils.ViewType

data class TopAdsImageModel(
    var bannerId: String? = "",
    var bannerName: String = "",
    var imageUrl: String? = "",
    var imageWidth: Int = 0,
    var imageHeight: Int = 0,
    var adViewUrl: String? = "",
    var adClickUrl: String? = "",
    var nextPageToken: String? = "",
    var applink: String? = "",
    var position: Int = 0,
    var shopId: String = "",
    var currentPage: String = "",
    var kind: String = ""
): Visitable<ViewType> {
    override fun type(typeFactory: ViewType): Int {
        return typeFactory.type(this)
    }
}