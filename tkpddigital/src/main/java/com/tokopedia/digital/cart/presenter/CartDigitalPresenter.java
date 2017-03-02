package com.tokopedia.digital.cart.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.digital.cart.interactor.ICartDigitalInteractor;
import com.tokopedia.digital.cart.listener.IDigitalCartView;
import com.tokopedia.digital.cart.model.CartDigitalInfoData;

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
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("category_id", passData.getCategoryId());
        view.renderLoadingGetCartInfo();
        cartDigitalInteractor.getCartInfoData(
                view.getGeneratedAuthParamNetwork(param),
                getSubscriberCartInfo()
        );
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
