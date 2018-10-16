package com.tokopedia.digital.newcart.presentation.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.Cart;
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.Data;
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.Relationships;
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.common_digital.cart.view.model.cart.CartAutoApplyVoucher;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.common_digital.cart.view.model.cart.CartItemDigital;
import com.tokopedia.common_digital.cart.view.model.cart.UserInputPriceDigital;
import com.tokopedia.common_digital.cart.view.model.checkout.CheckoutDataParameter;
import com.tokopedia.common_digital.common.constant.DigitalCache;
import com.tokopedia.digital.R;
import com.tokopedia.digital.cart.data.entity.requestbody.voucher.RequestBodyCancelVoucher;
import com.tokopedia.digital.cart.domain.interactor.ICartDigitalInteractor;
import com.tokopedia.digital.cart.domain.usecase.DigitalCheckoutUseCase;
import com.tokopedia.digital.cart.presentation.model.CheckoutDigitalData;
import com.tokopedia.digital.cart.presentation.model.VoucherAttributeDigital;
import com.tokopedia.digital.cart.presentation.model.VoucherDigital;
import com.tokopedia.digital.common.router.DigitalModuleRouter;
import com.tokopedia.digital.common.util.DigitalAnalytics;
import com.tokopedia.digital.newcart.presentation.contract.DigitalBaseContract;
import com.tokopedia.digital.utils.DeviceUtil;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

