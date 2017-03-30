package com.tokopedia.digital.cart.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.exception.ResponseErrorException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.cart.data.entity.requestbody.atc.Attributes;
import com.tokopedia.digital.cart.data.entity.requestbody.atc.Field;
import com.tokopedia.digital.cart.data.entity.requestbody.atc.RequestBodyAtcDigital;
import com.tokopedia.digital.cart.data.entity.requestbody.checkout.Cart;
import com.tokopedia.digital.cart.data.entity.requestbody.checkout.Data;
import com.tokopedia.digital.cart.data.entity.requestbody.checkout.Relationships;
import com.tokopedia.digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout;
import com.tokopedia.digital.cart.data.entity.requestbody.topcart.RequestBodyOtpSuccess;
import com.tokopedia.digital.cart.interactor.ICartDigitalInteractor;
import com.tokopedia.digital.cart.listener.IDigitalCartView;
import com.tokopedia.digital.cart.model.CartDigitalInfoData;
import com.tokopedia.digital.cart.model.CheckoutDataParameter;
import com.tokopedia.digital.cart.model.CheckoutDigitalData;
import com.tokopedia.digital.cart.model.InstantCheckoutData;
import com.tokopedia.digital.cart.model.VoucherDigital;
import com.tokopedia.digital.utils.DeviceUtil;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 2/24/17.
 */

public class CartDigitalPresenter implements ICartDigitalPresenter {
    private static final String TAG = CartDigitalPresenter.class.getSimpleName();
    private final IDigitalCartView view;
    private final ICartDigitalInteractor cartDigitalInteractor;

    public CartDigitalPresenter(IDigitalCartView view,
                                ICartDigitalInteractor iCartDigitalInteractor) {
        this.view = view;
        this.cartDigitalInteractor = iCartDigitalInteractor;
    }

    @Override
    public void processGetCartData() {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("category_id", view.getDigitalCategoryId());
        view.renderLoadingGetCartInfo();
        cartDigitalInteractor.getCartInfoData(
                view.getGeneratedAuthParamNetwork(param),
                getSubscriberCartInfo()
        );
    }

    @Override
    public void processAddToCart() {
        view.renderLoadingAddToCart();
        cartDigitalInteractor.addToCart(
                getRequestBodyAtcDigital(), view.getIdemPotencyKey(), getSubscriberAddToCart()
        );
    }


    @Override
    public void processCheckVoucher() {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("voucher_code", view.getVoucherCode());
        param.put("category_id", view.getDigitalCategoryId());
        view.showProgressLoading();
        cartDigitalInteractor.checkVoucher(
                view.getGeneratedAuthParamNetwork(param), getSubscriberCheckVoucher()
        );
    }

    @Override
    public void processToCheckout() {
        CheckoutDataParameter checkoutData = view.getCheckoutData();
        if (checkoutData.isNeedOtp()) {
            view.interruptRequestTokenVerification();
            return;
        }
        view.showProgressLoading();
        cartDigitalInteractor.checkout(
                getRequestBodyCheckout(checkoutData),
                getSubscriberCheckout()
        );
    }

    @Override
    public void processToInstantCheckout() {
        CheckoutDataParameter checkoutData = view.getCheckoutData();
        if (checkoutData.isNeedOtp()) {
            view.interruptRequestTokenVerification();
            return;
        }
        cartDigitalInteractor.instantCheckout(
                getRequestBodyCheckout(checkoutData),
                getSubscriberInstantCheckout()
        );
    }


    @Override
    public void processPatchOtpCart() {
        CheckoutDataParameter checkoutDataParameter = view.getCheckoutData();
        RequestBodyOtpSuccess requestBodyOtpSuccess = new RequestBodyOtpSuccess();
        requestBodyOtpSuccess.setType("cart");
        requestBodyOtpSuccess.setId(checkoutDataParameter.getRelationId());
        com.tokopedia.digital.cart.data.entity.requestbody.topcart.Attributes attributes =
                new com.tokopedia.digital.cart.data.entity.requestbody.topcart.Attributes();
        attributes.setIpAddress(DeviceUtil.getLocalIpAddress());
        attributes.setUserAgent(DeviceUtil.getUserAgentForApiCall());
        requestBodyOtpSuccess.setAttributes(attributes);

        TKPDMapParam<String, String> paramGetCart = new TKPDMapParam<>();
        paramGetCart.put("category_id", view.getDigitalCategoryId());
        view.renderLoadingGetCartInfo();
        cartDigitalInteractor.patchCartOtp(
                requestBodyOtpSuccess,
                view.getGeneratedAuthParamNetwork(paramGetCart),
                getSubscriberCartInfo()
        );

    }

