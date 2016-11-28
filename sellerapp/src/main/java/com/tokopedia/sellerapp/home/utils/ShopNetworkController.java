package com.tokopedia.sellerapp.home.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.tokopedia.tkpd.home.model.shopmodel.ShopModel;
import com.tokopedia.tkpd.network.apiservices.shop.ShopService;
import com.tokopedia.tkpd.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.network.retrofit.response.ErrorListener;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.network.retrofit.utils.AuthUtil;

import java.util.Map;

import retrofit.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by normansyahputa on 8/30/16.
 */

public class ShopNetworkController extends BaseNetworkController {
    private ShopService shopService;

    public ShopNetworkController(Context context, ShopService shopService, Gson gson){
        super(context, gson);
        this.shopService = shopService;
    }

    public void getShopInfo(String userid, String deviceId, ShopInfoParam shopInfoParam, final GetShopInfo getShopInfo){
        getShopInfo(userid, deviceId, shopInfoParam)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getShopInfo.onError(e);
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccess()) {
                    if(!response.body().isError()) {
                        String stringData = response.body().getStringData();
                        Log.d("STUART", "getShopInfo : onNext : "+stringData);
                        ShopModel shopModel = gson.fromJson(stringData, ShopModel.class);
                        getShopInfo.onSuccess(shopModel);
                    }else {
                        throw new MessageErrorException(response.body().getErrorMessages().get(0));
                    }
                } else {
                    onResponseError(response.code(), getShopInfo);
                }
            }
        });
    }

    public static void onResponseError(int code, final CommonListener commonListener) {
        new ErrorHandler(new ErrorListener() {
            @Override
            public void onUnknown() {
                commonListener.onError(new MessageErrorException("unknown"));
            }

            @Override
            public void onTimeout() {
                commonListener.onError(new MessageErrorException("timeout"));
            }

            @Override
            public void onServerError() {
                commonListener.onError(new MessageErrorException("server_error"));
            }

            @Override
            public void onBadRequest() {
                commonListener.onError(new MessageErrorException("bad_request"));
            }

            @Override
            public void onForbidden() {
                commonListener.onError(new MessageErrorException("forbidden"));
            }
        }, code);
    }

    public Observable<Response<TkpdResponse>> getShopInfo(String userid, String deviceId, ShopInfoParam shopInfoParam){
        return shopService.getApi().getInfo(AuthUtil.generateParams(userid, deviceId, paramShopInfo(shopInfoParam)));
    }

    public Map<String, String> paramShopInfo(ShopInfoParam shopInfoParam) {
        Map<String, String> params = new NonNullStringMap();
        params.put("shop_id", shopInfoParam.shopId);
        params.put("shop_domain", shopInfoParam.shopDomain);
        params.put("show_all", "1");
        return params;
    }

    public static class ShopInfoParam{
        public String shopId;
        public String shopDomain;
        public int showAll;
    }

    public interface CommonListener {
        void onError(Throwable e);
        void onFailure();
    }

    public interface GetShopInfo extends CommonListener {
        void onSuccess(ShopModel shopModel);
    }

    public static class MessageErrorException extends RuntimeException {

        public MessageErrorException(String message) {
            super(message);
        }
    }

    public static class ManyRequestErrorException extends RuntimeException {

        public ManyRequestErrorException(String message) {
            super(message);
        }
    }
}
