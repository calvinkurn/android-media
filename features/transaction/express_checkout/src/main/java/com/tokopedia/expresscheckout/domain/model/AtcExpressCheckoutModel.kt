package com.tokopedia.expresscheckout.domain.model;

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class AtcExpressCheckoutModel(
        var headerModel: HeaderModel? = null,
        var dataModel: DataModel? = null,
        var status: String? = null
)