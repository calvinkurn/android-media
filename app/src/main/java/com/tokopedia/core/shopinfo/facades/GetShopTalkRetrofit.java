package com.tokopedia.core.shopinfo.facades;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.google.gson.Gson;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.shopinfo.facades.authservices.ShopService;
import com.tokopedia.core.shopinfo.models.talkmodel.TalkModel;

import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Tkpd_Eka on 12/10/2015.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class GetShopTalkRetrofit {

    public interface OnGetShopTalkListener {
        void onSuccess(TalkModel model);

        void onFailure();
    }

    private Context context;
    private ShopService service;
    private String shopId;
    private String shopDomain;

    private OnGetShopTalkListener onGetShopTalkListener;

    private Subscription onGetShopTalkSubs;

    public GetShopTalkRetrofit(Context context, String shopId, String shopDomain) {
        this.context = context;
        this.shopId = shopId;
        this.shopDomain = shopDomain;
        service = new ShopService();
    }

    public void setOnGetShopTalkListener(OnGetShopTalkListener listener) {
        this.onGetShopTalkListener = listener;
    }

    public void getShopTalk(String page) {
        Observable<Response<TkpdResponse>> observable = service.getApi().getShopTalk(AuthUtil.generateParams(context, paramShopTalk(page)));
        onGetShopTalkSubs = observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(onGetShopTalk());
    }

    public boolean isFetching() {
        if (onGetShopTalkSubs != null)
            return !onGetShopTalkSubs.isUnsubscribed();
        else return false;
    }

    private Observer<Response<TkpdResponse>> onGetShopTalk() {
        return new Observer<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                onGetShopTalkListener.onFailure();
            }

            @Override
            public void onNext(Response<TkpdResponse> tkpdResponseResponse) {
                if (tkpdResponseResponse.isSuccessful())
                    onGetShopTalkListener.onSuccess(new Gson().fromJson(tkpdResponseResponse.body().getStringData(), TalkModel.class));
                else
                    onGetShopTalkListener.onFailure();
            }
        };
    }

    private Map<String, String> paramShopTalk(String page) {
        ArrayMap<String, String> param = new ArrayMap<>();
        param.put("shop_domain", shopDomain);
        param.put("shop_id", shopId);
        param.put("page", page);
        return param;
    }

}
