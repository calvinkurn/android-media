package com.tokopedia.digital.newcart.presentation.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.network.constant.ErrorNetMessage;
import com.tokopedia.abstraction.common.network.exception.HttpErrorException;
import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.common_digital.cart.data.entity.requestbody.atc.Attributes;
import com.tokopedia.common_digital.cart.data.entity.requestbody.atc.Field;
import com.tokopedia.common_digital.cart.data.entity.requestbody.atc.RequestBodyAtcDigital;
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.Cart;
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.Data;
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.Relationships;
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout;
import com.tokopedia.common_digital.cart.domain.usecase.DigitalAddToCartUseCase;
import com.tokopedia.common_digital.cart.domain.usecase.DigitalInstantCheckoutUseCase;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.common_digital.cart.view.model.cart.CartAdditionalInfo;
import com.tokopedia.common_digital.cart.view.model.cart.CartAutoApplyVoucher;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.common_digital.cart.view.model.cart.CartItemDigital;
import com.tokopedia.common_digital.cart.view.model.cart.UserInputPriceDigital;
import com.tokopedia.common_digital.cart.view.model.checkout.CheckoutDataParameter;
import com.tokopedia.common_digital.cart.view.model.checkout.InstantCheckoutData;
import com.tokopedia.common_digital.common.constant.DigitalCache;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.digital.R;
import com.tokopedia.digital.cart.data.entity.requestbody.otpcart.RequestBodyOtpSuccess;
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
import com.tokopedia.network.exception.ResponseDataNullException;
import com.tokopedia.network.exception.ResponseErrorException;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
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
    private DigitalAddToCartUseCase digitalAddToCartUseCase;
    private DigitalInstantCheckoutUseCase digitalInstantCheckoutUseCase;

    public DigitalBaseCartPresenter(DigitalAddToCartUseCase digitalAddToCartUseCase,
                                    DigitalAnalytics digitalAnalytics,
                                    DigitalModuleRouter digitalModuleRouter,
                                    ICartDigitalInteractor cartDigitalInteractor,
                                    UserSession userSession,
                                    DigitalCheckoutUseCase digitalCheckoutUseCase,
                                    DigitalInstantCheckoutUseCase digitalInstantCheckoutUseCase) {
        this.digitalAddToCartUseCase = digitalAddToCartUseCase;
        this.digitalAnalytics = digitalAnalytics;
        this.digitalModuleRouter = digitalModuleRouter;
        this.cartDigitalInteractor = cartDigitalInteractor;
        this.userSession = userSession;
        this.digitalCheckoutUseCase = digitalCheckoutUseCase;
        this.digitalInstantCheckoutUseCase = digitalInstantCheckoutUseCase;
    }

    @Override
    public void detachView() {
        digitalAddToCartUseCase.unsubscribe();
        digitalCheckoutUseCase.unsubscribe();
        digitalInstantCheckoutUseCase.unsubscribe();
        super.detachView();
    }

    @Override
    public void onViewCreated() {
        getView().hideContent();
        getView().showLoading();
        RequestParams requestParams = digitalAddToCartUseCase.createRequestParams(
                getRequestBodyAtcDigital(), getView().getIdemPotencyKey());
        digitalAddToCartUseCase.execute(requestParams, getSubscriberAddToCart());
    }


    @NonNull
    private RequestBodyAtcDigital getRequestBodyAtcDigital() {
        RequestBodyAtcDigital requestBodyAtcDigital = new RequestBodyAtcDigital();
        List<Field> fieldList = new ArrayList<>();
        String clientNumber = getView().getClientNumber();
        if (clientNumber != null && !clientNumber.isEmpty()) {
            Field field = new Field();
            field.setName("client_number");
            field.setValue(clientNumber);
            fieldList.add(field);
        }
        Attributes attributes = new Attributes();
        attributes.setDeviceId(5);
        attributes.setInstantCheckout(getView().isInstantCheckout());
        attributes.setIpAddress(DeviceUtil.getLocalIpAddress());
        attributes.setUserAgent(DeviceUtil.getUserAgentForApiCall());
        attributes.setUserId(Integer.parseInt(userSession.getUserId()));
        attributes.setProductId(getView().getProductId());
        attributes.setFields(fieldList);
        if (GlobalConfig.isSellerApp()) {
            attributes.setReseller(true);
        }
        attributes.setIdentifier(getView().getDigitalIdentifierParam());
        attributes.setShowSubscribeFlag(true);
        attributes.setThankyouNative(true);
        attributes.setThankyouNativeNew(true);
        requestBodyAtcDigital.setType("add_cart");
        requestBodyAtcDigital.setAttributes(attributes);
        return requestBodyAtcDigital;
    }

    private Subscriber<CartDigitalInfoData> getSubscriberAddToCart() {
        return new Subscriber<CartDigitalInfoData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    getView().closeViewWithMessageAlert(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                    getView().closeViewWithMessageAlert(
                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                    );
                } else if (e instanceof ResponseErrorException) {
                    getView().closeViewWithMessageAlert(e.getMessage());
                } else if (e instanceof ResponseDataNullException) {
                    getView().closeViewWithMessageAlert(e.getMessage());
                } else if (e instanceof HttpErrorException) {
                    getView().closeViewWithMessageAlert(e.getMessage());
                } else {
                    getView().closeViewWithMessageAlert(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(CartDigitalInfoData cartDigitalInfoData) {
                getView().setCartDigitalInfo(cartDigitalInfoData);
                getView().setCheckoutParameter(buildCheckoutData(cartDigitalInfoData, userSession.getAccessToken()));
                if (cartDigitalInfoData.getAttributes().isNeedOtp()) {
                    getView().showContent();
                    getView().hideLoading();
                    getView().interruptRequestTokenVerification(userSession.getPhoneNumber());
                } else {
                    renderCart(cartDigitalInfoData);
                }
            }
        };
    }


    private void renderCart(CartDigitalInfoData cartDigitalInfoData) {
        if (getView().getCartPassData().getInstantCheckout().equals("1") && !cartDigitalInfoData.isForceRenderCart()) {
            processToInstantCheckout();
        } else {
            switch (cartDigitalInfoData.getCrossSellingType()) {
                case 1:
                    getView().inflateDealsPage(cartDigitalInfoData, getView().getCartPassData());
                    break;
                default:
                    getView().showContent();
                    getView().hideLoading();
                    renderBaseCart(cartDigitalInfoData);
                    break;
            }
        }
    }


    private void processToInstantCheckout() {
        CheckoutDataParameter checkoutData = getView().getCheckoutData();
        if (checkoutData.isNeedOtp()) {
            getView().interruptRequestTokenVerification(userSession.getPhoneNumber());
            return;
        }
        RequestParams requestParams = digitalInstantCheckoutUseCase.createRequestParams(getRequestBodyCheckout(checkoutData));
        digitalInstantCheckoutUseCase.execute(requestParams, getSubscriberInstantCheckout());
    }


    protected void renderBaseCart(CartDigitalInfoData cartDigitalInfoData) {
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

        getView().renderCategory(cartDigitalInfoData.getAttributes().getCategoryName());
        List<CartItemDigital> mainInfos = cartDigitalInfoData.getMainInfo();
        CartItemDigital operatorItem = new CartItemDigital(getView().getString(R.string.digital_cart_type_service_label), cartDigitalInfoData.getAttributes().getOperatorName());
        mainInfos.add(0, operatorItem);
        getView().renderDetailMainInfo(mainInfos);
        getView().renderAdditionalInfo(new ArrayList<>(cartDigitalInfoData.getAdditionalInfos()));

        renderDataInputPrice(String.valueOf(cartDigitalInfoData.getAttributes().getPricePlain()),
                cartDigitalInfoData.getAttributes().getUserInputPrice());

        getView().renderCheckoutView(
                cartDigitalInfoData.getAttributes().getPricePlain());

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

        branchAutoApplyCouponIfAvailable();
    }

    private void branchAutoApplyCouponIfAvailable() {
        String savedCoupon = digitalModuleRouter.getBranchAutoApply(getView().getActivity());
        if (savedCoupon != null && savedCoupon.length() > 0) {
            getView().hideContent();
            getView().showLoading();
            Map<String, String> param = new HashMap<>();
            param.put("voucher_code", savedCoupon);
            param.put("category_id", getView().getCartPassData().getCategoryId());
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

    protected void renderCouponAndVoucher(VoucherDigital voucherDigital) {
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
        renderIfHasDiscount(voucherDigital);
    }

    protected void renderIfHasDiscount(VoucherDigital voucherDigital) {
        if (voucherDigital.getAttributeVoucher().getDiscountAmountPlain() > 0) {
            List<CartAdditionalInfo> additionals = new ArrayList<>(getView().getCartInfoData().getAdditionalInfos());
            List<CartItemDigital> items = new ArrayList<>();
            items.add(new CartItemDigital(getView().getString(R.string.digital_cart_additional_payment_cost_label), getView().getCartInfoData().getAttributes().getPrice()));
            items.add(new CartItemDigital(getView().getString(R.string.digital_cart_additional_payment_promo_label), String.format("-%s", getStringIdrFormat((double) voucherDigital.getAttributeVoucher().getDiscountAmountPlain()))));
            long totalPayment = getView().getCartInfoData().getAttributes().getPricePlain() - voucherDigital.getAttributeVoucher().getDiscountAmountPlain();
            items.add(new CartItemDigital(getView().getString(R.string.digital_cart_additional_payment_total_cost_label), getStringIdrFormat((double) totalPayment)));
            CartAdditionalInfo cartAdditionalInfo = new CartAdditionalInfo(getView().getString(R.string.digital_cart_additional_payment_label), items);
            additionals.add(cartAdditionalInfo);
            getView().renderAdditionalInfo(additionals);
            getView().expandAdditionalInfo();
            getView().enableVoucherDiscount(
                    voucherDigital.getAttributeVoucher().getDiscountAmountPlain()
            );
        }
    }

    protected String getStringIdrFormat(Double value) {
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        kursIndonesia.setMaximumFractionDigits(0);
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp ");
        formatRp.setGroupingSeparator('.');
        formatRp.setMonetaryDecimalSeparator('.');
        formatRp.setDecimalSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);

        return kursIndonesia.format(value).replace(",", ".");
    }

    private void renderCouponInfoData(VoucherDigital voucherDigital) {
        getView().setHachikoCoupon(
                voucherDigital.getAttributeVoucher().getTitle(),
                voucherDigital.getAttributeVoucher().getMessage(),
                voucherDigital.getAttributeVoucher().getVoucherCode()
        );
        renderIfHasDiscount(voucherDigital);
    }

    @Override
    public void onUseVoucherButtonClicked() {
        CartDigitalInfoData cartDigitalInfoData = getView().getCartInfoData();
        if (cartDigitalInfoData.getAttributes().isEnableVoucher()) {
            DigitalCheckoutPassData passData = getView().getCartPassData();
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
        getView().renderAdditionalInfo(new ArrayList<>(getView().getCartInfoData().getAdditionalInfos()));
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
        if (checkoutData.isNeedOtp()) {
            getView().interruptRequestTokenVerification(userSession.getPhoneNumber());
            return;
        }
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
        attributes.setDealsIds(getDealIds());
        attributes.setAppsFlyer(DeviceUtil.getAppsFlyerIdentifierParam());
        requestBodyCheckout.setAttributes(attributes);
        requestBodyCheckout.setRelationships(
                new Relationships(new Cart(new Data(
                        checkoutData.getRelationType(), checkoutData.getRelationId()
                )))
        );
        return requestBodyCheckout;
    }

    protected List<Integer> getDealIds() {
        return new ArrayList<>();
    }

    @Override
    public void onPaymentSuccess(String categoryId) {
        if (categoryId != null && categoryId.length() > 0) {
            digitalModuleRouter.getGlobalCacheManager().delete(DigitalCache.NEW_DIGITAL_CATEGORY_AND_FAV + "/" + categoryId);
        }
    }


    @Override
    public void processPatchOtpCart(String categoryId) {
        CheckoutDataParameter checkoutDataParameter = getView().getCheckoutData();
        RequestBodyOtpSuccess requestBodyOtpSuccess = new RequestBodyOtpSuccess();
        requestBodyOtpSuccess.setType("cart");
        requestBodyOtpSuccess.setId(checkoutDataParameter.getCartId());
        com.tokopedia.digital.cart.data.entity.requestbody.otpcart.Attributes attributes =
                new com.tokopedia.digital.cart.data.entity.requestbody.otpcart.Attributes();
        attributes.setIpAddress(DeviceUtil.getLocalIpAddress());
        attributes.setUserAgent(DeviceUtil.getUserAgentForApiCall());
        attributes.setIdentifier(getView().getDigitalIdentifierParam());
        requestBodyOtpSuccess.setAttributes(attributes);

        Map<String, String> paramGetCart = new HashMap<>();
        paramGetCart.put("category_id", categoryId);
        cartDigitalInteractor.patchCartOtp(
                requestBodyOtpSuccess,
                getView().getGeneratedAuthParamNetwork(userSession.getUserId(), userSession.getDeviceId(), paramGetCart),
                getSubscriberCartInfo()
        );


    }

    @Override
    public void processGetCartDataAfterCheckout(String categoryId) {
        Map<String, String> param = new HashMap<>();
        param.put("category_id", categoryId);
        getView().showLoading();
        getView().hideContent();
        cartDigitalInteractor.getCartInfoData(
                getView().getGeneratedAuthParamNetwork(userSession.getUserId(), userSession.getDeviceId(), param),
                getSubscriberCartInfoAfterCheckout()
        );
    }

    private Subscriber<CartDigitalInfoData> getSubscriberCartInfoAfterCheckout() {
        return new Subscriber<CartDigitalInfoData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException || e instanceof ConnectException) {
                    /* Ini kalau ga ada internet */
                    getView().closeViewWithMessageAlert(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else if (e instanceof SocketTimeoutException) {
                    /* Ini kalau timeout */
                    getView().closeViewWithMessageAlert(
                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                    );
                } else if (e instanceof ResponseErrorException) {
                    /* Ini kalau error dari API kasih message error */
                    getView().closeViewWithMessageAlert(e.getMessage());
                } else if (e instanceof ResponseDataNullException) {
                    /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
                    getView().closeViewWithMessageAlert(e.getMessage());
                } else if (e instanceof HttpErrorException) {
                    /* Ini Http error, misal 403, 500, 404,
                     code http errornya bisa diambil
                     e.getErrorCode */
                    getView().closeViewWithMessageAlert(e.getMessage());
                } else {
                    /* Ini diluar dari segalanya hahahaha */
                    getView().closeViewWithMessageAlert(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(CartDigitalInfoData cartDigitalInfoData) {
                getView().showContent();
                getView().hideLoading();
                cartDigitalInfoData.setForceRenderCart(true);
                getView().setCartDigitalInfo(cartDigitalInfoData);
                getView().setCheckoutParameter(buildCheckoutData(cartDigitalInfoData, userSession.getAccessToken()));
                renderCart(cartDigitalInfoData);
            }
        };
    }

    @NonNull
    private Subscriber<CartDigitalInfoData> getSubscriberCartInfo() {
        return new Subscriber<CartDigitalInfoData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    getView().closeViewWithMessageAlert(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                    getView().closeViewWithMessageAlert(
                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                    );
                } else if (e instanceof ResponseErrorException) {
                    getView().closeViewWithMessageAlert(e.getMessage());
                } else if (e instanceof ResponseDataNullException) {
                    getView().closeViewWithMessageAlert(e.getMessage());
                } else if (e instanceof HttpErrorException) {
                    getView().closeViewWithMessageAlert(e.getMessage());
                } else {
                    getView().closeViewWithMessageAlert(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(CartDigitalInfoData cartDigitalInfoData) {
                getView().setCartDigitalInfo(cartDigitalInfoData);
                getView().setCheckoutParameter(buildCheckoutData(cartDigitalInfoData, userSession.getAccessToken()));
                renderCart(cartDigitalInfoData);
            }
        };
    }

    private Subscriber<InstantCheckoutData> getSubscriberInstantCheckout() {
        return new Subscriber<InstantCheckoutData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException || e instanceof ConnectException) {
                    /* Ini kalau ga ada internet */
                    getView().closeViewWithMessageAlert(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else if (e instanceof SocketTimeoutException) {
                    /* Ini kalau timeout */
                    getView().closeViewWithMessageAlert(
                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                    );
                } else if (e instanceof ResponseErrorException) {
                    /* Ini kalau error dari API kasih message error */
                    getView().renderErrorInstantCheckout(e.getMessage());
                } else if (e instanceof ResponseDataNullException) {
                    /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
                    getView().renderErrorInstantCheckout(e.getMessage());
                } else if (e instanceof HttpErrorException) {
                    /* Ini Http error, misal 403, 500, 404,
                     code http errornya bisa diambil
                     e.getErrorCode */
                    getView().closeViewWithMessageAlert(e.getMessage());
                } else {
                    /* Ini diluar dari segalanya hahahaha */
                    getView().closeViewWithMessageAlert(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(InstantCheckoutData instantCheckoutData) {
                getView().hideContent();
                getView().renderToInstantCheckoutPage(instantCheckoutData);
            }
        };
    }


    protected CheckoutDataParameter.Builder buildCheckoutData(CartDigitalInfoData cartDigitalInfoData, String accessToken) {
        CheckoutDataParameter.Builder builder = new CheckoutDataParameter.Builder();
        builder.cartId(cartDigitalInfoData.getId());
        builder.accessToken(accessToken);
        builder.walletRefreshToken("");
        builder.ipAddress(DeviceUtil.getLocalIpAddress());
        builder.relationId(cartDigitalInfoData.getId());
        builder.relationType(cartDigitalInfoData.getType());
        builder.transactionAmount(cartDigitalInfoData.getAttributes().getPricePlain());
        builder.userAgent(DeviceUtil.getUserAgentForApiCall());
        builder.needOtp(cartDigitalInfoData.isNeedOtp());
        return builder;
    }
}