    @NonNull
    private Subscriber<CheckoutDigitalData> getSubscriberCheckout() {
        return new Subscriber<CheckoutDigitalData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.hideProgressLoading();
                if (e instanceof UnknownHostException) {
                    /* Ini kalau ga ada internet */
                    view.renderErrorNoConnectionCheckout(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION
                    );
                } else if (e instanceof SocketTimeoutException) {
                    /* Ini kalau timeout */
                    view.renderErrorTimeoutConnectionCheckout(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION
                    );
                } else if (e instanceof ResponseErrorException) {
                     /* Ini kalau error dari API kasih message error */
                    view.renderErrorCheckout(e.getMessage());
                } else if (e instanceof ResponseDataNullException) {
                    /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
                    view.renderErrorCheckout(e.getMessage());
                } else if (e instanceof HttpErrorException) {
                    /* Ini Http error, misal 403, 500, 404,
                     code http errornya bisa diambil
                     e.getErrorCode */
                    view.renderErrorHttpCheckout(e.getMessage());
                } else {
                    /* Ini diluar dari segalanya hahahaha */
                    view.renderErrorHttpCheckout(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(CheckoutDigitalData checkoutDigitalData) {
                view.hideProgressLoading();
                Log.d(TAG, checkoutDigitalData.toString());
                view.renderToTopPay(checkoutDigitalData);
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
                view.hideProgressLoading();
                if (e instanceof UnknownHostException) {
                    /* Ini kalau ga ada internet */
                    view.renderErrorNoConnectionInstantCheckout(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION
                    );
                } else if (e instanceof SocketTimeoutException) {
                    /* Ini kalau timeout */
                    view.renderErrorTimeoutConnectionInstantCheckout(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION
                    );
                } else if (e instanceof ResponseErrorException) {
                     /* Ini kalau error dari API kasih message error */
                    view.renderErrorInstantCheckout(e.getMessage());
                } else if (e instanceof ResponseDataNullException) {
                    /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
                    view.renderErrorInstantCheckout(e.getMessage());
                } else if (e instanceof HttpErrorException) {
                    /* Ini Http error, misal 403, 500, 404,
                     code http errornya bisa diambil
                     e.getErrorCode */
                    view.renderErrorHttpInstantCheckout(e.getMessage());
                } else {
                    /* Ini diluar dari segalanya hahahaha */
                    view.renderErrorHttpInstantCheckout(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(InstantCheckoutData instantCheckoutData) {
                view.hideProgressLoading();
                view.renderToInstantCheckoutPage(instantCheckoutData);
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
                view.hideProgressLoading();
                if (e instanceof UnknownHostException) {
                    /* Ini kalau ga ada internet */
                    view.renderErrorNoConnectionCheckVoucher(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION
                    );
                } else if (e instanceof SocketTimeoutException) {
                    /* Ini kalau timeout */
                    view.renderErrorTimeoutConnectionCheckVoucher(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION
                    );
                } else if (e instanceof ResponseErrorException) {
                     /* Ini kalau error dari API kasih message error */
                    view.renderErrorCheckVoucher(e.getMessage());
                } else if (e instanceof ResponseDataNullException) {
                    /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
                    view.renderErrorCheckVoucher(e.getMessage());
                } else if (e instanceof HttpErrorException) {
                    /* Ini Http error, misal 403, 500, 404,
                     code http errornya bisa diambil
                     e.getErrorCode */
                    view.renderErrorHttpCheckVoucher(e.getMessage());
                } else {
                    /* Ini diluar dari segalanya hahahaha */
                    view.renderErrorHttpCheckVoucher(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(VoucherDigital voucherDigital) {
                view.hideProgressLoading();
                view.renderVoucherInfoData(voucherDigital);
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
                    view.renderErrorNoConnectionAddToCart(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION
                    );
                } else if (e instanceof SocketTimeoutException) {
                    /* Ini kalau timeout */
                    view.renderErrorTimeoutConnectionAddToCart(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION
                    );
                } else if (e instanceof ResponseErrorException) {
                     /* Ini kalau error dari API kasih message error */
                    view.renderErrorAddToCart(e.getMessage());
                } else if (e instanceof ResponseDataNullException) {
                    /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
                    view.renderErrorAddToCart(e.getMessage());
                } else if (e instanceof HttpErrorException) {
                    /* Ini Http error, misal 403, 500, 404,
                     code http errornya bisa diambil
                     e.getErrorCode */
                    view.renderErrorHttpAddToCart(e.getMessage());
                } else {
                    /* Ini diluar dari segalanya hahahaha */
                    view.renderErrorHttpAddToCart(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(CartDigitalInfoData cartDigitalInfoData) {
                if (cartDigitalInfoData.getAttributes().isNeedOtp()) {
                    view.clearContentRendered();
                    view.interruptRequestTokenVerification();
                } else {
                    view.renderCartDigitalInfoData(cartDigitalInfoData);
                }
                //  view.renderCartDigitalInfoData(cartDigitalInfoData);
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
                    /* Ini kalau ga ada internet */
                    view.renderErrorNoConnectionGetCartData(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION
                    );
                } else if (e instanceof SocketTimeoutException) {
                    /* Ini kalau timeout */
                    view.renderErrorTimeoutConnectionGetCartData(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION
                    );
                } else if (e instanceof ResponseErrorException) {
                     /* Ini kalau error dari API kasih message error */
                    view.renderErrorGetCartData(e.getMessage());
                } else if (e instanceof ResponseDataNullException) {
                    /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
                    view.renderErrorGetCartData(e.getMessage());
                } else if (e instanceof HttpErrorException) {
                    /* Ini Http error, misal 403, 500, 404,
                     code http errornya bisa diambil
                     e.getErrorCode */
                    view.renderErrorHttpGetCartData(e.getMessage());
                } else {
                    /* Ini diluar dari segalanya hahahaha */
                    view.renderErrorHttpGetCartData(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(CartDigitalInfoData cartDigitalInfoData) {
                view.renderCartDigitalInfoData(cartDigitalInfoData);
            }
        };
    }

    @NonNull
    private RequestBodyAtcDigital getRequestBodyAtcDigital() {
        RequestBodyAtcDigital requestBodyAtcDigital = new RequestBodyAtcDigital();
        List<Field> fieldList = new ArrayList<>();
        String clientNumber = view.getClientNumber();
        if (clientNumber != null && !clientNumber.isEmpty()) {
            Field field = new Field();
            field.setName("client_number");
            field.setValue(clientNumber);
            fieldList.add(field);
        }
        Attributes attributes = new Attributes();
        attributes.setDeviceId(5);
        attributes.setInstantCheckout(view.isInstantCheckout());
        attributes.setIpAddress(DeviceUtil.getLocalIpAddress());
        attributes.setUserAgent(DeviceUtil.getUserAgentForApiCall());
        attributes.setUserId(Integer.parseInt(view.getUserId()));
        attributes.setAccessToken(view.getAccountToken());
        attributes.setWalletRefreshToken(view.getWalletRefreshToken());
        attributes.setProductId(view.getProductId());
        attributes.setFields(fieldList);
        requestBodyAtcDigital.setType("add_cart");
        requestBodyAtcDigital.setAttributes(attributes);
        return requestBodyAtcDigital;
    }

    @NonNull
    private RequestBodyCheckout getRequestBodyCheckout(CheckoutDataParameter checkoutData) {
        RequestBodyCheckout requestBodyCheckout = new RequestBodyCheckout();
        requestBodyCheckout.setType("checkout");
        com.tokopedia.digital.cart.data.entity.requestbody.checkout.Attributes attributes =
                new com.tokopedia.digital.cart.data.entity.requestbody.checkout.Attributes();
        attributes.setVoucherCode(checkoutData.getVoucherCode());
        attributes.setTransactionAmount(checkoutData.getTransactionAmount());
        attributes.setIpAddress(checkoutData.getIpAddress());
        attributes.setUserAgent(checkoutData.getUserAgent());
        attributes.setAccessToken(checkoutData.getAccessToken());
        attributes.setWalletRefreshToken(checkoutData.getWalletRefreshToken());
        requestBodyCheckout.setAttributes(attributes);
        requestBodyCheckout.setRelationships(
                new Relationships(new Cart(new Data(
                        checkoutData.getRelationType(), checkoutData.getRelationId()
                )))
        );
        return requestBodyCheckout;
    }

}
