package com.tokopedia.product.detail.data.model.datamodel.product_detail_info

/**
 * Created by yovi.putra on 11/04/23"
 * Project name: android-tokopedia-core
 **/

interface ProductDetailInfoNavigator {
    fun toCategory(appLink: String)

    fun toEtalase(appLink: String)

    fun toCatalog(appLink: String, subTitle: String)

    fun toAppLink(appLink: String)

    fun toWebView(infoLink: String)

    fun toProductDetailInfo(key: String, extParam: String)
}
