package com.tokopedia.tkpd.inboxticket.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.tkpd.inboxticket.model.giverating.GiveRating;
import com.tokopedia.tkpd.network.apiservices.user.TicketActService;
import com.tokopedia.tkpd.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.network.retrofit.response.ErrorListener;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.network.retrofit.utils.AuthUtil;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Nisie on 4/20/16.
 */
public class InboxTicketActRetrofitInteractorImpl implements InboxTicketActRetrofitInteractor {

    private static final String TAG = InboxTicketRetrofitInteractorImpl.class.getSimpleName();
    private static final String DEFAULT_MSG_ERROR = "Terjadi Kesalahan, Mohon ulangi beberapa saat lagi";

    private final CompositeSubscription compositeSubscription;
    private final TicketActService ticketActService;

    public InboxTicketActRetrofitInteractorImpl() {
        this.compositeSubscription = new CompositeSubscription();
        this.ticketActService = new TicketActService();
    }


    @Override
    public void setRating(@NonNull final Context context, @NonNull final Map<String, String> params, @NonNull final SetRatingInboxTicketListener listener) {
        Observable<Response<TkpdResponse>> observable = ticketActService.getApi()
                .giveRating(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException) {
                    listener.onNoInternetConnection();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout("Timeout connection," +
                            " Mohon ulangi beberapa saat lagi");
                } else {
                    listener.onError("Terjadi Kesalahan, " +
                            "Mohon ulangi beberapa saat lagi");
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        listener.onSuccess(response.body().convertDataObj(GiveRating.class));

                    } else {
                        if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().toString());
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeout("Timeout connection," +
                                    " Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onServerError() {
                            listener.onError("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
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
