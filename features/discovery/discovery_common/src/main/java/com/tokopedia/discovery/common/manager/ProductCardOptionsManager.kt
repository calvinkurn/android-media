@file:JvmName("ProductCardOptionsManager")

package com.tokopedia.discovery.common.manager

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.discovery.common.model.ProductCardOptionsModel

const val PRODUCT_CARD_OPTIONS_MODEL = "product_card_options_model"

const val PRODUCT_CARD_OPTIONS_REQUEST_CODE = 12854

const val PRODUCT_CARD_OPTION_RESULT_PRODUCT = "product_card_option_result_product"

const val PRODUCT_CARD_OPTIONS_RESULT_CODE_WISHLIST = 12855
const val PRODUCT_CARD_OPTIONS_RESULT_CODE_ATC = 12856
const val PRODUCT_CARD_OPTIONS_RESULT_CODE_VISIT_SHOP = 123857
const val PRODUCT_CARD_OPTIONS_RESULT_CODE_SHARE_PRODUCT = 123858

fun showProductCardOptions(activity: Activity, productCardOptionsModel: ProductCardOptionsModel) {
    val intent = getProductCardOptionsIntent(activity, productCardOptionsModel)

    activity.startActivityForResult(intent, PRODUCT_CARD_OPTIONS_REQUEST_CODE)
}

private fun getProductCardOptionsIntent(context: Context, productCardOptionsModel: ProductCardOptionsModel): Intent {
    val intent = RouteManager.getIntent(context, ApplinkConstInternalDiscovery.PRODUCT_CARD_OPTIONS)

    intent.putExtra(PRODUCT_CARD_OPTIONS_MODEL, productCardOptionsModel)

    return intent
}

fun showProductCardOptions(fragment: Fragment, productCardOptionsModel: ProductCardOptionsModel) {
    fragment.context?.let {
        val intent = getProductCardOptionsIntent(it, productCardOptionsModel)

        fragment.startActivityForResult(intent, PRODUCT_CARD_OPTIONS_REQUEST_CODE)
    }
}

@JvmOverloads
fun handleProductCardOptionsActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        wishlistCallback: ProductCardOptionsWishlistCallback? = null,
        addToCartCallback: ProductCardOptionsResult? = null,
        visitShopCallback: ProductCardOptionsResult? = null,
        shareProductCallback: ProductCardOptionsResult? = null
) {
    if (requestCode == PRODUCT_CARD_OPTIONS_REQUEST_CODE) {
        handleRequestFromProductCardOptions(
                resultCode, data,
                wishlistCallback, addToCartCallback, visitShopCallback, shareProductCallback
        )
    }
}

private fun handleRequestFromProductCardOptions(
        resultCode: Int,
        data: Intent?,
        wishlistCallback: ProductCardOptionsWishlistCallback?,
        addToCartCallback: ProductCardOptionsResult?,
        visitShopCallback: ProductCardOptionsResult?,
        shareProductCallback: ProductCardOptionsResult?
) {
    if (data == null) return

    val productCardOptionsModel = data.getParcelableExtra<ProductCardOptionsModel>(PRODUCT_CARD_OPTION_RESULT_PRODUCT) ?: return

    when (resultCode) {
        PRODUCT_CARD_OPTIONS_RESULT_CODE_WISHLIST -> wishlistCallback?.onReceiveWishlistResult(productCardOptionsModel)
        PRODUCT_CARD_OPTIONS_RESULT_CODE_ATC -> addToCartCallback?.onReceiveResult(productCardOptionsModel)
        PRODUCT_CARD_OPTIONS_RESULT_CODE_VISIT_SHOP -> visitShopCallback?.onReceiveResult(productCardOptionsModel)
        PRODUCT_CARD_OPTIONS_RESULT_CODE_SHARE_PRODUCT -> shareProductCallback?.onReceiveResult(productCardOptionsModel)
    }
}

interface ProductCardOptionsWishlistCallback {

    fun onReceiveWishlistResult(productCardOptionsModel: ProductCardOptionsModel)
}

interface ProductCardOptionsResult {

    fun onReceiveResult(productCardOptionsModel: ProductCardOptionsModel)
}