package com.tokopedia.product.detail.data.model.datamodel.product_detail_info

import android.os.Parcelable
import com.tokopedia.product.detail.common.data.model.pdplayout.Content
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

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
    val type: @RawValue Type = Type.Default,
    val action: @RawValue Action = Action.None
) : Parcelable {

    // should be legacy code, currently this keys need for tracking
    companion object {
        const val KEY_ETALASE = "etalase"
        const val KEY_CATEGORY = "kategori"
        const val KEY_CATALOG = "katalog"
    }

    sealed interface Type {
        object Action : Type
        object Default : Type
    }

    sealed interface Action {
        object None : Action
        data class AppLink(val appLink: String) : Action
        data class OpenProductDetailInfo(val extParam: String) : Action
    }

    fun routeOnClick(
        navigator: ProductDetailInfoNavigator
    ) {
        when (key) {
            KEY_ETALASE -> navigator.toEtalase(appLink = applink)
            KEY_CATALOG -> navigator.toCatalog(appLink = applink, subTitle = subtitle)
            KEY_CATEGORY -> navigator.toCategory(appLink = applink)
            else -> when (type) { // new code for generalize
                is Type.Action -> routeAction(navigator = navigator)
                else -> {
                    // no ops
                }
            }
        }
    }

    private fun routeAction(navigator: ProductDetailInfoNavigator) {
        when (action) {
            is Action.AppLink -> {
                navigator.toAppLink(key = key, appLink = action.appLink)
            }
            is Action.OpenProductDetailInfo -> {
                navigator.toProductDetailInfo(key = key, extParam = action.extParam)
            }
            else -> {
                navigator.toAppLink(key = key, appLink = applink)
            }
        }
    }
}

fun Content.getType() = when (type) {
    Content.TYPE_ACTION -> ProductDetailInfoContent.Type.Action
    else -> ProductDetailInfoContent.Type.Default
}

fun Content.getAction() = when (action) {
    Content.ACTION_APPLINK -> ProductDetailInfoContent.Action.AppLink(
        appLink = applink
    )
    Content.ACTION_OPEN_DETAIL_PRODUCT -> ProductDetailInfoContent.Action.OpenProductDetailInfo(
        extParam = extParam
    )
    else -> ProductDetailInfoContent.Action.None
}
