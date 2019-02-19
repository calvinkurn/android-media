package com.tokopedia.expresscheckout.domain.model.atc;

import com.tokopedia.expresscheckout.domain.model.HeaderModel

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class AtcResponseModel(
        var headerModel: HeaderModel? = null,
        var atcDataModel: AtcDataModel? = null,
        var status: String? = null
)