public abstract class DigitalBaseCartPresenter<T extends DigitalBaseContract.View> extends BaseDaggerPresenter<T>
        implements DigitalBaseContract.Presenter<T> {
    private final int COUPON_ACTIVE = 1;
    private DigitalAnalytics digitalAnalytics;
    private DigitalModuleRouter digitalModuleRouter;
    private ICartDigitalInteractor cartDigitalInteractor;
    private UserSession userSession;
    private DigitalCheckoutUseCase digitalCheckoutUseCase;

    public DigitalBaseCartPresenter(DigitalAnalytics digitalAnalytics,
                                    DigitalModuleRouter digitalModuleRouter,
                                    ICartDigitalInteractor cartDigitalInteractor,
                                    UserSession userSession,
                                    DigitalCheckoutUseCase digitalCheckoutUseCase) {
        this.digitalAnalytics = digitalAnalytics;
        this.digitalModuleRouter = digitalModuleRouter;
        this.cartDigitalInteractor = cartDigitalInteractor;
        this.userSession = userSession;
        this.digitalCheckoutUseCase = digitalCheckoutUseCase;
    }

    @Override
    public void onViewCreated() {
        getView().setToolbarTitle(R.string.digital_cart_default_toolbar_title);
        CartDigitalInfoData cartDigitalInfoData = getView().getCartInfoData();
        if (cartDigitalInfoData.getAttributes().isEnableVoucher()) {
            getView().showHachikoCart();
            if (cartDigitalInfoData.getAttributes().isCouponActive() == COUPON_ACTIVE) {
                getView().setHachikoPromoAndCouponLabel();
            } else {
                getView().setHachikoPromoLabelOnly();
            }
        } else {
            getView().hideHachikoCart();
        }

        digitalAnalytics.eventClickVoucher(cartDigitalInfoData.getAttributes().getCategoryName(),
                cartDigitalInfoData.getAttributes().getVoucherAutoCode(),
                cartDigitalInfoData.getAttributes().getOperatorName()
        );

        if (cartDigitalInfoData.getAttributes().isEnableVoucher() &&
                cartDigitalInfoData.getAttributes().getAutoApplyVoucher() != null &&
                cartDigitalInfoData.getAttributes().getAutoApplyVoucher().isSuccess()) {
            if (!(cartDigitalInfoData.getAttributes().isCouponActive() == 0 && cartDigitalInfoData.getAttributes().getAutoApplyVoucher().isCoupon() == 1)) {
                CartAutoApplyVoucher cartAutoApplyVoucher = cartDigitalInfoData.getAttributes().getAutoApplyVoucher();
                VoucherDigital voucherDigital = new VoucherDigital();
                VoucherAttributeDigital voucherAttributeDigital = new VoucherAttributeDigital();
                voucherAttributeDigital.setVoucherCode(cartAutoApplyVoucher.getCode());
                voucherAttributeDigital.setDiscountAmountPlain(cartAutoApplyVoucher.getDiscountAmount());
                voucherAttributeDigital.setMessage(cartAutoApplyVoucher.getMessageSuccess());
                voucherAttributeDigital.setIsCoupon(cartAutoApplyVoucher.isCoupon());
                voucherAttributeDigital.setTitle(cartAutoApplyVoucher.getTitle());
                voucherDigital.setAttributeVoucher(voucherAttributeDigital);
                renderCouponAndVoucher(voucherDigital);
            }
        }

        getView().renderCategory(cartDigitalInfoData.getAttributes().getCategoryName());
        List<CartItemDigital> mainInfos = cartDigitalInfoData.getMainInfo();
        CartItemDigital operatorItem = new CartItemDigital("Jenis Layanan", cartDigitalInfoData.getAttributes().getOperatorName());
        mainInfos.add(0, operatorItem);
        getView().renderDetailMainInfo(mainInfos);
        getView().renderAdditionalInfo(cartDigitalInfoData.getAdditionalInfos());

        renderDataInputPrice(String.valueOf(cartDigitalInfoData.getAttributes().getPricePlain()),
                cartDigitalInfoData.getAttributes().getUserInputPrice());

        getView().renderCheckoutView(cartDigitalInfoData.getAttributes().getPrice(),
                cartDigitalInfoData.getAttributes().getPrice(),
                cartDigitalInfoData.getAttributes().getPricePlain());


        // TODO : this
//        presenter.sendAnalyticsATCSuccess(cartDigitalInfoData);
//
//        sendGTMAnalytics(
//                cartDigitalInfoData.getAttributes().getCategoryName(),
//                cartDigitalInfoData.getAttributes().getOperatorName()
//                        + " - " + cartDigitalInfoData.getAttributes().getPricePlain(),
//                cartDigitalInfoData.isInstantCheckout()
//        );


        branchAutoApplyCouponIfAvailable();

    }

    private void branchAutoApplyCouponIfAvailable() {
        String savedCoupon = digitalModuleRouter.getBranchAutoApply(getView().getActivity());
        if (savedCoupon != null && savedCoupon.length() > 0) {
            getView().hideContent();
            getView().showLoading();
            Map<String, String> param = new HashMap<>();
            param.put("voucher_code", savedCoupon);
            param.put("category_id", getView().getCheckoutPassData().getCategoryId());
            cartDigitalInteractor.checkVoucher(
                    getView().getGeneratedAuthParamNetwork(
                            userSession.getUserId(),
                            userSession.getDeviceId(),
                            param),
                    getSubscriberCheckVoucher()
            );
        }
    }


    @NonNull
    private Subscriber<VoucherDigital> getSubscriberCheckVoucher() {
        return new Subscriber<VoucherDigital>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().showContent();
                    getView().hideLoading();
                }
            }

            @Override
            public void onNext(VoucherDigital voucherDigital) {
                getView().hideLoading();
                getView().showContent();
                renderCouponAndVoucher(voucherDigital);
            }
        };
    }



    @NonNull
    private Subscriber<CheckoutDigitalData> getSubscriberCheckout() {
        return new Subscriber<CheckoutDigitalData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().showContent();
                    getView().hideLoading();
                }
            }

            @Override
            public void onNext(CheckoutDigitalData checkoutDigitalData) {
                getView().hideLoading();
                getView().renderToTopPay(checkoutDigitalData);
            }
        };
    }

    private void renderDataInputPrice(String total, UserInputPriceDigital userInputPriceDigital) {
        if (userInputPriceDigital != null) {
            getView().getCheckoutDataParameter().transactionAmount(0);
            getView().renderInputPrice(total, userInputPriceDigital);
        }
    }

    private void renderCouponAndVoucher(VoucherDigital voucherDigital) {
        if (voucherDigital.getAttributeVoucher().getIsCoupon() == 1) {
            renderCouponInfoData(voucherDigital);
        } else {
            renderVoucherInfoData(voucherDigital);
        }
    }

    private void renderVoucherInfoData(VoucherDigital voucherDigital) {
        getView().setHachikoVoucher(
                voucherDigital.getAttributeVoucher().getVoucherCode(),
                voucherDigital.getAttributeVoucher().getMessage());
        if (voucherDigital.getAttributeVoucher().getDiscountAmountPlain() > 0) {
            getView().enableVoucherDiscount(
                    voucherDigital.getAttributeVoucher().getDiscountAmountPlain()
            );
        }
    }

    private void renderCouponInfoData(VoucherDigital voucherDigital) {
        getView().setHachikoCoupon(
                voucherDigital.getAttributeVoucher().getTitle(),
                voucherDigital.getAttributeVoucher().getMessage(),
                voucherDigital.getAttributeVoucher().getVoucherCode()
        );
        if (voucherDigital.getAttributeVoucher().getDiscountAmountPlain() > 0) {
            getView().enableVoucherDiscount(voucherDigital.getAttributeVoucher().getDiscountAmountPlain());
        }
    }

    @Override
    public void onUseVoucherButtonClicked() {
        CartDigitalInfoData cartDigitalInfoData = getView().getCartInfoData();
        if (cartDigitalInfoData.getAttributes().isEnableVoucher()) {
            DigitalCheckoutPassData passData = getView().getCheckoutPassData();
            if (cartDigitalInfoData.getAttributes().isCouponActive() == COUPON_ACTIVE) {
                if (cartDigitalInfoData.getAttributes().getDefaultPromoTab() != null &&
                        cartDigitalInfoData.getAttributes().getDefaultPromoTab().equalsIgnoreCase(
                                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_STATE)) {
                    getView().navigateToCouponActiveAndSelected(passData.getCategoryId());
                } else {
                    getView().navigateToCouponActive(passData.getCategoryId());
                }
            } else {
                getView().navigateToCouponNotActive(passData.getCategoryId());
            }
        } else {
            getView().hideHachikoCart();
        }
    }

    @Override
    public void onReceiveVoucherCode(String code, String message, long discount, int isCoupon) {
        VoucherDigital voucherDigital = new VoucherDigital();
        VoucherAttributeDigital voucherAttributeDigital = new VoucherAttributeDigital();
        voucherAttributeDigital.setVoucherCode(code);
        voucherAttributeDigital.setDiscountAmountPlain(discount);
        voucherAttributeDigital.setMessage(message);
        voucherAttributeDigital.setIsCoupon(isCoupon);
        voucherDigital.setAttributeVoucher(voucherAttributeDigital);
        renderCouponAndVoucher(voucherDigital);
    }

    @Override
    public void onReceiveCoupon(String couponTitle, String couponMessage, String couponCode, long couponDiscountAmount, int isCoupon) {
        VoucherDigital voucherDigital = new VoucherDigital();
        VoucherAttributeDigital voucherAttributeDigital = new VoucherAttributeDigital();
        voucherAttributeDigital.setIsCoupon(isCoupon);
        voucherAttributeDigital.setTitle(couponTitle);
        voucherAttributeDigital.setVoucherCode(couponCode);
        voucherAttributeDigital.setDiscountAmountPlain(couponDiscountAmount);
        voucherAttributeDigital.setMessage(couponMessage);
        voucherDigital.setAttributeVoucher(voucherAttributeDigital);
        renderCouponAndVoucher(voucherDigital);
    }

    @Override
    public void onClearVoucher() {
        getView().disableVoucherCheckoutDiscount();
        RequestBodyCancelVoucher requestBodyCancelVoucher = new RequestBodyCancelVoucher();
        com.tokopedia.digital.cart.data.entity.requestbody.voucher.Attributes attributes = new com.tokopedia.digital.cart.data.entity.requestbody.voucher.Attributes();
        attributes.setIdentifier(getView().getDigitalIdentifierParam());
        requestBodyCancelVoucher.setAttributes(attributes);
        cartDigitalInteractor.cancelVoucher(requestBodyCancelVoucher, new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(String s) {

            }
        });
    }

    @Override
    public void processToCheckout() {
        CheckoutDataParameter checkoutData = getView().getCheckoutDataParameter().build();
        /*if (checkoutData.isNeedOtp()) {
            startOTPProcess();
            return;
        }*/
        getView().hideContent();
        getView().showLoading();
        RequestParams requestParams = digitalCheckoutUseCase.createRequestParams(getRequestBodyCheckout(checkoutData));
        digitalCheckoutUseCase.execute(
                requestParams,
                getSubscriberCheckout()
        );
    }


    @NonNull
    private RequestBodyCheckout getRequestBodyCheckout(CheckoutDataParameter checkoutData) {
        RequestBodyCheckout requestBodyCheckout = new RequestBodyCheckout();
        requestBodyCheckout.setType("checkout");
        com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.Attributes attributes =
                new com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.Attributes();
        attributes.setVoucherCode(checkoutData.getVoucherCode());
        attributes.setTransactionAmount(checkoutData.getTransactionAmount());
        attributes.setIpAddress(checkoutData.getIpAddress());
        attributes.setUserAgent(checkoutData.getUserAgent());
        attributes.setIdentifier(getView().getDigitalIdentifierParam());
        attributes.setClientId(digitalModuleRouter.getTrackingClientId());
        attributes.setAppsFlyer(DeviceUtil.getAppsFlyerIdentifierParam());
        requestBodyCheckout.setAttributes(attributes);
        requestBodyCheckout.setRelationships(
                new Relationships(new Cart(new Data(
                        checkoutData.getRelationType(), checkoutData.getRelationId()
                )))
        );
        return requestBodyCheckout;
    }

    @Override
    public void onPaymentSuccess(String categoryId) {
        if (categoryId != null && categoryId.length() > 0) {
            digitalModuleRouter.getGlobalCacheManager().delete(DigitalCache.NEW_DIGITAL_CATEGORY_AND_FAV + "/" + categoryId);
        }
    }
}
