package com.tokopedia.product.detail.data.model.datamodel.product_detail_info

import android.os.Parcelable
import com.tokopedia.kotlin.model.ImpressHolder
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
    val extParam: String = "",
    val impressionHolder: ImpressHolder = ImpressHolder()
) : Parcelable {

    private val isAppLinkType: Boolean
        get() = type == Content.TYPE_APPLINK && applink.isNotBlank()

    val isClickable: Boolean
        get() = isAppLinkType || type == Content.TYPE_ACTION

    fun routeOnClick(
        navigator: ProductDetailInfoNavigator
    ) {
        when (key) {
            KEY_ETALASE -> navigator.toEtalase(appLink = applink)
            KEY_CATALOG -> navigator.toCatalog(appLink = applink, subTitle = subtitle)
            KEY_CATEGORY -> navigator.toCategory(appLink = applink)
            else -> when (type) { // new code for generalize
                Content.TYPE_ACTION -> actionRouting(navigator = navigator)
                Content.TYPE_APPLINK -> navigator.toAppLink(applink)
                Content.TYPE_WEBLINK -> navigator.toWebView(infoLink)
                else -> {
                    // no ops
                }
            }
        }
    }

    private fun actionRouting(navigator: ProductDetailInfoNavigator) {
        when (action) {
            Content.ACTION_OPEN_DETAIL_PRODUCT -> {
                navigator.toProductDetailInfo(key = key, extParam = extParam)
            }
            Content.ACTION_EMPTY -> {
                // no ops
            }
        }
    }
}
