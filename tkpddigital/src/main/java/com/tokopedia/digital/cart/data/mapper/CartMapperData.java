package com.tokopedia.digital.cart.data.mapper;

import com.tokopedia.core.otp.data.RequestOtpModel;
import com.tokopedia.core.otp.data.ValidateOtpModel;
import com.tokopedia.digital.cart.data.entity.response.AdditionalInfo;
import com.tokopedia.digital.cart.data.entity.response.Detail;
import com.tokopedia.digital.cart.data.entity.response.MainInfo;
import com.tokopedia.digital.cart.data.entity.response.RelationshipsCart;
import com.tokopedia.digital.cart.data.entity.response.ResponseCartData;
import com.tokopedia.digital.cart.data.entity.response.ResponseCheckoutData;
import com.tokopedia.digital.cart.data.entity.response.ResponseInstantCheckoutData;
import com.tokopedia.digital.cart.data.entity.response.ResponseVoucherData;
import com.tokopedia.digital.cart.model.AttributesDigital;
import com.tokopedia.digital.cart.model.CartAdditionalInfo;
import com.tokopedia.digital.cart.model.CartDigitalInfoData;
import com.tokopedia.digital.cart.model.CartItemDigital;
import com.tokopedia.digital.cart.model.CheckoutDigitalData;
import com.tokopedia.digital.cart.model.InstantCheckoutData;
import com.tokopedia.digital.cart.model.OtpData;
import com.tokopedia.digital.cart.model.Relation;
import com.tokopedia.digital.cart.model.RelationData;
import com.tokopedia.digital.cart.model.Relationships;
import com.tokopedia.digital.cart.model.UserInputPriceDigital;
import com.tokopedia.digital.cart.model.VoucherAttributeDigital;
import com.tokopedia.digital.cart.model.VoucherDigital;
import com.tokopedia.digital.exception.MapperDataException;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 3/2/17.
 */

