package com.tokopedia.digital.cart.presenter;

import android.os.Build;
import android.support.annotation.NonNull;

import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.exception.ResponseErrorException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.digital.cart.data.entity.requestbody.atc.Attributes;
import com.tokopedia.digital.cart.data.entity.requestbody.atc.Field;
import com.tokopedia.digital.cart.data.entity.requestbody.atc.RequestBodyAtcDigital;
import com.tokopedia.digital.cart.interactor.ICartDigitalInteractor;
import com.tokopedia.digital.cart.listener.IDigitalCartView;
import com.tokopedia.digital.cart.model.CartDigitalInfoData;
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
    public void processAddToCart(DigitalCheckoutPassData passData) {
        RequestBodyAtcDigital requestBodyAtcDigital = new RequestBodyAtcDigital();
        List<Field> fieldList = new ArrayList<>();
        if (passData.getClientNumber() != null && !passData.getClientNumber().isEmpty()) {
            Field field = new Field();
            field.setName("client_number");
            field.setValue(passData.getClientNumber());
            fieldList.add(field);
        }
        Attributes attributes = new Attributes();
        attributes.setDeviceId(5);
        attributes.setInstantCheckout(passData.getInstantCheckout().equals("1"));
        attributes.setIpAddress(DeviceUtil.getLocalIpAddress());
        attributes.setUserAgent(
                "Android Tokopedia Application/"
                        + GlobalConfig.getPackageApplicationName()
                        + " v." + GlobalConfig.VERSION_NAME
                        + " (" + DeviceUtil.getDeviceName()
                        + "; Android; API_"
                        + Build.VERSION.SDK_INT
                        + "; Version"
                        + Build.VERSION.RELEASE + ") "
        );
        attributes.setUserId(Integer.parseInt(view.getUserId()));
        attributes.setAccessToken(view.getAccountToken());
        attributes.setWalletRefreshToken(view.getWalletRefreshToken());
        attributes.setProductId(Integer.parseInt(passData.getProductId()));
        attributes.setFields(fieldList);
        requestBodyAtcDigital.setType("add_cart");
        requestBodyAtcDigital.setAttributes(attributes);
        view.renderLoadingGetCartInfo();
        cartDigitalInteractor.addToCart(
                requestBodyAtcDigital, passData.getIdemPotencyKey(), getSubscriberAddToCart()
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
                view.renderCartDigitalInfoData(cartDigitalInfoData);
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
}
