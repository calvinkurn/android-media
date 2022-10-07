package com.tokopedia.search.result.product.inspirationlistatc

import android.content.Context
import android.content.Intent
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTrackingUnification
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTrackingUnificationDataMapper
import javax.inject.Inject

class InspirationListAtcActivityResult @Inject constructor(
    private val inspirationListAtcPresenterDelegate: InspirationListAtcPresenterDelegate,
    private val inspirationListAtcView: InspirationListAtcView,
    searchParameterProvider: SearchParameterProvider,
): SearchParameterProvider by searchParameterProvider {

    companion object {
        private const val REQUEST_CODE_CHECKOUT = 12382
    }

    fun handleOnActivityResult(context: Context, requestCode: Int, data: Intent?) {
        AtcVariantHelper.onActivityResultAtcVariant(context, requestCode, data) {
            if (this.requestCode == REQUEST_CODE_CHECKOUT) {
                val product = inspirationListAtcPresenterDelegate.productAddedToCart

                product?.let {
                    val trackingData =
                        InspirationCarouselTrackingUnificationDataMapper.createCarouselTrackingUnificationData(
                            product,
                            getSearchParameter(),
                            this.cartId,
                            product.minOrder.toIntOrZero(),
                        )
                    inspirationListAtcView.trackItemClick(trackingData)
                    inspirationListAtcView.trackAddToCart(trackingData)
                }
            }
        }
    }
}
