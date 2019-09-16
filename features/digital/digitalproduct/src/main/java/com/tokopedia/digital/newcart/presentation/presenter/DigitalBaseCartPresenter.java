package com.tokopedia.digital.newcart.presentation.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.network.constant.ErrorNetMessage;
import com.tokopedia.abstraction.common.network.exception.HttpErrorException;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.common_digital.cart.data.entity.requestbody.atc.Attributes;
import com.tokopedia.common_digital.cart.data.entity.requestbody.atc.Field;
import com.tokopedia.common_digital.cart.data.entity.requestbody.atc.RequestBodyAtcDigital;
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.Cart;
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.Data;
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.Relationships;
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout;
import com.tokopedia.common_digital.cart.domain.usecase.DigitalAddToCartUseCase;
import com.tokopedia.common_digital.cart.domain.usecase.DigitalInstantCheckoutUseCase;
import com.tokopedia.common_digital.cart.view.model.cart.CartAdditionalInfo;
import com.tokopedia.common_digital.cart.view.model.cart.CartAutoApplyVoucher;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.common_digital.cart.view.model.cart.CartItemDigital;
import com.tokopedia.common_digital.cart.view.model.cart.UserInputPriceDigital;
import com.tokopedia.common_digital.cart.view.model.checkout.CheckoutDataParameter;
import com.tokopedia.common_digital.cart.view.model.checkout.InstantCheckoutData;
import com.tokopedia.common_digital.common.constant.DigitalCache;
import com.tokopedia.commonpromo.PromoCodeAutoApplyUseCase;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.digital.R;
import com.tokopedia.digital.common.analytic.DigitalAnalytics;
import com.tokopedia.digital.common.data.entity.response.RechargePushEventRecommendationResponseEntity;
import com.tokopedia.digital.common.domain.interactor.RechargePushEventRecommendationUseCase;
import com.tokopedia.digital.common.router.DigitalModuleRouter;
import com.tokopedia.digital.newcart.data.cache.DigitalPostPaidLocalCache;
import com.tokopedia.digital.newcart.data.entity.requestbody.otpcart.RequestBodyOtpSuccess;
import com.tokopedia.digital.newcart.domain.interactor.ICartDigitalInteractor;
import com.tokopedia.digital.newcart.domain.model.CheckoutDigitalData;
import com.tokopedia.digital.newcart.domain.model.VoucherAttributeDigital;
import com.tokopedia.digital.newcart.domain.model.VoucherDigital;
import com.tokopedia.digital.newcart.domain.usecase.DigitalCheckoutUseCase;
import com.tokopedia.digital.newcart.presentation.contract.DigitalBaseContract;
import com.tokopedia.digital.utils.DeviceUtil;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.network.exception.ResponseDataNullException;
import com.tokopedia.network.exception.ResponseErrorException;
import com.tokopedia.track.TrackApp;
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
    private DigitalPostPaidLocalCache digitalPostPaidLocalCache;
    private RechargePushEventRecommendationUseCase rechargePushEventRecommendationUseCase;
    private String PROMO_CODE = "promoCode";
    public static final String KEY_CACHE_PROMO_CODE = "KEY_CACHE_PROMO_CODE";


    public DigitalBaseCartPresenter(DigitalAddToCartUseCase digitalAddToCartUseCase,
                                    DigitalAnalytics digitalAnalytics,
                                    DigitalModuleRouter digitalModuleRouter,
                                    ICartDigitalInteractor cartDigitalInteractor,
                                    UserSession userSession,
                                    DigitalCheckoutUseCase digitalCheckoutUseCase,
                                    DigitalInstantCheckoutUseCase digitalInstantCheckoutUseCase,
                                    DigitalPostPaidLocalCache digitalPostPaidLocalCache,
                                    RechargePushEventRecommendationUseCase rechargePushEventRecommendationUseCase) {
        this.digitalAddToCartUseCase = digitalAddToCartUseCase;
        this.digitalAnalytics = digitalAnalytics;
        this.digitalModuleRouter = digitalModuleRouter;
        this.cartDigitalInteractor = cartDigitalInteractor;
        this.userSession = userSession;
        this.digitalCheckoutUseCase = digitalCheckoutUseCase;
        this.digitalInstantCheckoutUseCase = digitalInstantCheckoutUseCase;
        this.digitalPostPaidLocalCache = digitalPostPaidLocalCache;
        this.rechargePushEventRecommendationUseCase = rechargePushEventRecommendationUseCase;
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
        if (!userSession.isLoggedIn()){
            getView().closeViewWithMessageAlert(getView().getString(R.string.digital_cart_login_message));
        } else {
            getView().hideCartView();
            getView().showFullPageLoading();
            RequestParams requestParams = digitalAddToCartUseCase.createRequestParams(
                    getRequestBodyAtcDigital(), getView().getIdemPotencyKey());
            getView().startPerfomanceMonitoringTrace();
            digitalAddToCartUseCase.execute(requestParams, getSubscriberAddToCart());
        }
    }


    @NonNull
    private RequestBodyAtcDigital getRequestBodyAtcDigital() {
        RequestBodyAtcDigital requestBodyAtcDigital = new RequestBodyAtcDigital();
        List<Field> fieldList = new ArrayList<>();
        String clientNumber = getView().getClientNumber();
        String zoneId = getView().getZoneId();
        if (clientNumber != null && !clientNumber.isEmpty()) {
            Field field = new Field();
            field.setName("client_number");
            field.setValue(clientNumber);
            fieldList.add(field);
        }
        if (zoneId != null && !zoneId.isEmpty()) {
            Field field = new Field();
            field.setName("zone_id");
            field.setValue(zoneId);
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
                if (isViewAttached()) {
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

                    getView().stopPerfomanceMonitoringTrace();
                }
            }

            @Override
            public void onNext(CartDigitalInfoData cartDigitalInfoData) {
                getView().setCartDigitalInfo(cartDigitalInfoData);
                getView().setCheckoutParameter(buildCheckoutData(cartDigitalInfoData, userSession.getAccessToken()));

                digitalAnalytics.eventAddToCart(cartDigitalInfoData, getView().getCartPassData().getSource());
                digitalAnalytics.eventCheckout(cartDigitalInfoData);

                if (cartDigitalInfoData.getAttributes().isNeedOtp()) {
                    getView().showCartView();
                    getView().hideFullPageLoading();
                    getView().interruptRequestTokenVerification(userSession.getPhoneNumber());
                } else {
                    trackRechargePushEventRecommendation(Integer.parseInt(getView().getCartPassData().getCategoryId()));
                    renderCart(cartDigitalInfoData);
                }
            }
        };
    }


    private void renderCart(CartDigitalInfoData cartDigitalInfoData) {
        if (getView().getCartPassData().getInstantCheckout().equals("1") && !cartDigitalInfoData.isForceRenderCart()) {
            processToInstantCheckout();
        } else {
            digitalAnalytics.sendCartScreen(getView().getActivity());

            renderCrossSellingCart(cartDigitalInfoData);
        }
        getView().stopPerfomanceMonitoringTrace();
    }

    protected void renderCrossSellingCart(CartDigitalInfoData cartDigitalInfoData) {
        getView().showCartView();
        getView().hideFullPageLoading();
        renderBaseCart(cartDigitalInfoData);
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
        setPromoTickerVisibility(cartDigitalInfoData);

        renderCartInfo(cartDigitalInfoData);

        renderDataInputPrice(
                String.valueOf(cartDigitalInfoData.getAttributes().getPricePlain()),
                cartDigitalInfoData.getAttributes().getUserInputPrice()
        );

        getView().renderCheckoutView(
                cartDigitalInfoData.getAttributes().getPricePlain()
        );

        renderAutoApplyPromo(cartDigitalInfoData);

        branchAutoApplyCouponIfAvailable();

        renderPostPaidPopUp(cartDigitalInfoData);
    }

    private void renderAutoApplyPromo(CartDigitalInfoData cartDigitalInfoData) {
        if (cartDigitalInfoData.getAttributes().isEnableVoucher() &&
                cartDigitalInfoData.getAttributes().getAutoApplyVoucher() != null &&
                cartDigitalInfoData.getAttributes().getAutoApplyVoucher().isSuccess()) {
            if (!(cartDigitalInfoData.getAttributes().isCouponActive() == 0 && cartDigitalInfoData.getAttributes().getAutoApplyVoucher().isCoupon() == 1)) {
                CartAutoApplyVoucher cartAutoApplyVoucher = cartDigitalInfoData.getAttributes().getAutoApplyVoucher();
                getView().onAutoApplyPromo(
                        cartAutoApplyVoucher.getTitle(),
                        cartAutoApplyVoucher.getMessageSuccess(),
                        cartAutoApplyVoucher.getCode(),
                        cartAutoApplyVoucher.isCoupon()
                );
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
    }

    private void renderCartInfo(CartDigitalInfoData cartDigitalInfoData) {
        getView().renderCategoryInfo(cartDigitalInfoData.getAttributes().getCategoryName());
        getView().renderDetailMainInfo(cartDigitalInfoData.getMainInfo());
        getView().renderAdditionalInfo(new ArrayList<>(cartDigitalInfoData.getAdditionalInfos()));
    }

    private void setPromoTickerVisibility(CartDigitalInfoData cartDigitalInfoData) {
        if (cartDigitalInfoData.getAttributes().isEnableVoucher()) {
            getView().renderPromoTicker();
        } else {
            getView().hidePromoTicker();
        }
    }

    private void renderPostPaidPopUp(CartDigitalInfoData cartDigitalInfoData) {
        if (!getView().isAlreadyShowPostPaid()
                && cartDigitalInfoData.getAttributes().getPostPaidPopupAttribute() != null
                && !digitalPostPaidLocalCache.isAlreadyShowPostPaidPopUp(userSession.getUserId())) {
            getView().showPostPaidDialog(
                    cartDigitalInfoData.getAttributes().getPostPaidPopupAttribute().getTitle(),
                    cartDigitalInfoData.getAttributes().getPostPaidPopupAttribute().getContent(),
                    cartDigitalInfoData.getAttributes().getPostPaidPopupAttribute().getConfirmButtonTitle(),
                    userSession.getUserId()
            );
        }
    }

    private void branchAutoApplyCouponIfAvailable() {
        String savedCoupon = PersistentCacheManager.instance.getString(KEY_CACHE_PROMO_CODE, "");
        applyPromoCode(savedCoupon);
        if (savedCoupon != null && savedCoupon.length() > 0) {
            getView().hideCartView();
            getView().showFullPageLoading();
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

    private void applyPromoCode(String promoCode){
        PromoCodeAutoApplyUseCase promoCodeAutoApplyUseCase = new PromoCodeAutoApplyUseCase(getView().getActivity());
        com.tokopedia.usecase.RequestParams requestParams = com.tokopedia.usecase.RequestParams.create();
        requestParams.putString(PROMO_CODE, promoCode);

        promoCodeAutoApplyUseCase.createObservable(requestParams);
        promoCodeAutoApplyUseCase.execute(null);
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
                    getView().showCartView();
                    getView().hideFullPageLoading();
                }
            }

            @Override
            public void onNext(VoucherDigital voucherDigital) {
                getView().hideFullPageLoading();
                getView().showCartView();

                VoucherAttributeDigital voucherAttributeDigital = voucherDigital.getAttributeVoucher();
                getView().onAutoApplyPromo(
                        voucherAttributeDigital.getTitle(),
                        voucherAttributeDigital.getMessage(),
                        voucherAttributeDigital.getVoucherCode(),
                        voucherAttributeDigital.getIsCoupon()
                );
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
                    getView().showCartView();
                    getView().hideFullPageLoading();
                }
            }

            @Override
            public void onNext(CheckoutDigitalData checkoutDigitalData) {
                getView().hideFullPageLoading();
                getView().renderToTopPay(checkoutDigitalData);
                digitalAnalytics.eventProceedToPayment(getView().getCartInfoData(), getView().getCheckoutData().getVoucherCode());
            }
        };
    }

    private void renderDataInputPrice(String total, UserInputPriceDigital userInputPriceDigital) {
        if (userInputPriceDigital != null) {
            getView().getCheckoutDataParameter().transactionAmount(0);
            getView().renderInputPriceView(total, userInputPriceDigital);
        }
    }

    protected void renderCouponAndVoucher(VoucherDigital voucherDigital) {
        getView().renderPromo();
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

    @Override
    public void onReceivePromoCode(String couponTitle, String couponMessage, String couponCode, int isCoupon) {
        if (isViewAttached()){
            VoucherDigital voucherDigital = new VoucherDigital();
            VoucherAttributeDigital voucherAttributeDigital = new VoucherAttributeDigital();
            voucherAttributeDigital.setIsCoupon(isCoupon);
            voucherAttributeDigital.setTitle(couponTitle);
            voucherAttributeDigital.setVoucherCode(couponCode);
            voucherAttributeDigital.setMessage(couponMessage);
            voucherDigital.setAttributeVoucher(voucherAttributeDigital);
            renderCouponAndVoucher(voucherDigital);
        }
    }

    @Override
    public void processToCheckout() {
        CheckoutDataParameter checkoutData = getView().getCheckoutDataParameter().build();
        if (checkoutData.isNeedOtp()) {
            getView().interruptRequestTokenVerification(userSession.getPhoneNumber());
            return;
        }
        getView().hideCartView();
        getView().showFullPageLoading();
        RequestParams requestParams = digitalCheckoutUseCase.createRequestParams(getRequestBodyCheckout(checkoutData));
        digitalCheckoutUseCase.execute(
                requestParams,
                getSubscriberCheckout()
        );
    }


    @NonNull
    protected RequestBodyCheckout getRequestBodyCheckout(CheckoutDataParameter checkoutData) {
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
        attributes.setAppsFlyer(DeviceUtil.getAppsFlyerIdentifierParam(
                TrackApp.getInstance().getAppsFlyer().getUniqueId(),
                ""));
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
            PersistentCacheManager.instance.delete(DigitalCache.INSTANCE.getNEW_DIGITAL_CATEGORY_AND_FAV() + "/" + categoryId);
        }
    }


    @Override
    public void processPatchOtpCart(String categoryId) {
        CheckoutDataParameter checkoutDataParameter = getView().getCheckoutData();
        RequestBodyOtpSuccess requestBodyOtpSuccess = new RequestBodyOtpSuccess();
        requestBodyOtpSuccess.setType("cart");
        requestBodyOtpSuccess.setId(checkoutDataParameter.getCartId());
        com.tokopedia.digital.newcart.data.entity.requestbody.otpcart.Attributes attributes =
                new com.tokopedia.digital.newcart.data.entity.requestbody.otpcart.Attributes();
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
        getView().showFullPageLoading();
        getView().hideCartView();
        getView().startPerfomanceMonitoringTrace();
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
                if (isViewAttached()) {
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
            }

            @Override
            public void onNext(CartDigitalInfoData cartDigitalInfoData) {
                getView().showCartView();
                getView().hideFullPageLoading();
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
                if (isViewAttached()) {
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
                if (isViewAttached()) {
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
            }

            @Override
            public void onNext(InstantCheckoutData instantCheckoutData) {
                getView().hideCartView();
                getView().renderToInstantCheckoutPage(instantCheckoutData);
            }
        };
    }


    CheckoutDataParameter.Builder buildCheckoutData(CartDigitalInfoData cartDigitalInfoData, String accessToken) {
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

    private void trackRechargePushEventRecommendation(int categoryId) {
        rechargePushEventRecommendationUseCase.execute(rechargePushEventRecommendationUseCase.createRequestParams(categoryId, "ATC"), new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                RechargePushEventRecommendationResponseEntity response = graphqlResponse.getData(RechargePushEventRecommendationResponseEntity.class);
            }
        });
    }
}
