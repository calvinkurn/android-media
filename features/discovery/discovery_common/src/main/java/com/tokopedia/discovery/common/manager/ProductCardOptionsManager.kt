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

fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?, wishlistCallback: ProductCardOptionsWishlistCallback?) {
    if (requestCode == PRODUCT_CARD_OPTIONS_REQUEST_CODE) {
        handleRequestFromProductCardOptions(resultCode, data, wishlistCallback)
    }
}

private fun handleRequestFromProductCardOptions(resultCode: Int, data: Intent?, wishlistCallback: ProductCardOptionsWishlistCallback?) {
    if (resultCode == Activity.RESULT_OK && data != null) {
        handleProductCardOptionsResultOK(data, wishlistCallback)
    }
}

private fun handleProductCardOptionsResultOK(data: Intent, wishlistCallback: ProductCardOptionsWishlistCallback?) {
    val productCardOptionsModel = data.getParcelableExtra<ProductCardOptionsModel>(PRODUCT_CARD_OPTION_RESULT_PRODUCT)

    if (productCardOptionsModel != null) {
        wishlistCallback?.onReceiveWishlistResult(productCardOptionsModel)
    }
}

interface ProductCardOptionsWishlistCallback {

    fun onReceiveWishlistResult(productCardOptionsModel: ProductCardOptionsModel)
}