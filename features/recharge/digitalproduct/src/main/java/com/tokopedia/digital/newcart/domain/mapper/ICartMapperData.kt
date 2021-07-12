package com.tokopedia.digital.newcart.domain.mapper

import com.tokopedia.digital.common.exception.MapperDataException
import com.tokopedia.digital.newcart.data.entity.response.cart.ResponseCancelVoucherData
import com.tokopedia.digital.newcart.data.entity.response.cart.ResponseCartData
import com.tokopedia.digital.newcart.data.entity.response.cart.ResponseCheckoutData
import com.tokopedia.digital.newcart.data.entity.response.voucher.ResponseVoucherData
import com.tokopedia.digital.newcart.domain.model.CheckoutDigitalData
import com.tokopedia.digital.newcart.domain.model.VoucherDigital
import com.tokopedia.digital.newcart.presentation.model.cart.CartDigitalInfoData

/**
 * Created by Rizky on 27/08/18.
 */
interface ICartMapperData {

    @Throws(MapperDataException::class)
    fun transformCartInfoData(
            responseCartData: ResponseCartData?
    ): CartDigitalInfoData

    @Throws(MapperDataException::class)
    fun transformCancelVoucherData(
            responseCancelVoucherData: ResponseCancelVoucherData?
    ): Boolean

    @Throws(MapperDataException::class)
    fun transformVoucherDigitalData(
            responseVoucherData: ResponseVoucherData
    ): VoucherDigital?

    @Throws(MapperDataException::class)
    fun transformCheckoutData(
            responseCheckoutData: ResponseCheckoutData
    ): CheckoutDigitalData?
}
