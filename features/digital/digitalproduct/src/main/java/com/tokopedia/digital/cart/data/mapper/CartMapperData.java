package com.tokopedia.digital.cart.data.mapper;

import com.tokopedia.common_digital.cart.data.entity.response.ResponseCheckoutData;
import com.tokopedia.core.otp.data.RequestOtpModel;
import com.tokopedia.core.otp.data.ValidateOtpModel;
import com.tokopedia.digital.cart.data.entity.response.voucher.ResponseVoucherData;
import com.tokopedia.digital.cart.presentation.model.CheckoutDigitalData;
import com.tokopedia.digital.cart.presentation.model.OtpData;
import com.tokopedia.digital.cart.presentation.model.Relation;
import com.tokopedia.digital.cart.presentation.model.RelationData;
import com.tokopedia.digital.cart.presentation.model.VoucherAttributeDigital;
import com.tokopedia.digital.cart.presentation.model.VoucherDigital;
import com.tokopedia.digital.exception.MapperDataException;

/**
 * @author anggaprasetiyo on 3/2/17.
 */

public class CartMapperData implements ICartMapperData {

    @Override
    public VoucherDigital transformVoucherDigitalData(
            ResponseVoucherData responseVoucherData
    ) throws MapperDataException {
        try {
            VoucherDigital voucherDigital = new VoucherDigital();

            voucherDigital.setId(responseVoucherData.getId());
            voucherDigital.setType(responseVoucherData.getType());

            RelationData relationDataCart = new RelationData();
            relationDataCart.setId(
                    responseVoucherData.getRelationships().getCart().getData().getId()
            );
            relationDataCart.setType(
                    responseVoucherData.getRelationships().getCart().getData().getType()
            );

            Relation relationCart = new Relation(relationDataCart);
            voucherDigital.setCart(relationCart);

            VoucherAttributeDigital voucherAttributeDigital = new VoucherAttributeDigital();
            voucherAttributeDigital.setMessage(responseVoucherData.getAttributes().getMessage());
            voucherAttributeDigital.setDiscountAmountPlain(
                    responseVoucherData.getAttributes().getDiscountAmountPlain()
            );
            voucherAttributeDigital.setCashbackAmpountPlain(
                    responseVoucherData.getAttributes().getCashbackAmountPlain()
            );
            voucherAttributeDigital.setUserId(responseVoucherData.getAttributes().getUserId());
            voucherAttributeDigital.setVoucherCode(
                    responseVoucherData.getAttributes().getVoucherCode()
            );

            voucherDigital.setAttributeVoucher(voucherAttributeDigital);

            return voucherDigital;
        } catch (Exception e) {
            throw new MapperDataException(e.getMessage(), e);
        }
    }

    @Override
    public CheckoutDigitalData transformCheckoutData(
            ResponseCheckoutData responseCheckoutData
    ) throws MapperDataException {
        try {
            CheckoutDigitalData checkoutDigitalData = new CheckoutDigitalData();
            checkoutDigitalData.setFailedCallbackUrl(
                    responseCheckoutData.getAttributes().getCallbackUrlFailed()
            );
            checkoutDigitalData.setSuccessCallbackUrl(
                    responseCheckoutData.getAttributes().getCallbackUrlSuccess()
            );
            checkoutDigitalData.setRedirectUrl(
                    responseCheckoutData.getAttributes().getRedirectUrl()
            );
            checkoutDigitalData.setStringQuery(
                    responseCheckoutData.getAttributes().getQueryString()
            );
            checkoutDigitalData.setTransactionId(
                    responseCheckoutData.getAttributes().getParameter().getTransactionId()
            );
            return checkoutDigitalData;
        } catch (Exception e) {
            throw new MapperDataException(e.getMessage(), e);
        }
    }

    @Override
    public OtpData transformOtpData(RequestOtpModel requestOtpModel) throws MapperDataException {
        try {
            OtpData otpData = new OtpData();
            if (requestOtpModel.isSuccess() && requestOtpModel.isResponseSuccess()
                    && requestOtpModel.getRequestOtpData().isSuccess()) {
                otpData.setSuccess(true);
                otpData.setMessage(requestOtpModel.getStatusMessage());
            } else {
                otpData.setSuccess(false);
                otpData.setMessage(requestOtpModel.getErrorMessage());
            }
            return otpData;
        } catch (Exception e) {
            throw new MapperDataException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public OtpData transformOtpData(ValidateOtpModel validateOtpModel) throws MapperDataException {
        try {
            OtpData otpData = new OtpData();
            if (validateOtpModel.isSuccess() && validateOtpModel.isResponseSuccess()
                    && validateOtpModel.getValidateOtpData().isSuccess()) {
                otpData.setSuccess(true);
                otpData.setMessage(validateOtpModel.getStatusMessage());
            } else {
                otpData.setSuccess(false);
                otpData.setMessage(validateOtpModel.getErrorMessage());
            }
            return otpData;
        } catch (Exception e) {
            throw new MapperDataException(e.getMessage(), e.getCause());
        }
    }
}
