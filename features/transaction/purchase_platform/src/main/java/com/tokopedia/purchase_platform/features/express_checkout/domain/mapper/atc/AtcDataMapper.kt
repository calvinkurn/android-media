package com.tokopedia.purchase_platform.features.express_checkout.domain.mapper.atc

import com.tokopedia.purchase_platform.features.express_checkout.data.entity.response.atc.AtcResponse
import com.tokopedia.purchase_platform.features.express_checkout.domain.model.atc.AtcResponseModel

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

interface AtcDataMapper {

    fun convertToDomainModel(atcResponse: AtcResponse): AtcResponseModel

}