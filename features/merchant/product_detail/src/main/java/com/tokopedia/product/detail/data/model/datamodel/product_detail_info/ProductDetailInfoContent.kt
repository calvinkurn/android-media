package com.tokopedia.product.detail.data.model.datamodel.product_detail_info

import android.os.Parcelable
import com.tokopedia.product.detail.common.data.model.pdplayout.Content
import com.tokopedia.product.detail.common.data.model.pdplayout.Content.Companion.KEY_CATALOG
import com.tokopedia.product.detail.common.data.model.pdplayout.Content.Companion.KEY_CATEGORY
import com.tokopedia.product.detail.common.data.model.pdplayout.Content.Companion.KEY_ETALASE
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
    val type: String = "",
    val action: String = "",
    val extParam: String = ""
) : Parcelable {

    val isClickable: Boolean
        get() = applink.isNotBlank() || type != Content.TYPE_DEFAULT

    fun routeOnClick(
        navigator: ProductDetailInfoNavigator
    ) {
        when (key) {
            KEY_ETALASE -> navigator.toEtalase(appLink = applink)
            KEY_CATALOG -> navigator.toCatalog(appLink = applink, subTitle = subtitle)
            KEY_CATEGORY -> navigator.toCategory(appLink = applink)
            else -> when (type) { // new code for generalize
                Content.TYPE_ACTION -> actionRouting(navigator = navigator)
                else -> {
                    // no ops
                }
            }
        }
    }

    private fun actionRouting(navigator: ProductDetailInfoNavigator) {
        when (action) {
            Content.ACTION_APPLINK -> {
                navigator.toAppLink(appLink = applink)
            }
            Content.ACTION_OPEN_DETAIL_PRODUCT -> {
                navigator.toProductDetailInfo(key = key, extParam = extParam)
            }
            else -> {
                navigator.toAppLink(appLink = applink)
            }
        }
    }
}
