package com.tokopedia.core.shopinfo.facades;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.google.gson.Gson;
import com.tkpd.library.kirisame.Kirisame;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.shopinfo.facades.authservices.ShopService;
import com.tokopedia.core.shopinfo.models.etalasemodel.EtalaseModel;

import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Tkpd_Eka on 12/3/2015.
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class GetShopInfoRetrofit {

    public interface OnGetShopInfoListener {
        void onSuccess(String result);

        void onError(String message);

        void onFailure();
    }

    public interface OnGetShopEtalase {
        void onSuccess(EtalaseModel model);

        void onFailure();
    }

    private ShopService shopService;
    private Context context;
    private String shopId;
    private String shopDomain;
    private int autoRetry = 3;

    private OnGetShopInfoListener getShopInfoListener;
    private OnGetShopEtalase getShopEtalase;

    private Subscription onGetShopInfoSubs;
    private Subscription onGetShopEtalaseSubs;

    public GetShopInfoRetrofit(Context context, String shopId, String shopDomain) {
        shopService = new ShopService();
        this.context = context;
        this.shopId = shopId;
        this.shopDomain = shopDomain;
    }

    public void setGetShopInfoListener(OnGetShopInfoListener listener) {
        getShopInfoListener = listener;
    }

    //================================ Get shop info====================================================================

    public void getShopInfo() {
        Observable<Response<TkpdResponse>> observable = shopService.getApi()
                .getShopInfo(AuthUtil.generateParams(context, paramShopInfo()));
        onGetShopInfoSubs = observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers
                .mainThread()).subscribe(onGetShopInfoSubscriber());
    }

    public void cancelGetShopInfo() {
//        onGetShopInfoSubs.unsubscribe(); TODO Update neh kelak
    }

    private Subscriber<Response<TkpdResponse>> onGetShopInfoSubscriber() {
        return new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                Kirisame.print("Coba");
            }

            @Override
            public void onError(Throwable e) {
                getShopInfoListener.onFailure();
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                response.errorBody();
                if (response.isSuccessful()) {
                    if (!response.body().isError())
                        getShopInfoListener.onSuccess(response.body().getStringData());
                    else
                        getShopInfoListener.onError(response.body().getErrorMessages().get(0));
                } else {
                    onShopResponseError(response.code());
                }
            }
        };
    }

    private void onShopResponseError(int code) {
        new ErrorHandler(new ErrorListener() {
            @Override
            public void onUnknown() {
                getShopInfoListener.onFailure();
            }

            @Override
            public void onTimeout() {
                getShopInfoListener.onFailure();
            }

            @Override
            public void onServerError() {
                getShopInfoListener.onFailure();
            }

            @Override
            public void onBadRequest() {
                getShopInfoListener.onFailure();
            }

            @Override
            public void onForbidden() {
                getShopInfoListener.onFailure();
            }
        }, code);
    }

    //======================= GET Shop Etalase ======================================================================

    public void getShopEtalase() {
        autoRetry = 3;
        getShopEtalaseStart();
    }

    private void getShopEtalaseStart() {
        Observable<Response<TkpdResponse>> observable = shopService.getApi().getShopEtalase(AuthUtil.generateParams(context, paramShopInfo()));
        onGetShopEtalaseSubs = observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(onGetShopEtalaseSubscriber());
    }

    public void setOnGetShopEtalase(OnGetShopEtalase listener) {
        this.getShopEtalase = listener;
    }

    private Subscriber<Response<TkpdResponse>> onGetShopEtalaseSubscriber() {
        return new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                autoRetryEtalase();
            }

            @Override
            public void onNext(Response<TkpdResponse> tkpdResponseResponse) {
                if (tkpdResponseResponse.isSuccessful())
                    getShopEtalase.onSuccess(new Gson().fromJson(tkpdResponseResponse.body().getStringData(), EtalaseModel.class));
                else
                    onEtalaseResponseError(tkpdResponseResponse.code());
            }
        };
    }

    private void onEtalaseResponseError(int code) {
        new ErrorHandler(new ErrorListener() {
            @Override
            public void onUnknown() {
                autoRetryEtalase();
            }

            @Override
            public void onTimeout() {
                autoRetryEtalase();
            }

            @Override
            public void onServerError() {
                autoRetryEtalase();
            }

            @Override
            public void onBadRequest() {
                autoRetryEtalase();
            }

            @Override
            public void onForbidden() {
                autoRetryEtalase();
            }
        }, code);
    }

    private void autoRetryEtalase() {
        if (autoRetry > 0) {
            autoRetry--;
            getShopEtalaseStart();
        } else
            getShopEtalase.onFailure();
    }

    public void cancelGetShopEtalase() {
//        if(!onGetShopEtalase.isUnsubscribed()) TODO Update nanti
//            onGetShopEtalase.unsubscribe();
    }

    //======================= Misc =================================================================================

    private Map<String, String> paramShopInfo() {
        Map<String, String> params = new ArrayMap<>();
        params.put("shop_id", shopId);
        params.put("shop_domain", shopDomain);
        params.put("show_all", "1");
        return params;
    }

}