public class CartMapperData implements ICartMapperData {
    @Override
    public CartDigitalInfoData transformCartInfoData(
            ResponseCartData responseCartData
    ) throws MapperDataException {
        try {
            CartDigitalInfoData cartDigitalInfoData = new CartDigitalInfoData();
            List<CartItemDigital> cartItemDigitalList = new ArrayList<>();
            List<CartAdditionalInfo> cartAdditionalInfoList = new ArrayList<>();
            for (MainInfo mainInfo : responseCartData.getAttributes().getMainInfo()) {
                cartItemDigitalList.add(
                        new CartItemDigital(mainInfo.getLabel(), mainInfo.getValue())
                );
            }
            for (AdditionalInfo additionalInfo : responseCartData.getAttributes().getAdditionalInfo()) {
                List<CartItemDigital> cartItemDigitalList1 = new ArrayList<>();
                for (Detail detail : additionalInfo.getDetail()) {
                    cartItemDigitalList1.add(new CartItemDigital(detail.getLabel(), detail.getValue()));
                }
                cartAdditionalInfoList.add(
                        new CartAdditionalInfo(additionalInfo.getTitle(), cartItemDigitalList1)
                );
            }
            AttributesDigital attributesDigital = new AttributesDigital();
            attributesDigital.setCategoryName(responseCartData.getAttributes().getCategoryName());
            attributesDigital.setOperatorName(responseCartData.getAttributes().getOperatorName());
            attributesDigital.setClientNumber(responseCartData.getAttributes().getClientNumber());
            attributesDigital.setIcon(responseCartData.getAttributes().getIcon());
            attributesDigital.setInstantCheckout(responseCartData.getAttributes().isInstantCheckout());
            attributesDigital.setNeedOtp(responseCartData.getAttributes().isNeedOtp());
            attributesDigital.setSmsState(responseCartData.getAttributes().getSmsState());
            attributesDigital.setPrice(responseCartData.getAttributes().getPrice());
            attributesDigital.setPricePlain(responseCartData.getAttributes().getPricePlain());
            attributesDigital.setEnableVoucher(responseCartData.getAttributes().isEnableVoucher());
            attributesDigital.setIsCouponActive(responseCartData.getAttributes().isCouponActive());
            attributesDigital.setVoucherAutoCode(responseCartData.getAttributes().getVoucherAutoCode());
            if (responseCartData.getAttributes().getUserInputPrice() != null) {
                UserInputPriceDigital userInputPriceDigital = new UserInputPriceDigital();
                userInputPriceDigital.setMaxPaymentPlain(
                        responseCartData.getAttributes().getUserInputPrice().getMaxPaymentPlain()
                );
                userInputPriceDigital.setMinPaymentPlain(
                        responseCartData.getAttributes().getUserInputPrice().getMinPaymentPlain()
                );
                userInputPriceDigital.setMinPayment(responseCartData.getAttributes()
                        .getUserInputPrice().getMinPayment());
                userInputPriceDigital.setMaxPayment(responseCartData.getAttributes()
                        .getUserInputPrice().getMaxPayment());
                attributesDigital.setUserInputPrice(userInputPriceDigital);
            }
            attributesDigital.setUserId(responseCartData.getAttributes().getUserId());

            RelationshipsCart relationshipsResponse =
                    responseCartData.getRelationships();


            RelationData relationDataProduct =
                    new RelationData();
            relationDataProduct.setType(relationshipsResponse.getProduct().getData().getType());
            relationDataProduct.setId(relationshipsResponse.getProduct().getData().getId());

            RelationData relationDataCategory =
                    new RelationData();
            relationDataCategory.setType(relationshipsResponse.getCategory().getData().getType());
            relationDataCategory.setId(relationshipsResponse.getCategory().getData().getId());


            RelationData relationDataOperator =
                    new RelationData();
            relationDataOperator.setType(relationshipsResponse.getOperator().getData().getType());
            relationDataOperator.setId(relationshipsResponse.getOperator().getData().getId());


            Relationships relationships = new Relationships();
            relationships.setRelationCategory(new Relation(relationDataCategory));
            relationships.setRelationOperator(new Relation(relationDataOperator));
            relationships.setRelationProduct(new Relation(relationDataProduct));


            cartDigitalInfoData.setAdditionalInfos(cartAdditionalInfoList);
            cartDigitalInfoData.setAttributes(attributesDigital);
            cartDigitalInfoData.setId(responseCartData.getId());
            cartDigitalInfoData.setMainInfo(cartItemDigitalList);
            cartDigitalInfoData.setInstantCheckout(
                    responseCartData.getAttributes().isInstantCheckout()
            );
            cartDigitalInfoData.setNeedOtp(responseCartData.getAttributes().isNeedOtp());
            cartDigitalInfoData.setSmsState(responseCartData.getAttributes().getSmsState());
            cartDigitalInfoData.setTitle(responseCartData.getAttributes().getTitle());
            cartDigitalInfoData.setType(responseCartData.getType());
            cartDigitalInfoData.setRelationships(relationships);

            return cartDigitalInfoData;
        } catch (Exception e) {
            throw new MapperDataException(e.getMessage(), e);
        }
    }

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
    public InstantCheckoutData transformInstantCheckoutData(
            ResponseInstantCheckoutData responseCheckoutData) throws MapperDataException {
        try {
            InstantCheckoutData instantCheckoutData = new InstantCheckoutData();
            instantCheckoutData.setRedirectUrl(
                    responseCheckoutData.getAttributes().getThanksUrl()
            );
            instantCheckoutData.setFailedCallbackUrl(
                    responseCheckoutData.getAttributes().getCallbackUrlFailed()
            );
            instantCheckoutData.setSuccessCallbackUrl(
                    responseCheckoutData.getAttributes().getCallbackUrlSuccess()
            );
            instantCheckoutData.setTransactionId(
                    MessageFormat.format("{0}", responseCheckoutData.getId())
            );
            return instantCheckoutData;
        } catch (Exception e) {
            throw new MapperDataException(e.getMessage(), e.getCause());
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
