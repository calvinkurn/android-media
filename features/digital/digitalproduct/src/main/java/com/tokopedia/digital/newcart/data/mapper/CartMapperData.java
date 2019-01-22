package com.tokopedia.digital.newcart.data.mapper;

import com.tokopedia.common_digital.cart.data.entity.response.ResponseCheckoutData;
import com.tokopedia.digital.newcart.data.entity.response.voucher.ResponseVoucherData;
import com.tokopedia.digital.newcart.domain.model.CheckoutDigitalData;
import com.tokopedia.digital.newcart.domain.model.VoucherAttributeDigital;
import com.tokopedia.digital.newcart.domain.model.VoucherDigital;
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
}
