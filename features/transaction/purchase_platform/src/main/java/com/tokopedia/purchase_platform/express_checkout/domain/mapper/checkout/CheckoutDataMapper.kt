package com.tokopedia.purchase_platform.express_checkout.domain.mapper.checkout

import com.tokopedia.purchase_platform.express_checkout.data.entity.response.checkout.CheckoutResponse
import com.tokopedia.purchase_platform.express_checkout.domain.model.checkout.CheckoutResponseModel


/**
 * Created by Irfan Khoirul on 17/12/18.
 */

interface CheckoutDataMapper {

    fun convertToDomainModel(checkoutResponse: CheckoutResponse): CheckoutResponseModel

}