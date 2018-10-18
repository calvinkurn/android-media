package com.tokopedia.logisticaddaddress.data.cloud;

import android.content.Context;
import android.util.Log;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.logisticaddaddress.di.AddressScope;
import com.tokopedia.logisticdata.data.apiservice.PeopleActApi;
import com.tokopedia.logisticdata.data.entity.address.GetPeopleAddress;
import com.tokopedia.network.utils.MapNulRemover;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created on 5/19/16.
 */
@AddressScope
public class RetrofitInteractorImpl implements RetrofitInteractor {

    private static final String TAG = RetrofitInteractorImpl.class.getSimpleName();

    private final CompositeSubscription compsoiteSubscription;
    private final PeopleActApi peopleService;

    @Inject
    public RetrofitInteractorImpl(PeopleActApi peopleActApi) {
        this.compsoiteSubscription = new CompositeSubscription();
        this.peopleService = peopleActApi;

    }

    @Override
    public void unsubscribe() {
        compsoiteSubscription.unsubscribe();
    }

    @Override
    public void getPeopleAddress(final Context context,
                                 final Map<String, String> params,
                                 final GetPeopleAddressListener listener) {
        listener.onPreExecute(context, params);

        Observable<Response<TokopediaWsV4Response>> observable = peopleService.getAddress(MapNulRemover.removeNull(params));

        Subscriber<Response<TokopediaWsV4Response>> subscriber = new Subscriber<Response<TokopediaWsV4Response>>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof IOException) {
                    listener.onTimeOut(new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            getPeopleAddress(context, params, listener);
                        }
                    });
                } else {
                    listener.onError(null, new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            getPeopleAddress(context, params, listener);
                        }
                    });
                }
            }

            @Override
            public void onNext(Response<TokopediaWsV4Response> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        GetPeopleAddress data =
                                response.body().convertDataObj(GetPeopleAddress.class);
                        if (!data.getList().isEmpty()) {
                            listener.onSuccess(context, params, data);
                        } else {
                            listener.onEmptyList(context, params, data);
                        }
                    } else {
                        if (response.body().isNullData()) {
                            listener.onNullData();
                        } else {
                            listener.onError(
                                    response.body().getErrorMessages().get(0),
                                    new NetworkErrorHelper.RetryClickedListener() {
                                        @Override
                                        public void onRetryClicked() {
                                            getPeopleAddress(context, params, listener);
                                        }
                                    });
                        }
                    }
                } else {
                    listener.onError(response.message(), new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            getPeopleAddress(context, params, listener);
                        }
                    });
                }
            }
        };

        compsoiteSubscription.add(
                observable.subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber)
        );
    }
}
