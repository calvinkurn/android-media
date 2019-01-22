package com.tokopedia.digital.cart.data.mapper;

import com.tokopedia.common_digital.cart.data.entity.response.ResponseCheckoutData;
import com.tokopedia.core.otp.data.RequestOtpModel;
import com.tokopedia.core.otp.data.ValidateOtpModel;
import com.tokopedia.digital.cart.data.entity.response.voucher.ResponseVoucherData;
import com.tokopedia.digital.cart.presentation.model.CheckoutDigitalData;
import com.tokopedia.digital.cart.presentation.model.OtpData;
import com.tokopedia.digital.cart.presentation.model.VoucherDigital;
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

    OtpData transformOtpData(
            RequestOtpModel requestOtpModel
    ) throws MapperDataException;

    OtpData transformOtpData(
            ValidateOtpModel validateOtpModel
    ) throws MapperDataException;
}
