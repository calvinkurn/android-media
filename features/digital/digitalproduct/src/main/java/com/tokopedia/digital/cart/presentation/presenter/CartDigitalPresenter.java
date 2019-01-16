package com.tokopedia.digital.cart.presentation.presenter;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.network.exception.HttpErrorException;
import com.tokopedia.common_digital.cart.data.entity.requestbody.atc.Attributes;
import com.tokopedia.common_digital.cart.data.entity.requestbody.atc.Field;
import com.tokopedia.common_digital.cart.data.entity.requestbody.atc.RequestBodyAtcDigital;
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.Cart;
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.Data;
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.Relationships;
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout;
import com.tokopedia.common_digital.cart.domain.usecase.DigitalAddToCartUseCase;
import com.tokopedia.common_digital.cart.domain.usecase.DigitalInstantCheckoutUseCase;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.common_digital.cart.view.model.checkout.CheckoutDataParameter;
import com.tokopedia.common_digital.cart.view.model.checkout.InstantCheckoutData;
import com.tokopedia.common_digital.common.DigitalRouter;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.cart.data.cache.DigitalPostPaidLocalCache;
import com.tokopedia.core.util.BranchSdkUtils;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.digital.cart.data.entity.requestbody.otpcart.RequestBodyOtpSuccess;
import com.tokopedia.digital.cart.data.entity.requestbody.voucher.RequestBodyCancelVoucher;
import com.tokopedia.digital.cart.domain.interactor.ICartDigitalInteractor;
import com.tokopedia.digital.cart.domain.usecase.DigitalCheckoutUseCase;
import com.tokopedia.digital.cart.presentation.model.CheckoutDigitalData;
import com.tokopedia.digital.cart.presentation.model.VoucherDigital;
import com.tokopedia.digital.common.constant.DigitalCache;
import com.tokopedia.digital.common.util.DigitalAnalytics;
import com.tokopedia.digital.utils.DeviceUtil;
import com.tokopedia.network.exception.ResponseDataNullException;
import com.tokopedia.network.exception.ResponseErrorException;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.usecase.RequestParams;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 2/24/17.
 */

public class CartDigitalPresenter extends BaseDaggerPresenter<CartDigitalContract.View> implements CartDigitalContract.Presenter {

    private static final String TAG = CartDigitalPresenter.class.getSimpleName();
    private DigitalAnalytics digitalAnalytics;
    private UserSession userSession;
    private final ICartDigitalInteractor cartDigitalInteractor;
    private DigitalCheckoutUseCase digitalCheckoutUseCase;
    private DigitalAddToCartUseCase digitalAddToCartUseCase;
    private DigitalInstantCheckoutUseCase digitalInstantCheckoutUseCase;
    private DigitalRouter digitalRouter;
    private DigitalPostPaidLocalCache digitalPostPaidLocalCache;

    @Inject
    public CartDigitalPresenter(
            DigitalAnalytics digitalAnalytics,
            UserSession userSession,
            ICartDigitalInteractor iCartDigitalInteractor,
            DigitalAddToCartUseCase digitalAddToCartUseCase,
            DigitalCheckoutUseCase digitalCheckoutUseCase,
            DigitalInstantCheckoutUseCase digitalInstantCheckoutUseCase,
            DigitalRouter digitalRouter,
            DigitalPostPaidLocalCache digitalPostPaidLocalCache) {


        this.cartDigitalInteractor = iCartDigitalInteractor;
        this.digitalAddToCartUseCase = digitalAddToCartUseCase;
        this.digitalCheckoutUseCase = digitalCheckoutUseCase;
        this.digitalInstantCheckoutUseCase = digitalInstantCheckoutUseCase;
        this.digitalRouter = digitalRouter;
        this.userSession = userSession;
        this.digitalAnalytics = digitalAnalytics;
        this.digitalPostPaidLocalCache = digitalPostPaidLocalCache;
    }

