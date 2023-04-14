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
    val type: Type = Type.DEFAULT,
    val action: Action = Action.NONE,
    val extParam: String = ""
) : Parcelable {

    val isClickable: Boolean
        get() = applink.isNotBlank() || type != Type.DEFAULT

    enum class Type(val value: String) {
        DEFAULT(""),
        ACTION("action")
    }

    enum class Action(val value: String) {
        NONE(""),
        APP_LINK("applink"),
        OPEN_PRODUCT_DETAIL_INFO("open_detail_product")
    }

    fun routeOnClick(
        navigator: ProductDetailInfoNavigator
    ) {
        when (key) {
            KEY_ETALASE -> navigator.toEtalase(appLink = applink)
            KEY_CATALOG -> navigator.toCatalog(appLink = applink, subTitle = subtitle)
            KEY_CATEGORY -> navigator.toCategory(appLink = applink)
            else -> when (type) { // new code for generalize
                Type.ACTION -> routeAction(navigator = navigator)
                else -> {
                    // no ops
                }
            }
        }
    }

    private fun routeAction(navigator: ProductDetailInfoNavigator) {
        when (action) {
            Action.APP_LINK -> {
                navigator.toAppLink(key = key, appLink = applink)
            }
            Action.OPEN_PRODUCT_DETAIL_INFO -> {
                navigator.toProductDetailInfo(key = key, extParam = extParam)
            }
            else -> {
                navigator.toAppLink(key = key, appLink = applink)
            }
        }
    }
}

fun Content.getType() = when (type) {
    ProductDetailInfoContent.Type.ACTION.value -> ProductDetailInfoContent.Type.ACTION
    else -> ProductDetailInfoContent.Type.DEFAULT
}

fun Content.getAction() = when (action) {
    ProductDetailInfoContent.Action.APP_LINK.value -> ProductDetailInfoContent.Action.APP_LINK
    ProductDetailInfoContent.Action.OPEN_PRODUCT_DETAIL_INFO.value -> ProductDetailInfoContent.Action.OPEN_PRODUCT_DETAIL_INFO
    else -> ProductDetailInfoContent.Action.NONE
}
