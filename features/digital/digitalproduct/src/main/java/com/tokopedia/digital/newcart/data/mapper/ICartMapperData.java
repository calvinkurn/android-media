package com.tokopedia.digital.newcart.data.mapper;

import com.tokopedia.common_digital.cart.data.entity.response.ResponseCheckoutData;
import com.tokopedia.digital.newcart.data.entity.response.voucher.ResponseVoucherData;
import com.tokopedia.digital.newcart.domain.model.CheckoutDigitalData;
import com.tokopedia.digital.newcart.domain.model.VoucherDigital;
import com.tokopedia.digital.exception.MapperDataException;

/**
 * @author anggaprasetiyo on 3/2/17.
 */

public interface ICartMapperData {

    VoucherDigital transformVoucherDigitalData(
            ResponseVoucherData responseVoucherData
    ) throws MapperDataException;

    CheckoutDigitalData transformCheckoutData(
            ResponseCheckoutData responseCheckoutData
    ) throws MapperDataException;
}