    @Override
    public void processGetCartData(String digitalCategoryId) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("category_id", digitalCategoryId);
        getView().renderLoadingGetCartInfo();
        cartDigitalInteractor.getCartInfoData(
                getView().getGeneratedAuthParamNetwork(param),
                getSubscriberCartInfo()
        );
    }

    @Override
    public void processGetCartDataAfterCheckout(String digitalCategoryId) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("category_id", digitalCategoryId);
        getView().showInitialProgressLoading();
        cartDigitalInteractor.getCartInfoData(
                getView().getGeneratedAuthParamNetwork(param),
                getSubscriberCartInfoAfterCheckout()
        );
    }

    @Override
    public void processAddToCart() {
        getView().renderLoadingAddToCart();
        RequestParams requestParams = digitalAddToCartUseCase.createRequestParams(
                getRequestBodyAtcDigital(), getView().getIdemPotencyKey());
        digitalAddToCartUseCase.execute(requestParams, getSubscriberAddToCart());
    }

    @Override
    public void processCheckVoucher(String voucherCode, String digitalCategoryId) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("voucher_code", voucherCode);
        param.put("category_id", digitalCategoryId);
        getView().showProgressLoading();
        cartDigitalInteractor.checkVoucher(
                getView().getGeneratedAuthParamNetwork(param), getSubscriberCheckVoucher()
        );
    }

    @Override
    public void processToCheckout() {
        CheckoutDataParameter checkoutData = getView().getCheckoutData();
        if (checkoutData.isNeedOtp()) {
            startOTPProcess();
            return;
        }
        getView().showProgressLoading();
        RequestParams requestParams = digitalCheckoutUseCase.createRequestParams(getRequestBodyCheckout(checkoutData));
        digitalCheckoutUseCase.execute(
                requestParams,
                getSubscriberCheckout()
        );
    }

    @Override
    public void processToInstantCheckout() {
        CheckoutDataParameter checkoutData = getView().getCheckoutData();
        if (checkoutData.isNeedOtp()) {
            startOTPProcess();
            return;
        }
        RequestParams requestParams = digitalInstantCheckoutUseCase.createRequestParams(getRequestBodyCheckout(checkoutData));
        digitalInstantCheckoutUseCase.execute(requestParams, getSubscriberInstantCheckout());
    }

    @Override
    public void processPatchOtpCart(String digitalCategoryId) {
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

        TKPDMapParam<String, String> paramGetCart = new TKPDMapParam<>();
        paramGetCart.put("category_id", digitalCategoryId);
        getView().renderLoadingGetCartInfo();
        cartDigitalInteractor.patchCartOtp(
                requestBodyOtpSuccess,
                getView().getGeneratedAuthParamNetwork(paramGetCart),
                getSubscriberCartInfo()
        );
    }

    @Override
    public void sendAnalyticsATCSuccess(CartDigitalInfoData cartDigitalInfoData, int extraComeFrom) {
        digitalAnalytics.eventAddToCart(cartDigitalInfoData, extraComeFrom);
        digitalAnalytics.eventCheckout(cartDigitalInfoData);
    }

    @Override
    public void onClearVoucher() {
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
    public void onPaymentSuccess(String categoryId) {
        //Delete Category and Favorit List Cache
        if (!TextUtils.isEmpty(categoryId)) {
            GlobalCacheManager globalCacheManager = new GlobalCacheManager();
            globalCacheManager.delete(DigitalCache.NEW_DIGITAL_CATEGORY_AND_FAV + "/" + categoryId);
        }
    }

    @Override
    public void onFirstTimeLaunched() {
        if (userSession.isLoggedIn()) {
            processAddToCart();
        } else {
            getView().navigateToLoggedInPage();
        }
    }

    @Override
    public void onLoginResultReceived() {
        if (userSession.isLoggedIn()) {
            processAddToCart();
        } else {
            getView().closeView();
        }
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
                getView().hideProgressLoading();
                if (e instanceof UnknownHostException || e instanceof ConnectException) {
                    /* Ini kalau ga ada internet */
                    getView().renderErrorNoConnectionCheckout(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else if (e instanceof SocketTimeoutException) {
                    /* Ini kalau timeout */
                    getView().renderErrorTimeoutConnectionCheckout(
                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                    );
                } else if (e instanceof ResponseErrorException) {
                    /* Ini kalau error dari API kasih message error */
                    getView().renderErrorCheckout(e.getMessage());
                } else if (e instanceof ResponseDataNullException) {
                    /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
                    getView().renderErrorCheckout(e.getMessage());
                } else if (e instanceof HttpErrorException) {
                    /* Ini Http error, misal 403, 500, 404,
                     code http errornya bisa diambil
                     e.getErrorCode */
                    getView().renderErrorHttpCheckout(e.getMessage());
                } else {
                    /* Ini diluar dari segalanya hahahaha */
                    getView().renderErrorHttpCheckout(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(CheckoutDigitalData checkoutDigitalData) {
                getView().hideProgressLoading();
                Log.d(TAG, checkoutDigitalData.toString());
                digitalAnalytics.eventProceedToPayment(getView().getCartDataInfo(), getView().getCheckoutData().getVoucherCode());
                getView().renderToTopPay(checkoutDigitalData);
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
                getView().hideProgressLoading();
                if (e instanceof UnknownHostException || e instanceof ConnectException) {
                    /* Ini kalau ga ada internet */
                    getView().renderErrorNoConnectionInstantCheckout(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else if (e instanceof SocketTimeoutException) {
                    /* Ini kalau timeout */
                    getView().renderErrorTimeoutConnectionInstantCheckout(
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
                    getView().renderErrorHttpInstantCheckout(e.getMessage());
                } else {
                    /* Ini diluar dari segalanya hahahaha */
                    getView().renderErrorHttpInstantCheckout(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(InstantCheckoutData instantCheckoutData) {
                getView().hideProgressLoading();
                getView().renderToInstantCheckoutPage(instantCheckoutData);
            }
        };
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
                getView().hideProgressLoading();
                if (e instanceof UnknownHostException || e instanceof ConnectException) {
                    /* Ini kalau ga ada internet */
//                    view.renderErrorNoConnectionCheckVoucher(
//                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
//                    );
                } else if (e instanceof SocketTimeoutException) {
                    /* Ini kalau timeout */
//                    view.renderErrorTimeoutConnectionCheckVoucher(
//                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
//                    );
                } else if (e instanceof ResponseErrorException) {
                    /* Ini kalau error dari API kasih message error */
//                    view.renderErrorCheckVoucher(e.getMessage());
                    removeBranchPromoIfNeeded();
                } else if (e instanceof ResponseDataNullException) {
                    /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
//                    view.renderErrorCheckVoucher(e.getMessage());
                } else if (e instanceof HttpErrorException) {
                    /* Ini Http error, misal 403, 500, 404,
                     code http errornya bisa diambil
                     e.getErrorCode */
//                    view.renderErrorHttpCheckVoucher(e.getMessage());
                } else {
                    /* Ini diluar dari segalanya hahahaha */
//                    view.renderErrorHttpCheckVoucher(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(VoucherDigital voucherDigital) {
                getView().hideProgressLoading();
                getView().renderVoucherInfoData(voucherDigital);
            }
        };
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
                    /* Ini kalau ga ada internet */
                    getView().renderErrorNoConnectionAddToCart(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                    /* Ini kalau timeout */
                    getView().renderErrorTimeoutConnectionAddToCart(
                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                    );
                } else if (e instanceof ResponseErrorException) {
                    /* Ini kalau error dari API kasih message error */
                    getView().renderErrorAddToCart(e.getMessage());
                } else if (e instanceof ResponseDataNullException) {
                    /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
                    getView().renderErrorAddToCart(e.getMessage());
                } else if (e instanceof HttpErrorException) {
                    /* Ini Http error, misal 403, 500, 404,
                     code http errornya bisa diambil
                     e.getErrorCode */
                    getView().renderErrorHttpAddToCart(e.getMessage());
                } else {
                    /* Ini diluar dari segalanya hahahaha */
                    getView().renderErrorHttpAddToCart(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(CartDigitalInfoData cartDigitalInfoData) {
                if (cartDigitalInfoData.getAttributes().isNeedOtp()) {
                    getView().clearContentRendered();
                    getView().setCartDigitalInfo(cartDigitalInfoData);
                    startOTPProcess();
                } else {
                    if (cartDigitalInfoData.getAttributes().getPostPaidPopupAttribute() != null
                            && !digitalPostPaidLocalCache.isAlreadyShowPostPaidPopUp(userSession.getUserId())) {
                        getView().showPostPaidDialog(
                                cartDigitalInfoData.getAttributes().getPostPaidPopupAttribute().getTitle(),
                                cartDigitalInfoData.getAttributes().getPostPaidPopupAttribute().getContent(),
                                cartDigitalInfoData.getAttributes().getPostPaidPopupAttribute().getConfirmButtonTitle()
                        );
                    }
                    getView().renderAddToCartData(cartDigitalInfoData);
                }
            }
        };
    }


    private void startOTPProcess() {
        getView().interruptRequestTokenVerification();
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
                if (e instanceof UnknownHostException || e instanceof ConnectException) {
                    /* Ini kalau ga ada internet */
                    getView().renderErrorNoConnectionGetCartData(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else if (e instanceof SocketTimeoutException) {
                    /* Ini kalau timeout */
                    getView().renderErrorTimeoutConnectionGetCartData(
                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                    );
                } else if (e instanceof ResponseErrorException) {
                    /* Ini kalau error dari API kasih message error */
                    getView().renderErrorGetCartData(e.getMessage());
                } else if (e instanceof ResponseDataNullException) {
                    /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
                    getView().renderErrorGetCartData(e.getMessage());
                } else if (e instanceof HttpErrorException) {
                    /* Ini Http error, misal 403, 500, 404,
                     code http errornya bisa diambil
                     e.getErrorCode */
                    getView().renderErrorHttpGetCartData(e.getMessage());
                } else {
                    /* Ini diluar dari segalanya hahahaha */
                    getView().renderErrorHttpGetCartData(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(CartDigitalInfoData cartDigitalInfoData) {
                getView().renderCartDigitalInfoData(cartDigitalInfoData);

                if (!getView().isAlreadyShowPostPaid()
                        && cartDigitalInfoData.getAttributes().getPostPaidPopupAttribute() != null
                        && !digitalPostPaidLocalCache.isAlreadyShowPostPaidPopUp(userSession.getUserId())) {
                    getView().showPostPaidDialog(
                            cartDigitalInfoData.getAttributes().getPostPaidPopupAttribute().getTitle(),
                            cartDigitalInfoData.getAttributes().getPostPaidPopupAttribute().getContent(),
                            cartDigitalInfoData.getAttributes().getPostPaidPopupAttribute().getConfirmButtonTitle()
                    );
                }
            }
        };
    }

    @NonNull
    private Subscriber<CartDigitalInfoData> getSubscriberCartInfoAfterCheckout() {
        return new Subscriber<CartDigitalInfoData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if(isViewAttached()) {
                    if (e instanceof UnknownHostException || e instanceof ConnectException) {
                        /* Ini kalau ga ada internet */
                        getView().renderErrorNoConnectionGetCartData(
                                ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                        );
                    } else if (e instanceof SocketTimeoutException) {
                        /* Ini kalau timeout */
                        getView().renderErrorTimeoutConnectionGetCartData(
                                ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                        );
                    } else if (e instanceof ResponseErrorException) {
                        /* Ini kalau error dari API kasih message error */
                        getView().renderErrorGetCartData(e.getMessage());
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
                        getView().renderErrorHttpGetCartData(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                    }
                }
            }

            @Override
            public void onNext(CartDigitalInfoData cartDigitalInfoData) {
                cartDigitalInfoData.setForceRenderCart(true);
                getView().renderCartDigitalInfoData(cartDigitalInfoData);
            }
        };
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
        attributes.setUserId(Integer.parseInt(getView().getUserId()));
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
        attributes.setDealsIds(new ArrayList<>());
        attributes.setIdentifier(getView().getDigitalIdentifierParam());
        attributes.setClientId(TrackingUtils.getClientID(getView().getApplicationContext()));
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
    public void autoApplyCouponIfAvailable(String digitalCategoryId) {
        String savedCoupon = BranchSdkUtils.getAutoApplyCouponIfAvailable(getView().getActivity());
        if (!TextUtils.isEmpty(savedCoupon)) {
            processCheckVoucher(savedCoupon, digitalCategoryId);
        }
    }

    private void removeBranchPromoIfNeeded() {
        BranchSdkUtils.removeCouponCode(getView().getActivity());
    }
}
