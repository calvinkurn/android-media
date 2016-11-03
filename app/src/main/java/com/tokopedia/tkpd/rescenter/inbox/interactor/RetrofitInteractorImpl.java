package com.tokopedia.tkpd.rescenter.inbox.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.tkpd.network.NetworkErrorHelper;
import com.tokopedia.tkpd.network.apiservices.user.InboxResCenterService;
import com.tokopedia.tkpd.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.network.retrofit.response.ErrorListener;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.network.retrofit.utils.MapNulRemover;
import com.tokopedia.tkpd.rescenter.inbox.model.ResCenterInboxData;

import java.io.IOException;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created on 4/6/16.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class RetrofitInteractorImpl implements RetrofitInteractor {

    private static final String TAG = RetrofitInteractorImpl.class.getSimpleName();

    private final InboxResCenterService inboxResCenterService;
    private final CompositeSubscription compositeSubscription;

    public RetrofitInteractorImpl() {
        this.compositeSubscription = new CompositeSubscription();
        this.inboxResCenterService = new InboxResCenterService();
    }

    @Override
    public void unsubscribe() {
        compositeSubscription.unsubscribe();
    }

    @Override
    public void getResCenterInbox(@NonNull final Context context,
                                  @NonNull final Map<String, String> params,
                                  @NonNull final ResCenterInboxListener listener) {
        listener.onPreExecute(params);

        Observable<Response<TkpdResponse>> observable =
                inboxResCenterService
                        .getApi()
                        .getResCenter(MapNulRemover.removeNull(params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof IOException) {
                    listener.onTimeout(new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            getResCenterInbox(context, params, listener);
                        }
                    });
                } else {
                    listener.onError(null);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        ResCenterInboxData data = response.body().convertDataObj(ResCenterInboxData.class);
                        if (data.getList().size() != 0) {
                            listener.onSuccess(params, data);
                        } else {
                            listener.onNullData();
                        }
                    } else {
                        if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(null);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeout(new NetworkErrorHelper.RetryClickedListener() {
                                @Override
                                public void onRetryClicked() {
                                    getResCenterInbox(context, params, listener);
                                }
                            });
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(null);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(null);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(null);
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
}
