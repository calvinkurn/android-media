package com.tokopedia.checkout.data.api

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

class CheckoutApiUrl: CommonPurchaseApiUrl() {

    companion object {

        const val PATH_SAVE_SHIPMENT = "$BASE_PATH$VERSION/save_shipment"

    }

}