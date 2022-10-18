package com.tokopedia.search.result.product.addtocart

import android.content.Context
import android.content.Intent
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.addtocart.analytics.AddToCartTracking
import javax.inject.Inject

class AddToCartHandleActivityResult @Inject constructor(
    private val addToCartPresenter: AddToCartPresenter,
    private val addToCartTracking: AddToCartTracking,
    searchParameterProvider: SearchParameterProvider,
): SearchParameterProvider by searchParameterProvider {

    companion object {
        private const val REQUEST_CODE_CHECKOUT = 12382
    }

    fun handleOnActivityResult(context: Context, requestCode: Int, data: Intent?) {
        AtcVariantHelper.onActivityResultAtcVariant(context, requestCode, data) {
            if (this.requestCode == REQUEST_CODE_CHECKOUT) {
                val product = addToCartPresenter.productAddedToCart

                product.let {
                    addToCartTracking.trackItemClick(product)
                    addToCartTracking.trackAddToCartSuccess(product)
                }
            }
        }
    }
}
