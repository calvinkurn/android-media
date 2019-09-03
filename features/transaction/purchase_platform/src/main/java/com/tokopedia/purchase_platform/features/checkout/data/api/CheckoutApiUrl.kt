package com.tokopedia.purchase_platform.features.checkout.data.api

import com.tokopedia.purchase_platform.common.data.api.CommonPurchaseApiUrl

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

class CheckoutApiUrl: CommonPurchaseApiUrl() {

    companion object {

        const val PATH_SHIPMENT_ADDRESS_FORM_DIRECT = "$BASE_PATH$VERSION/shipment_address_form"
        const val PATH_SHIPMENT_ADDRESS_ONE_CLICK_CHECKOUT = "$PATH_SHIPMENT_ADDRESS_FORM_DIRECT/one_click_shipment"
        const val PATH_SAVE_SHIPMENT = "$BASE_PATH$VERSION/save_shipment"

    }

}