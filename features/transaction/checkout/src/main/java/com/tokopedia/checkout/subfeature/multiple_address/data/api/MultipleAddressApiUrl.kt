package com.tokopedia.checkout.subfeature.multiple_address.data.api

import com.tokopedia.checkout.data.api.CommonPurchaseApiUrl

/**
 * Created by Irfan Khoirul on 2019-08-30.
 */

class MultipleAddressApiUrl {

    companion object {
        const val PATH_CART_LIST_MULTIPLE_ADDRESS = "${CommonPurchaseApiUrl.BASE_PATH}${CommonPurchaseApiUrl.VERSION}/cart_list/multi_address"
    }
}