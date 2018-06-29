package com.tokopedia.digital.cart.data.mapper;

import com.tokopedia.core.otp.data.RequestOtpModel;
import com.tokopedia.core.otp.data.ValidateOtpModel;
import com.tokopedia.digital.cart.data.entity.response.ResponseCartData;
import com.tokopedia.digital.cart.data.entity.response.ResponseCheckoutData;
import com.tokopedia.digital.cart.data.entity.response.ResponseInstantCheckoutData;
import com.tokopedia.digital.cart.data.entity.response.ResponseVoucherData;
import com.tokopedia.digital.cart.model.CartDigitalInfoData;
import com.tokopedia.digital.cart.model.CheckoutDigitalData;
import com.tokopedia.digital.cart.model.InstantCheckoutData;
import com.tokopedia.digital.cart.model.OtpData;
import com.tokopedia.digital.cart.model.VoucherDigital;
import com.tokopedia.digital.exception.MapperDataException;

/**
 * @author anggaprasetiyo on 3/2/17.
 */

public interface ICartMapperData {

    CartDigitalInfoData transformCartInfoData(
            ResponseCartData responseCartData
    ) throws MapperDataException;

    VoucherDigital transformVoucherDigitalData(
            ResponseVoucherData responseVoucherData
    ) throws MapperDataException;

    CheckoutDigitalData transformCheckoutData(
            ResponseCheckoutData responseCheckoutData
    ) throws MapperDataException;

    InstantCheckoutData transformInstantCheckoutData(
            ResponseInstantCheckoutData responseCheckoutData
    ) throws MapperDataException;

    OtpData transformOtpData(
            RequestOtpModel requestOtpModel
    ) throws MapperDataException;

    OtpData transformOtpData(
            ValidateOtpModel validateOtpModel
    ) throws MapperDataException;
}
