package com.tokopedia.digital.cart.presenter;

import android.util.Log;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.apiservices.digital.DigitalEndpointService;
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
        final Observable<Response<String>> observable = digitalService.getApi()
                .getCart(AuthUtil.generateParamsNetwork(MainApplication.getAppContext(), param));
        observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Response<String> stringResponse) {
                        Log.d("CART RESPONSE", stringResponse.body());
                    }
                });
//        Observable.just(param)
//                .flatMap(new Func1<TKPDMapParam<String, String>, Observable<Response<String>>>() {
//                    @Override
//                    public Observable<Response<String>> call(TKPDMapParam<String, String> param) {
//                        return new DigitalEndpointService().getApi().getCart(param);
//                    }
//                })
//                .subscribeOn(Schedulers.newThread())
//                .unsubscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Response<String>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onNext(Response<String> stringResponse) {
//                        Log.d("CART RESPONSE", stringResponse.body());
//                    }
//                });
    }
}
