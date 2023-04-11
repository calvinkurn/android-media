package com.tokopedia.product.detail.data.model.datamodel.product_detail_info

/**
 * Created by yovi.putra on 11/04/23"
 * Project name: android-tokopedia-core
 **/

interface ProductDetailInfoNavigator {

    fun onCategory(appLink: String)

    fun onEtalase(appLink: String)

    fun onCatalog(appLink: String, subTitle: String)

    fun onAppLink(appLink: String)
}
