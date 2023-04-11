package com.tokopedia.product.detail.data.model.datamodel.product_detail_info

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by yovi.putra on 03/09/22"
 * Project name: android-tokopedia-core
 **/

@Parcelize
data class ProductDetailInfoContent(
    val icon: String = "",
    val title: String = "",
    var subtitle: String = "",
    val applink: String = "",
    val infoLink: String = "",
    val showAtFront: Boolean = false,
    val isAnnotation: Boolean = false,
    val showAtBottomSheet: Boolean = true,
    val key: String = "",
    val extParam: String = ""
) : Parcelable {

    companion object {
        const val KEY_ETALASE = "etalase"
        const val KEY_CATEGORY = "kategori"
        const val KEY_CATALOG = "katalog"
    }

    fun routeOnClick(
        navigator: ProductDetailInfoNavigator
    ) {
        when (key) {
            KEY_CATEGORY -> {
                navigator.onCategory(applink)
            }
            KEY_ETALASE -> {
                navigator.onEtalase(applink)
            }
            KEY_CATALOG -> {
                navigator.onCatalog(applink, subtitle)
            }
            else -> {
                navigator.onAppLink(applink)
            }
        }
    }
}
