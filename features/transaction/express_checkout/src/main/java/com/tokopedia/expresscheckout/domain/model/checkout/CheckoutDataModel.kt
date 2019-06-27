package com.tokopedia.expresscheckout.domain.model.checkout

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

data class CheckoutDataModel(
        var success: Int = 0,
        var error: String? = null,
        var errorState: Int = 0,
        var message: String? = null,
        var dataModel: DataModel? = null
)