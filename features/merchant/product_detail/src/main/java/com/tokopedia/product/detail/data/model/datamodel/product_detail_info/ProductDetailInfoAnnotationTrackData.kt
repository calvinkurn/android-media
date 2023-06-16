package com.tokopedia.product.detail.data.model.datamodel.product_detail_info

import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel

/**
 * Created by yovi.putra on 11/05/23"
 * Project name: tokopedia-app-wg
 **/

data class ProductDetailInfoAnnotationTrackData(
    val parentTrackData: ComponentTrackDataModel? = null,
    val key: String = "",
    val value: String = "",
    val userId: String = ""
)
