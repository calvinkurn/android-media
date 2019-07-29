package com.tokopedia.purchase_platform.express_checkout.domain.model.checkout

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

data class DataModel(
        var applink: String? = null,
        var callbackUrl: String? = null,
        var reflectModel: ReflectModel? = null,
        var redirectParam: String? = null
)