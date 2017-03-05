package com.tokopedia.digital.cart.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.digital.cart.data.entity.requestbody.atc.Attributes;
import com.tokopedia.digital.cart.data.entity.requestbody.atc.Field;
import com.tokopedia.digital.cart.data.entity.requestbody.atc.RequestBodyAtcDigital;
import com.tokopedia.digital.cart.interactor.ICartDigitalInteractor;
import com.tokopedia.digital.cart.listener.IDigitalCartView;
import com.tokopedia.digital.cart.model.CartDigitalInfoData;

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
    public void processGetCartData(String categoryId) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("category_id", categoryId);
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
        attributes.setIpAddress("127.0.0.1");
        attributes.setUserAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:51.0) Gecko/20100101 Firefox/51.0");
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

    private Subscriber<CartDigitalInfoData> getSubscriberAddToCart() {
        return new Subscriber<CartDigitalInfoData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof ResponseDataNullException) {
                    view.closeViewWithMessageAlert(e.getMessage());
                } else {
                    view.showToastMessage(e.getMessage());
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
                if (e instanceof ResponseDataNullException) {
                    view.closeViewWithMessageAlert(e.getMessage());
                } else {
                    view.showToastMessage(e.getMessage());
                }
            }

            @Override
            public void onNext(CartDigitalInfoData cartDigitalInfoData) {
                view.renderCartDigitalInfoData(cartDigitalInfoData);
            }
        };
    }
}
