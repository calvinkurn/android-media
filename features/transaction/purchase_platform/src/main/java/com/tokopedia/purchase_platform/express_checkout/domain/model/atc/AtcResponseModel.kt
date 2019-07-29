package com.tokopedia.purchase_platform.express_checkout.domain.model.atc;

import com.tokopedia.purchase_platform.express_checkout.domain.model.HeaderModel

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class AtcResponseModel(
        var headerModel: HeaderModel? = null,
        var atcDataModel: AtcDataModel? = null,
        var status: String? = null
)