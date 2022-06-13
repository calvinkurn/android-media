package com.tokopedia.product.addedit.tracking

import com.tokopedia.product.addedit.tracking.ProductVariantTracking.sendCustomVariantTypeEventClick

object CustomVariantTypeTracking {

    fun clickCreateCustomVariant(shopId: String) {
        sendCustomVariantTypeEventClick("click create custom variant", "", false, shopId)
    }

    fun clickSelectVariantRecommendation(shopId: String, variantName: String, isEdit: Boolean) {
        sendCustomVariantTypeEventClick("click select variant recommendation", variantName, isEdit, shopId)
    }

    fun clickSubmitVariantSuccess(shopId: String, variantName: String, isEdit: Boolean) {
        val eventAction =
                if (isEdit) "click save variant - success"
                else "click add variant - success"

        sendCustomVariantTypeEventClick(eventAction, variantName, isEdit, shopId)
    }

    fun clickEditExistingVariant(shopId: String) {
        sendCustomVariantTypeEventClick("click edit existing variant", "", true, shopId)
    }

    fun clickDeleteVariant(shopId: String, variantName: String) {
        sendCustomVariantTypeEventClick("click delete variant", variantName, true, shopId)
    }

    fun clickEditVariant(shopId: String, variantName: String) {
        sendCustomVariantTypeEventClick("click edit variant", variantName, true, shopId)
    }
}