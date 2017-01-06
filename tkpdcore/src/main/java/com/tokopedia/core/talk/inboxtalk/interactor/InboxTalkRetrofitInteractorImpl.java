package com.tokopedia.core.talk.inboxtalk.interactor;

import android.content.Context;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.tokopedia.core.R;
import com.tokopedia.core.network.apiservices.kunyit.KunyitService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.talk.inboxtalk.model.InboxTalkListModel;
import com.tokopedia.core.talk.inboxtalk.presenter.InboxTalkPresenterImpl;
import com.tokopedia.core.util.NewPagingHandler;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by stevenfredian on 4/5/16.
 * <p>
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class InboxTalkRetrofitInteractorImpl implements InboxTalkRetrofitInteractor {

    InboxTalkPresenterImpl presenter;
    KunyitService kunyitService;
    CompositeSubscription subscription;


    public static InboxTalkRetrofitInteractor createInstance(InboxTalkPresenterImpl inboxTalkPresenter) {
        InboxTalkRetrofitInteractorImpl facade = new InboxTalkRetrofitInteractorImpl();
        facade.presenter = inboxTalkPresenter;
        facade.kunyitService = new KunyitService();
        facade.subscription = new CompositeSubscription();
        return facade;
    }

    @Override
    public void getInboxTalk(final Context context, Map<String, String> param, final GetInboxTalkListener listener) {
        Observable<Response<TkpdResponse>> observable = kunyitService.getApi()
                .getInboxTalk(AuthUtil.generateParams(context, param));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("Get Inbox Talk", e.toString());
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
                            listener.onError(context.getResources().getString(R.string.default_request_error_unknown));
                        }

                        @Override
                        public void onTimeout() {
                            listener.onError(context.getResources().getString(R.string.default_request_error_timeout));
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(context.getResources().getString(R.string.default_request_error_internal_server));
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(context.getResources().getString(R.string.default_request_error_unknown));
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(context.getResources().getString(R.string.default_request_error_forbidden_auth));
                        }
                    }, response.code());
                }
            }
        };

        subscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));

    }

    @Override
    public InboxTalkListModel parseList(JSONObject result) {
        return new GsonBuilder().create().fromJson(result.toString(), InboxTalkListModel.class);
    }

    @Override
    public void unSubscribe() {
        subscription.unsubscribe();
    }

    @Override
    public NewPagingHandler.PagingBean parsePaging(JSONObject result) {
        if (result == null)
            return null;
        return new GsonBuilder().create().fromJson(result.optString("paging"), NewPagingHandler.PagingBean.class);
    }


//
//    @Override
//    public void followTalk(Context context, Map<String, String> param, final FollowListener listener) {
//        Observable<Response<TkpdResponse>> observable = talkActService.getApi()
//                .followProductTalk(AuthUtil.generateParams(context, param));
//        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.e("ASDASD", e.toString());
//                listener.onThrowable(e);
//            }
//
//            @Override
//            public void onNext(Response<TkpdResponse> response) {
//                if (response.isSuccess()) {
//                    final TkpdResponse tkpdResponse = response.body();
//                    if (!tkpdResponse.isError()) {
//                        JSONObject result = tkpdResponse.getJsonData();
//                        listener.onSuccess(result);
//                    } else {
//                        listener.onError(response.body().getErrorMessages().get(0));
//                    }
//                }
//                else {
//                    new ErrorHandler(new ErrorListener() {
//                        @Override
//                        public void onUnknown() {
//                            listener.onError("Network Unknown Error!");
//                        }
//
//                        @Override
//                        public void onTimeout() {
//                            listener.onError("Network Timeout Error!");
//                            listener.onTimeout();
//                        }
//
//                        @Override
//                        public void onServerError() {
//                            listener.onError("Network Internal Server Error!");
//                        }
//
//                        @Override
//                        public void onBadRequest() {
//                            listener.onError("Network Bad Request Error!");
//                        }
//
//                        @Override
//                        public void onForbidden() {
//
//                        }
//                    }, response.code());
//                }
//            }
//        };
//
//        subscription.add(observable.subscribeOn(Schedulers.newThread())
//                .unsubscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber));
//    }
//
//    @Override
//    public void deleteTalk(Context context, Map<String, String> param, final DeleteListener listener) {
//        Observable<Response<TkpdResponse>> observable = talkActService.getApi()
//                .deleteProductTalk(AuthUtil.generateParams(context, param));
//        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.e("ASDASD", e.toString());
//                listener.onThrowable(e);
//            }
//
//            @Override
//            public void onNext(Response<TkpdResponse> response) {
//                if (response.isSuccess()) {
//                    final TkpdResponse tkpdResponse = response.body();
//                    if (!tkpdResponse.isError()) {
//                        JSONObject result = tkpdResponse.getJsonData();
//                        listener.onSuccess(result);
//                    } else {
//                        listener.onError(response.body().getErrorMessages().get(0));
//                    }
//                }
//                else {
//                    new ErrorHandler(new ErrorListener() {
//                        @Override
//                        public void onUnknown() {
//                            listener.onError("Network Unknown Error!");
//                        }
//
//                        @Override
//                        public void onTimeout() {
//                            listener.onError("Network Timeout Error!");
//                            listener.onTimeout();
//                        }
//
//                        @Override
//                        public void onServerError() {
//                            listener.onError("Network Internal Server Error!");
//                        }
//
//                        @Override
//                        public void onBadRequest() {
//                            listener.onError("Network Bad Request Error!");
//                        }
//
//                        @Override
//                        public void onForbidden() {
//
//                        }
//                    }, response.code());
//                }
//            }
//        };
//
//        subscription.add(observable.subscribeOn(Schedulers.newThread())
//                .unsubscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber));
//    }
//
//    @Override
//    public void reportTalk(Context context, Map<String, String> param, final ReportListener listener) {
//        Observable<Response<TkpdResponse>> observable = talkActService.getApi()
//                .reportProductTalk(AuthUtil.generateParams(context, param));
//        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.e("ASDASD", e.toString());
//                listener.onThrowable(e);
//            }
//
//            @Override
//            public void onNext(Response<TkpdResponse> response) {
//                if (response.isSuccess()) {
//                    final TkpdResponse tkpdResponse = response.body();
//                    if (!tkpdResponse.isError()) {
//                        JSONObject result = tkpdResponse.getJsonData();
//                        listener.onSuccess(result);
//                    } else {
//                        listener.onError(response.body().getErrorMessages().get(0));
//                    }
//                }
//                else {
//                    new ErrorHandler(new ErrorListener() {
//                        @Override
//                        public void onUnknown() {
//                            listener.onError("Network Unknown Error!");
//                        }
//
//                        @Override
//                        public void onTimeout() {
//                            listener.onError("Network Timeout Error!");
//                            listener.onTimeout();
//                        }
//
//                        @Override
//                        public void onServerError() {
//                            listener.onError("Network Internal Server Error!");
//                        }
//
//                        @Override
//                        public void onBadRequest() {
//                            listener.onError("Network Bad Request Error!");
//                        }
//
//                        @Override
//                        public void onForbidden() {
//
//                        }
//                    }, response.code());
//                }
//            }
//        };
//
//        subscription.add(observable.subscribeOn(Schedulers.newThread())
//                .unsubscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber));
//    }

}
