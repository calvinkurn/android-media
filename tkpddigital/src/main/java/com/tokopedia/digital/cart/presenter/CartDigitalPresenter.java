package com.tokopedia.digital.cart.presenter;

import android.util.Log;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.apiservices.digital.DigitalEndpointService;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.cart.listener.IDigitalCartView;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author anggaprasetiyo on 2/24/17.
 */

public class CartDigitalPresenter implements ICartDigitalPresenter {
    private final IDigitalCartView view;

    public CartDigitalPresenter(IDigitalCartView view) {
        this.view = view;
    }

    @Override
    public void processGetCartData(String categoryId) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("category_id", categoryId);

        DigitalEndpointService digitalService = new DigitalEndpointService();
        final Observable<Response<TkpdDigitalResponse>> observable = digitalService.getApi()
                .getCart(AuthUtil.generateParamsNetwork(MainApplication.getAppContext(), param));
        observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<TkpdDigitalResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Response<TkpdDigitalResponse> stringResponse) {
                        Log.d("CART RESPONSE", stringResponse.body().getStrResponse());

                    }
                });

//        try {
//            TkpdDigitalResponse.factory("{\"data\":null}");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
