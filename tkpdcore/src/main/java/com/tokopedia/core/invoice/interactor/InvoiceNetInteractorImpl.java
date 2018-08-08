package com.tokopedia.core.invoice.interactor;

import android.content.Context;
import android.util.Log;

import com.tokopedia.core.network.apiservices.user.InvoiceService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;

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
 * InvoiceNetInteractorImpl
 * Created by Angga.Prasetiyo on 15/06/2016.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class InvoiceNetInteractorImpl implements InvoiceNetInteractor {
    private static final String TAG = InvoiceNetInteractorImpl.class.getSimpleName();
    private final InvoiceService invoiceService;
    private final CompositeSubscription compositeSubscription;
    private static final String DEFAULT_MSG_ERROR = "Terjadi Kesalahan";
    private static final String CONNECTION_MSG_ERROR = "Tidak ada koneksi internet";

    public InvoiceNetInteractorImpl() {
        invoiceService = new InvoiceService();
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void renderInvoice(final Context context, Map<String, String> params,
                              final OnRenderInvoice listener) {
        Observable<Response<String>> observable = invoiceService.getApi().render(params);
        Subscriber<Response<String>> subscriber = new Subscriber<Response<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException) {
                    listener.onNoInternetConnection(CONNECTION_MSG_ERROR);
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout(DEFAULT_MSG_ERROR);
                } else {
                    listener.onError(DEFAULT_MSG_ERROR);
                }
            }

            @Override
            public void onNext(Response<String> stringResponse) {
                if (stringResponse.isSuccessful()) {
                    listener.onSuccess(stringResponse.body());
                } else {
                    if (stringResponse.code() == 404)
                        listener.onError("Invoice tidak ditemukan");
                    else {
                        new ErrorHandler(new ErrorListener() {
                            @Override
                            public void onUnknown() {
                                listener.onError(DEFAULT_MSG_ERROR);
                            }

                            @Override
                            public void onTimeout() {
                                listener.onTimeout(DEFAULT_MSG_ERROR);
                            }

                            @Override
                            public void onServerError() {
                                listener.onError(DEFAULT_MSG_ERROR);
                            }

                            @Override
                            public void onBadRequest() {
                                listener.onError(DEFAULT_MSG_ERROR);
                            }

                            @Override
                            public void onForbidden() {
                                listener.onError(DEFAULT_MSG_ERROR);
                            }
                        }, stringResponse.code());
                    }
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }
}
