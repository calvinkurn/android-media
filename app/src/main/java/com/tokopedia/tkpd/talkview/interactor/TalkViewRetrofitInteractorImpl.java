package com.tokopedia.tkpd.talkview.interactor;

import android.content.Context;
import android.util.Log;

import com.tokopedia.tkpd.network.apiservices.kunyit.KunyitService;
import com.tokopedia.tkpd.network.apiservices.product.TalkActService;
import com.tokopedia.tkpd.network.apiservices.product.TalkService;
import com.tokopedia.tkpd.network.apiservices.user.InboxTalkService;
import com.tokopedia.tkpd.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.network.retrofit.response.ErrorListener;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.talkview.activity.TalkViewActivity;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by stevenfredian on 4/28/16.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class TalkViewRetrofitInteractorImpl implements TalkViewRetrofitInteractor {

    TalkService talkService;
    TalkActService talkActService;
    InboxTalkService inboxTalkService;
    KunyitService kunyitService;
    CompositeSubscription compositeSubscription;

    public static TalkViewRetrofitInteractor createInstance() {
        TalkViewRetrofitInteractorImpl facade = new TalkViewRetrofitInteractorImpl();
        facade.talkService = new TalkService();
        facade.talkActService = new TalkActService();
        facade.inboxTalkService = new InboxTalkService();
        facade.kunyitService = new KunyitService();
        facade.compositeSubscription = new CompositeSubscription();
        return facade;
    }

    @Override
    public void getComment(Context context, Map<String, String> param, final String from, final GetCommentListener listener) {
        Observable<Response<TkpdResponse>> observable;
        if (from.equals(TalkViewActivity.PRODUCT_TALK) || from.equals(TalkViewActivity.SHOP_TALK)) {
            observable = talkService.getApi()
                    .getCommentTalk((AuthUtil.generateParams(context, param)));
        } else {
            observable = inboxTalkService.getApi().getDetail(AuthUtil.generateParams(context, param));
        }

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("get detail of talk" + from, e.toString());
                listener.onThrowable(e);
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    final TkpdResponse tkpdResponse = response.body();
                    if (!tkpdResponse.isError()) {
                        JSONObject result = tkpdResponse.getJsonData();
                        listener.onSuccess(result);
                    } else {
                        listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError("Network Unknown Error!");
                        }

                        @Override
                        public void onTimeout() {
                            listener.onError("Network Timeout Error!");
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError("Network Internal Server Error!");
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError("Network Bad Request Error!");
                        }

                        @Override
                        public void onForbidden() {

                        }
                    }, response.code());
                }
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void reply(Context context, Map<String, String> param, final AddCommentListener listener) {
        Observable<Response<TkpdResponse>> observable = talkActService.getApi()
                .addCommentTalk(AuthUtil.generateParams(context, param));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("Reply comment talk", e.toString());
                listener.onThrowable(e);
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    final TkpdResponse tkpdResponse = response.body();
                    if (!tkpdResponse.isError()) {
                        JSONObject result = tkpdResponse.getJsonData();
                        listener.onSuccess(result);
                    } else {
                        listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError("Network Unknown Error!");
                        }

                        @Override
                        public void onTimeout() {
                            listener.onError("Network Timeout Error!");
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError("Network Internal Server Error!");
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError("Network Bad Request Error!");
                        }

                        @Override
                        public void onForbidden() {

                        }
                    }, response.code());
                }
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void unSubscribe() {
        compositeSubscription.unsubscribe();
    }
}
