package com.tokopedia.product.addedit.tracking

import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.CAT_ADD_PRODUCT_PAGE
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.EVENT_CLICK_ADD_PRODUCT
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.KEY_SCREEN_NAME
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.KEY_SHOP_ID
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendAddProductClick
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendEditProductClick

object ProductEditDescriptionTracking {

    fun clickBack(shopId: String) {
        sendEditProductClick(shopId,"click back on product description page")
    }

    fun clickRemoveVideoLink(shopId: String) {
        sendEditProductClick(shopId,"click remove video link")
    }

    fun clickAddVideoLink(shopId: String) {
        sendEditProductClick(shopId,"click add video link")
    }

    fun clickAddProductVariant(shopId: String) {
        sendEditProductClick(shopId,"click add product variant")
    }

    fun clickHelpVariant(shopId: String) {
        sendEditProductClick(shopId,"click toaster", "determine product variant")
    }

    fun clickContinue(shopId: String) {
        sendEditProductClick(shopId,"click continue on product description page")
    }

    fun clickPlayVideo(shopId: String) {
        sendEditProductClick(shopId,"click play video")
    }

}