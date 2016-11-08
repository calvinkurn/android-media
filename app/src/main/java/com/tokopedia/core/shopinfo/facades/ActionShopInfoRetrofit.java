package com.tokopedia.core.shopinfo.facades;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.shopinfo.facades.authservices.ActionService;

import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Tkpd_Eka on 12/7/2015.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class ActionShopInfoRetrofit {

    public interface OnActionToggleFavListener {
        void onSuccess();

        void onFailure();
    }

    public static String SOURCE_SHOP = "shop_detail";

    private Context context;
    private ActionService actionService;
    private String shopId;
    private String shopDomain;
    private String adKey;
    private OnActionToggleFavListener onActionToggleFavListener;
    private Subscription onActionToggleFavSubs;

    public ActionShopInfoRetrofit(Context context, String id, String domain, String adKey) {
        this.context = context;
        this.shopId = id;
        this.shopDomain = domain;
        this.adKey = adKey;
        actionService = new ActionService();
    }

    public void setOnActionToggleFavListener(OnActionToggleFavListener listener) {
        this.onActionToggleFavListener = listener;
    }

    public void actionToggleFav() {
        Observable<Response<TkpdResponse>> observable = actionService.getApi().actionFavoriteShop(AuthUtil.generateParams(context, paramToggleFav()));
        onActionToggleFavSubs = observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(onActionToggleFav());
    }

    public void cancelToggleFav() {
//        onActionToggleFavSubs.unsubscribe(); TODO Update nih kelak
    }

    private Observer<Response<TkpdResponse>> onActionToggleFav() {
        return new Observer<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Response<TkpdResponse> tkpdResponseResponse) {
                tkpdResponseResponse.body().getStringData();
                if (tkpdResponseResponse.isSuccessful())
                    onActionToggleFavListener.onSuccess();
                else {
                    onActionToggleFavListener.onFailure();
                    onResponseError(tkpdResponseResponse.code());
                }
            }
        };
    }

    private void onResponseError(int code) {
        new ErrorHandler(new ErrorListener() {
            @Override
            public void onUnknown() {

            }

            @Override
            public void onTimeout() {

            }

            @Override
            public void onServerError() {

            }

            @Override
            public void onBadRequest() {

            }

            @Override
            public void onForbidden() {

            }
        }, code);
    }

    private Map<String, String> paramToggleFav() {
        Map<String, String> params = new ArrayMap<>();
        params.put("shop_id", shopId);
        params.put("shop_domain", shopDomain);
        params.put("src", SOURCE_SHOP);
        params.put("ad_key", adKey);
        return params;
    }

}
