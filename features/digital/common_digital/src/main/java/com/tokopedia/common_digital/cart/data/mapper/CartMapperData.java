package com.tokopedia.common_digital.cart.data.mapper;

import com.tokopedia.common_digital.cart.data.entity.response.AdditionalInfo;
import com.tokopedia.common_digital.cart.data.entity.response.AutoApplyVoucher;
import com.tokopedia.common_digital.cart.data.entity.response.Detail;
import com.tokopedia.common_digital.cart.data.entity.response.MainInfo;
import com.tokopedia.common_digital.cart.data.entity.response.RelationshipsCart;
import com.tokopedia.common_digital.cart.data.entity.response.ResponseCartData;
import com.tokopedia.common_digital.cart.data.entity.response.ResponseCheckoutData;
import com.tokopedia.common_digital.cart.domain.model.PostPaidPopupAttribute;
import com.tokopedia.common_digital.cart.view.model.cart.AttributesDigital;
import com.tokopedia.common_digital.cart.view.model.cart.CartAdditionalInfo;
import com.tokopedia.common_digital.cart.view.model.cart.CartAutoApplyVoucher;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.common_digital.cart.view.model.cart.CartItemDigital;
import com.tokopedia.common_digital.cart.view.model.cart.CrossSellingConfig;
import com.tokopedia.common_digital.cart.view.model.checkout.InstantCheckoutData;
import com.tokopedia.common_digital.cart.view.model.cart.Relation;
import com.tokopedia.common_digital.cart.view.model.cart.RelationData;
import com.tokopedia.common_digital.cart.view.model.cart.Relationships;
import com.tokopedia.common_digital.cart.view.model.cart.UserInputPriceDigital;
import com.tokopedia.common_digital.common.MapperDataException;
import com.tokopedia.common_digital.product.data.response.PostPaidPopup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rizky on 27/08/18.
 */
public class CartMapperData implements ICartMapperData {

    @Override
    public CartDigitalInfoData transformCartInfoData(ResponseCartData responseCartData)
            throws MapperDataException {
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

            if (responseCartData.getAttributes().getAutoApply() != null) {
                AutoApplyVoucher entity = responseCartData.getAttributes().getAutoApply();
                CartAutoApplyVoucher applyVoucher = new CartAutoApplyVoucher();
                applyVoucher.setCode(entity.getCode());
                applyVoucher.setSuccess(entity.isSuccess());
                applyVoucher.setDiscountAmount(entity.getDiscountAmount());
                applyVoucher.setIsCoupon(entity.getIsCoupon());
                applyVoucher.setPromoId(entity.getPromoId());
                applyVoucher.setTitle(entity.getTitle());
                applyVoucher.setMessageSuccess(entity.getMessageSuccess());
                attributesDigital.setAutoApplyVoucher(applyVoucher);
            }

            if (responseCartData.getAttributes().getPostPaidPopUp() != null &&
                    responseCartData.getAttributes().getPostPaidPopUp().getAction() != null &&
                    responseCartData.getAttributes().getPostPaidPopUp().getAction().getConfirmAction() != null) {
                PostPaidPopup postPaidPopup = responseCartData.getAttributes().getPostPaidPopUp();
                PostPaidPopupAttribute postPaidPopupAttribute = new PostPaidPopupAttribute();
                postPaidPopupAttribute.setTitle(postPaidPopup.getTitle());
                postPaidPopupAttribute.setContent(postPaidPopup.getContent());
                postPaidPopupAttribute.setImageUrl(postPaidPopup.getImageUrl());
                postPaidPopupAttribute.setConfirmButtonTitle(postPaidPopup.getAction().getConfirmAction().getTitle());
                attributesDigital.setPostPaidPopupAttribute(postPaidPopupAttribute);
            }

            attributesDigital.setDefaultPromoTab(responseCartData.getAttributes().getDefaultPromoTab());

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

            if (responseCartData.getAttributes().getCrossSellingConfig() != null) {
                CrossSellingConfig crossSellingConfig = new CrossSellingConfig();
                crossSellingConfig.setSkipAble(responseCartData.getAttributes().getCrossSellingConfig().isSkipAble());
                crossSellingConfig.setHeaderTitle(responseCartData.getAttributes().getCrossSellingConfig().getWording().getHeaderTitle());
                crossSellingConfig.setBodyTitle(responseCartData.getAttributes().getCrossSellingConfig().getWording().getBodyTitle());
                crossSellingConfig.setBodyContentBefore(responseCartData.getAttributes().getCrossSellingConfig().getWording().getBodyContentBefore());
                crossSellingConfig.setBodyContentAfter(responseCartData.getAttributes().getCrossSellingConfig().getWording().getBodyContentAfter());
                crossSellingConfig.setCheckoutButtonText(responseCartData.getAttributes().getCrossSellingConfig().getWording().getCheckoutButtonText());
                cartDigitalInfoData.setCrossSellingConfig(crossSellingConfig);
            }

            cartDigitalInfoData.setCrossSellingType(responseCartData.getAttributes().getCrossSellingType());
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
    public InstantCheckoutData transformInstantCheckoutData(
            ResponseCheckoutData responseCheckoutData
    ) throws MapperDataException {
        try {
            InstantCheckoutData checkoutDigitalData = new InstantCheckoutData();
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
            if (responseCheckoutData.getAttributes().getParameter() != null) {
                checkoutDigitalData.setTransactionId(
                        responseCheckoutData.getAttributes().getParameter().getTransactionId()
                );
            }
            return checkoutDigitalData;
        } catch (Exception e) {
            throw new MapperDataException(e.getMessage(), e);
        }
    }

}
