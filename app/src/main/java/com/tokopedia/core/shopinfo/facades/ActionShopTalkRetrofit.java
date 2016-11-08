package com.tokopedia.core.shopinfo.facades;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.shopinfo.facades.authservices.ActionService;

import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Tkpd_Eka on 12/14/2015.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class ActionShopTalkRetrofit {

    public interface OnActionShopListener {
        void onSuccess();

        void onFailure();
    }

    private Context context;
    private String shopId;
    private String shopDomain;
    private ActionService actionService;

    private OnActionShopListener listener;

    private Subscription actionSubscription;

    public ActionShopTalkRetrofit(Context context, String shopId, String shopDomain) {
        this.context = context;
        this.shopId = shopId;
        this.shopDomain = shopDomain;
        actionService = new ActionService();
    }

    public void setListener(OnActionShopListener listener) {
        this.listener = listener;
    }

    public void actionFollowTalk(String prodId, String talkId) {
        Observable<Response<TkpdResponse>> observable = actionService.getApi().actionFollowTalk(AuthUtil.generateParams(context, paramFollowTalk(prodId, talkId)));
        actionSubscription = observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(onActionTalk());
    }

    private Map<String, String> paramFollowTalk(String prodId, String talkId) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("shop_id", shopId);
        map.put("shop_domain", shopDomain);
        map.put("product_id", prodId);
        map.put("talk_id", talkId);
        return map;
    }

    //============================================================================================================================

    public void actionReportTalk(String prodId, String talkId, String message) {
        Observable<Response<TkpdResponse>> observable = actionService.getApi().actionReportTalk(AuthUtil.generateParams(context, paramReportTalk(prodId, talkId, message)));
        actionSubscription = observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(onActionTalk());
    }

    private Observer<Response<TkpdResponse>> onActionTalk() {
        return new Observer<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Response<TkpdResponse> tkpdResponseResponse) {
                if (tkpdResponseResponse.isSuccessful())
                    listener.onSuccess();
                else
                    listener.onFailure();
            }
        };
    }


    private Map<String, String> paramReportTalk(String prodId, String talkId, String message) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("shop_id", shopId);
        map.put("shop_domain", shopDomain);
        map.put("product_id", prodId);
        map.put("talk_id", talkId);
        map.put("text_message", message);
        return map;
    }

    //===============================================================================================================================================

    public void actionDeleteTalk(String talkId) {
        Observable<Response<TkpdResponse>> observable = actionService.getApi().actionDeleteTalk(AuthUtil.generateParams(context, paramDeleteTalk(talkId)));
        observable.subscribeOn(Schedulers.newThread()).unsubscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(onActionDelete());
    }

    private Subscriber<Response<TkpdResponse>> onActionDelete() {
        return new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Response<TkpdResponse> tkpdResponseResponse) {
                tkpdResponseResponse.body();
                listener.onSuccess();
            }
        };
    }

    private Map<String, String> paramDeleteTalk(String talkId) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("shop_id", shopId);
        map.put("shop_domain", shopDomain);
        map.put("talk_id", talkId);
        return map;
    }
}
