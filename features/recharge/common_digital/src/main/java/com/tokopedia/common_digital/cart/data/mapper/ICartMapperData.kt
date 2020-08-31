package com.tokopedia.common_digital.cart.data.mapper

import com.tokopedia.common_digital.cart.data.entity.response.ResponseCartData
import com.tokopedia.common_digital.cart.data.entity.response.ResponseCheckoutData
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData
import com.tokopedia.common_digital.cart.view.model.checkout.InstantCheckoutData
import com.tokopedia.common_digital.common.MapperDataException

/**
 * Created by Rizky on 27/08/18.
 */
interface ICartMapperData {

    @Throws(MapperDataException::class)
    fun transformCartInfoData(
            responseCartData: ResponseCartData
    ): CartDigitalInfoData

    @Throws(MapperDataException::class)
    fun transformInstantCheckoutData(
            responseCheckoutData: ResponseCheckoutData
    ): InstantCheckoutData

}
