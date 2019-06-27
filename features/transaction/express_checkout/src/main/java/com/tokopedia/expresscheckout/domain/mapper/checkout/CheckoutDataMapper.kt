package com.tokopedia.expresscheckout.domain.mapper.checkout

import com.tokopedia.expresscheckout.data.entity.response.checkout.CheckoutResponse
import com.tokopedia.expresscheckout.domain.model.checkout.CheckoutResponseModel

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

interface CheckoutDataMapper {

    fun convertToDomainModel(checkoutResponse: CheckoutResponse): CheckoutResponseModel

}