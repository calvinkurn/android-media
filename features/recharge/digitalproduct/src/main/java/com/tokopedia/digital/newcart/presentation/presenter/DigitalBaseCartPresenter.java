package com.tokopedia.digital.newcart.presentation.presenter;

import androidx.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.network.exception.HttpErrorException;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.common_digital.common.RechargeAnalytics;
import com.tokopedia.common_digital.common.constant.DigitalCache;
import com.tokopedia.commonpromo.PromoCodeAutoApplyUseCase;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.digital.R;
import com.tokopedia.digital.common.analytic.DigitalAnalytics;
import com.tokopedia.digital.newcart.data.entity.requestbody.atc.Attributes;
import com.tokopedia.digital.newcart.data.entity.requestbody.atc.Field;
import com.tokopedia.digital.newcart.data.entity.requestbody.atc.RequestBodyAtcDigital;
import com.tokopedia.digital.newcart.data.entity.requestbody.checkout.Cart;
import com.tokopedia.digital.newcart.data.entity.requestbody.checkout.Data;
import com.tokopedia.digital.newcart.data.entity.requestbody.checkout.FintechProductCheckout;
import com.tokopedia.digital.newcart.data.entity.requestbody.checkout.Relationships;
import com.tokopedia.digital.newcart.data.entity.requestbody.checkout.RequestBodyCheckout;
import com.tokopedia.digital.newcart.data.entity.requestbody.otpcart.RequestBodyOtpSuccess;
import com.tokopedia.digital.newcart.data.entity.requestbody.voucher.RequestBodyCancelVoucher;
import com.tokopedia.digital.newcart.data.entity.response.cart.ResponseCartData;
import com.tokopedia.digital.newcart.domain.interactor.ICartDigitalInteractor;
import com.tokopedia.digital.newcart.domain.mapper.CartMapperData;
import com.tokopedia.digital.newcart.domain.mapper.ICartMapperData;
import com.tokopedia.digital.newcart.domain.model.CheckoutDigitalData;
import com.tokopedia.digital.newcart.domain.model.VoucherAttributeDigital;
import com.tokopedia.digital.newcart.domain.model.VoucherDigital;
import com.tokopedia.digital.newcart.domain.usecase.DigitalCheckoutUseCase;
import com.tokopedia.digital.newcart.presentation.contract.DigitalBaseContract;
import com.tokopedia.digital.newcart.presentation.model.DigitalSubscriptionParams;
import com.tokopedia.digital.newcart.presentation.model.cart.AttributesDigital;
import com.tokopedia.digital.newcart.presentation.model.cart.CartAdditionalInfo;
import com.tokopedia.digital.newcart.presentation.model.cart.CartAutoApplyVoucher;
import com.tokopedia.digital.newcart.presentation.model.cart.CartDigitalInfoData;
import com.tokopedia.digital.newcart.presentation.model.cart.CartItemDigital;
import com.tokopedia.digital.newcart.presentation.model.cart.FintechProduct;
import com.tokopedia.digital.newcart.presentation.model.cart.FintechProductInfo;
import com.tokopedia.digital.newcart.presentation.model.cart.UserInputPriceDigital;
import com.tokopedia.digital.newcart.presentation.model.checkout.CheckoutDataParameter;
import com.tokopedia.digital.newcart.presentation.usecase.DigitalAddToCartUseCase;
import com.tokopedia.digital.newcart.presentation.usecase.DigitalGetCartUseCase;
import com.tokopedia.digital.utils.DeviceUtil;
import com.tokopedia.network.constant.ErrorNetMessage;
import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.network.exception.ResponseDataNullException;
import com.tokopedia.network.exception.ResponseErrorException;
import com.tokopedia.promocheckout.common.view.model.PromoData;
import com.tokopedia.track.TrackApp;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import java.lang.reflect.Type;
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
    protected DigitalAnalytics digitalAnalytics;
    private RechargeAnalytics rechargeAnalytics;
    private ICartDigitalInteractor cartDigitalInteractor;
    private UserSessionInterface userSession;
    private DigitalCheckoutUseCase digitalCheckoutUseCase;
    private DigitalAddToCartUseCase digitalAddToCartUseCase;
    private DigitalGetCartUseCase digitalGetCartUseCase;
    private String PROMO_CODE = "promoCode";
    public static final String KEY_CACHE_PROMO_CODE = "KEY_CACHE_PROMO_CODE";
    public static final String TRANSACTION_TYPE_PROTECTION = "purchase-protection";


    public DigitalBaseCartPresenter(DigitalAddToCartUseCase digitalAddToCartUseCase,
                                    DigitalGetCartUseCase digitalGetCartUseCase,
                                    DigitalAnalytics digitalAnalytics,
                                    RechargeAnalytics rechargeAnalytics,
                                    ICartDigitalInteractor cartDigitalInteractor,
                                    UserSessionInterface userSession,
                                    DigitalCheckoutUseCase digitalCheckoutUseCase) {
        this.digitalAddToCartUseCase = digitalAddToCartUseCase;
        this.digitalGetCartUseCase = digitalGetCartUseCase;
        this.digitalAnalytics = digitalAnalytics;
        this.rechargeAnalytics = rechargeAnalytics;
        this.cartDigitalInteractor = cartDigitalInteractor;
        this.userSession = userSession;
        this.digitalCheckoutUseCase = digitalCheckoutUseCase;
    }

    @Override
    public void detachView() {
        digitalAddToCartUseCase.unsubscribe();
        digitalCheckoutUseCase.unsubscribe();
        super.detachView();
    }

    @Override
    public void onViewCreated() {
        if (getView().getCartPassData() == null) {
            getView().closeViewWithMessageAlert(getView().getString(R.string.digital_transaction_failed_title));
            return;
        }

        if (!userSession.isLoggedIn()) {
            getView().closeViewWithMessageAlert(getView().getString(R.string.digital_cart_login_message));
        } else {
            getView().hideCartView();
            getView().showFullPageLoading();
            getView().startPerfomanceMonitoringTrace();
            if (getView().getCartPassData().isFromPDP() || getView().getCartPassData().getNeedGetCart()) {
                if (getView().getCartPassData().getCategoryId() != null) {
                    RequestParams requestParams = digitalGetCartUseCase.createRequestParams(
                            getView().getCartPassData().getCategoryId(),
                            userSession.getUserId(),
                            userSession.getDeviceId());
                    digitalGetCartUseCase.execute(requestParams, getSubscriberCart(false));
                } else
                    getView().closeViewWithMessageAlert(getView().getString(R.string.digital_transaction_failed_title));
            } else {
                RequestParams requestParams = digitalAddToCartUseCase.createRequestParams(
                        getRequestBodyAtcDigital(), getView().getIdemPotencyKey());
                digitalAddToCartUseCase.execute(requestParams, getSubscriberCart(true));
            }
        }
    }


    @NonNull
    private RequestBodyAtcDigital getRequestBodyAtcDigital() {
        RequestBodyAtcDigital requestBodyAtcDigital = new RequestBodyAtcDigital();
        List<Field> fieldList = new ArrayList<>();
        String clientNumber = getView().getClientNumber();
        String zoneId = getView().getZoneId();
        HashMap<String, String> fields = getView().getFields();
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
        if (fields != null) {
            for (Map.Entry<String, String> fieldItem : fields.entrySet()) {
                Field field = new Field();
                field.setName(fieldItem.getKey());
                field.setValue(fieldItem.getValue());
                fieldList.add(field);
            }
        }
        Attributes attributes = new Attributes();
        attributes.setDeviceId(5);
        attributes.setInstantCheckout(getView().isInstantCheckout());
        attributes.setIpAddress(DeviceUtil.getLocalIpAddress());
        attributes.setUserAgent(DeviceUtil.getUserAgentForApiCall());
        attributes.setUserId(Integer.parseInt(userSession.getUserId()));
        attributes.setProductId(getView().getProductId());
        long orderId = getView().getOrderId();
        if (orderId > 0L) attributes.setOrderId(orderId);
        attributes.setFields(fieldList);
        if (GlobalConfig.isSellerApp()) {
            attributes.setReseller(true);
        }
        attributes.setIdentifier(getView().getDigitalIdentifierParam());
        attributes.setShowSubscribeFlag(true);
        attributes.setThankyouNative(true);
        attributes.setThankyouNativeNew(true);
        // Handle subscription params
        DigitalSubscriptionParams subParams = getView().getDigitalSubscriptionParams();
        if (subParams.getShowSubscribePopUp() != null) {
            attributes.setShowSubscribePopUp(subParams.getShowSubscribePopUp());
        }
        if (subParams.getAutoSubscribe() != null) {
            attributes.setAutoSubscribe(subParams.getAutoSubscribe());
        }
        requestBodyAtcDigital.setType("add_cart");
        requestBodyAtcDigital.setAttributes(attributes);
        return requestBodyAtcDigital;
    }

    private Subscriber<Map<Type, RestResponse>> getSubscriberCart(Boolean isAddToCart) {
        return new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    renderErrorState(e);

                    getView().stopPerfomanceMonitoringTrace();
                }
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<ResponseCartData>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse data = restResponse.getData();
                ResponseCartData responseCartData = (ResponseCartData) data.getData();
                ICartMapperData cartMapperData = new CartMapperData();
                CartDigitalInfoData cartDigitalInfoData = cartMapperData.transformCartInfoData(responseCartData);

                getView().setCartDigitalInfo(cartDigitalInfoData);
                getView().setCheckoutParameter(buildCheckoutData(cartDigitalInfoData, userSession.getAccessToken()));

                if (isAddToCart) {
                    digitalAnalytics.eventAddToCart(cartDigitalInfoData, getView().getCartPassData().getSource(),
                            userSession.getUserId());
                }
                digitalAnalytics.eventCheckout(cartDigitalInfoData);

                if (cartDigitalInfoData.getAttributes().isNeedOtp()) {
                    getView().showCartView();
                    getView().hideFullPageLoading();
                    getView().interruptRequestTokenVerification(userSession.getPhoneNumber());
                } else {
                    rechargeAnalytics.trackAddToCartRechargePushEventRecommendation(Integer.parseInt(getView().getCartPassData().getCategoryId()));
                    renderCart(cartDigitalInfoData);
                }
            }
        };
    }

    private void renderCart(CartDigitalInfoData cartDigitalInfoData) {
        digitalAnalytics.sendCartScreen(getView().getActivity());
        renderCrossSellingCart(cartDigitalInfoData);
        getView().stopPerfomanceMonitoringTrace();
    }

    protected void renderCrossSellingCart(CartDigitalInfoData cartDigitalInfoData) {
        getView().showCartView();
        getView().hideFullPageLoading();
        renderBaseCart(cartDigitalInfoData);
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

        renderFintechProduct(cartDigitalInfoData.getAttributes().getFintechProduct());
    }

    private void renderFintechProduct(List<FintechProduct> fintechProducts) {
        if (fintechProducts != null && !fintechProducts.isEmpty()) {
            getView().renderMyBillsEgoldView(fintechProducts.get(0));
            getView().updateTotalPriceWithFintechAmount();
        }
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

    public void renderPostPaidPopUp(CartDigitalInfoData cartDigitalInfoData) {
        if (!getView().getDigitalSubscriptionParams().isSubscribed() && cartDigitalInfoData.getAttributes().getPostPaidPopupAttribute() != null) {
            getView().showPostPaidDialog(
                    cartDigitalInfoData.getAttributes().getPostPaidPopupAttribute().getTitle(),
                    cartDigitalInfoData.getAttributes().getPostPaidPopupAttribute().getContent(),
                    cartDigitalInfoData.getAttributes().getPostPaidPopupAttribute().getConfirmButtonTitle()
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

    private void applyPromoCode(String promoCode) {
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
                    renderErrorState(e);
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
    public void onClickPromoButton() {
        AttributesDigital attributes = getView().getCartInfoData().getAttributes();
        if (attributes != null
                && attributes.getCategoryName() != null
                && attributes.getOperatorName() != null) {
            digitalAnalytics.eventclickUseVoucher(attributes.getCategoryName());
            digitalAnalytics.eventClickPromoButton(
                    attributes.getCategoryName(),
                    attributes.getOperatorName(),
                    userSession.getUserId()
            );
        }
    }

    @Override
    public void onClickPromoDetail() {
        AttributesDigital attributes = getView().getCartInfoData().getAttributes();
        if (attributes != null
                && attributes.getCategoryName() != null
                && attributes.getOperatorName() != null) {
            digitalAnalytics.eventClickPromoButton(
                    attributes.getCategoryName(),
                    attributes.getOperatorName(),
                    userSession.getUserId()
            );
        }
    }

    @Override
    public void onReceivePromoCode(PromoData promoData) {
        if (isViewAttached()) {
            VoucherDigital voucherDigital = new VoucherDigital();
            VoucherAttributeDigital voucherAttributeDigital = new VoucherAttributeDigital();
            voucherAttributeDigital.setIsCoupon(promoData.getTypePromo());
            voucherAttributeDigital.setTitle(promoData.getTitle());
            voucherAttributeDigital.setVoucherCode(promoData.getPromoCode());
            voucherAttributeDigital.setMessage(promoData.getDescription());
            voucherAttributeDigital.setDiscountAmountPlain(promoData.getAmount());
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
        com.tokopedia.digital.newcart.data.entity.requestbody.checkout.Attributes attributes =
                new com.tokopedia.digital.newcart.data.entity.requestbody.checkout.Attributes();
        attributes.setVoucherCode(checkoutData.getVoucherCode());
        attributes.setTransactionAmount(checkoutData.getTransactionAmount());
        attributes.setIpAddress(checkoutData.getIpAddress());
        attributes.setUserAgent(checkoutData.getUserAgent());
        attributes.setIdentifier(getView().getDigitalIdentifierParam());
        String getTrackClientId = TrackApp.getInstance().getGTM().getClientIDString();
        attributes.setClientId(getTrackClientId);
        attributes.setAppsFlyer(DeviceUtil.getAppsFlyerIdentifierParam(
                TrackApp.getInstance().getAppsFlyer().getUniqueId(),
                ""));

        if (getView().isEgoldChecked()) {
            FintechProduct fintechProduct = getFintechProduct();
            if (fintechProduct != null) {
                List fintechProductCheckouts = new ArrayList<FintechProductCheckout>();
                fintechProductCheckouts.add(
                        new FintechProductCheckout(
                                fintechProduct.getTransactionType(),
                                fintechProduct.getTierId(),
                                Long.parseLong(attributes.getIdentifier().getUserId()),
                                fintechProduct.getFintechAmount(),
                                fintechProduct.getFintechPartnerAmount(),
                                fintechProduct.getInfo().getTitle()
                        ));
                attributes.setFintechProduct(fintechProductCheckouts);
            }
        }

        requestBodyCheckout.setAttributes(attributes);
        requestBodyCheckout.setRelationships(
                new Relationships(new Cart(new Data(
                        checkoutData.getRelationType(), checkoutData.getRelationId()
                )))
        );

        return requestBodyCheckout;
    }

    private FintechProduct getFintechProduct() {
        AttributesDigital attributes = getView().getCartInfoData().getAttributes();
        if (attributes != null && attributes.getFintechProduct() != null
                && !attributes.getFintechProduct().isEmpty()) {
            return attributes.getFintechProduct().get(0);
        } else return null;
    }

    @Override
    public void onPaymentSuccess(String categoryId) {
        if (categoryId != null && categoryId.length() > 0) {
            PersistentCacheManager.instance.delete(DigitalCache.NEW_DIGITAL_CATEGORY_AND_FAV + "/" + categoryId);
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
    public void cancelVoucherCart() {
        RequestBodyCancelVoucher requestBodyCancelVoucher = new RequestBodyCancelVoucher();
        com.tokopedia.digital.newcart.data.entity.requestbody.voucher.Attributes attributes =
                new com.tokopedia.digital.newcart.data.entity.requestbody.voucher.Attributes();
        attributes.setIdentifier(getView().getDigitalIdentifierParam());
        requestBodyCancelVoucher.setAttributes(attributes);
        cartDigitalInteractor.cancelVoucher(requestBodyCancelVoucher, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().hideFullPageLoading();
                    getView().failedCancelVoucherCart(e);
                }
            }

            @Override
            public void onNext(Boolean success) {
                getView().hideFullPageLoading();
                if (success) {
                    getView().successCancelVoucherCart();
                } else {
                    getView().failedCancelVoucherCart(new Throwable(""));
                }
            }
        });
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

    @Override
    public void onEgoldMoreInfoClicked() {
        List<FintechProduct> fintechProducts = getView().getCartInfoData().getAttributes().getFintechProduct();
        if (fintechProducts != null && !fintechProducts.isEmpty()) {
            FintechProductInfo fintechProductInfo = fintechProducts.get(0).getInfo();
            if (fintechProductInfo != null) {
                getView().renderEgoldMoreInfo(fintechProductInfo.getTitle(),
                        fintechProductInfo.getTooltipText(), fintechProductInfo.getUrlLink());
            }
        }
    }

    @Override
    public void onEgoldCheckedListener(Boolean checked, Long inputPrice) {
        AttributesDigital attributes = getView().getCartInfoData().getAttributes();
        FintechProduct fintechProduct = getFintechProduct();
        if (fintechProduct != null) {
            // Check fintech product type
            if (fintechProduct.getTransactionType() != null
                    && fintechProduct.getTransactionType() == TRANSACTION_TYPE_PROTECTION) {
                digitalAnalytics.eventClickProtection(checked, attributes.getCategoryName(),
                        attributes.getOperatorName(), userSession.getUserId());
            } else {
                digitalAnalytics.eventClickCrossSell(checked, attributes.getCategoryName(),
                        attributes.getOperatorName(), userSession.getUserId());
            }
            updateTotalPriceWithFintechAmount(checked, inputPrice);
        }
    }

    @Override
    public void updateTotalPriceWithFintechAmount(Boolean checked, Long inputPrice) {
        AttributesDigital attributes = getView().getCartInfoData().getAttributes();
        FintechProduct fintechProduct = getFintechProduct();
        if (fintechProduct != null) {
            long totalPrice = inputPrice > 0 ? inputPrice : attributes.getPricePlain();
            if (checked) totalPrice += fintechProduct.getFintechAmount();
            getView().renderCheckoutView(totalPrice);
        }
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
                    renderErrorState(e);
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

    private void renderErrorState(Throwable e) {
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
                    renderErrorState(e);
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
}
