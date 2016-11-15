package com.tokopedia.core.payment.interactor;

import android.content.Context;

import com.tokopedia.core.R;
import com.tokopedia.core.network.apiservices.transaction.TXActService;
import com.tokopedia.core.network.apiservices.transaction.TXCartActService;
import com.tokopedia.core.network.apiservices.transaction.TXCartService;
import com.tokopedia.core.network.apiservices.transaction.TXService;
import com.tokopedia.core.network.apiservices.transaction.TXSprintAsiaService;
import com.tokopedia.core.network.apiservices.transaction.TXVoucherService;
import com.tokopedia.core.network.apiservices.user.PeopleActService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.DialogNoConnection;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.payment.model.responsecalculateshipping.CalculateShipping;
import com.tokopedia.core.payment.model.responsecartstep1.CarStep1Data;
import com.tokopedia.core.payment.model.responsecartstep2.CartStep2Data;
import com.tokopedia.core.payment.model.responsecreditcardstep1.CreditCardStep1Data;
import com.tokopedia.core.payment.model.responsedynamicpayment.DynamicPaymentData;
import com.tokopedia.core.payment.model.responsethankspayment.ThanksPaymentData;
import com.tokopedia.core.payment.model.responsevoucher.VoucherCodeData;

import org.json.JSONException;

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
 * @author by Angga.Prasetiyo on 19/05/2016.
 *         migrate retrofit 2 by Angga.Prasetiyo
 */
public class PaymentNetInteractorImpl implements PaymentNetInteractor {
    private static final String MESSAGE_ERROR_UNDEFINED_PAYMENT_METHOD
            = "Metode Pembayaran tidak dikenali. Silahkan coba kembali";
    private static final String KEY_FLAG_IS_SUCCESS = "is_success";

    private final TXActService txActService;
    private final TXCartActService txCartActService;
    private final TXCartService txCartService;
    private final TXVoucherService txVoucherService;
    private final CompositeSubscription compositeSubscription;
    private final TXService txService;
    private final TXSprintAsiaService txSprintAsiaService;
    private final PeopleActService peopleActService;

    public PaymentNetInteractorImpl() {
        txActService = new TXActService();
        txVoucherService = new TXVoucherService();
        txService = new TXService();
        txSprintAsiaService = new TXSprintAsiaService();
        txCartActService = new TXCartActService();
        txCartService = new TXCartService();
        compositeSubscription = new CompositeSubscription();
        peopleActService = new PeopleActService();
    }

    @Override
    public void getParameterDynamicPayment(final Context context, final Map<String, String> params,
                                           final OnGetParameterDynamicPayment listener) {
        final Observable<Response<TkpdResponse>> observable = txActService.getApi()
                .getParameterDynamicPayment(AuthUtil.generateParams(context, params));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    listener.onNoConnection();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    TkpdResponse tkpdResponse = response.body();
                    if (tkpdResponse.isError()) {
                        if (!tkpdResponse.getErrorMessages().isEmpty()) {
                            listener.onError(tkpdResponse.getErrorMessages().get(0));
                        } else {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                        return;
                    }
                    if (tkpdResponse.isNullData()) {
                        listener.onError(ErrorNetMessage.MESSAGE_ERROR_NULL_DATA);
                        return;
                    }
                    listener.onSuccess(tkpdResponse.convertDataObj(DynamicPaymentData.class));
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                    }, response.code());
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.immediate())
                .unsubscribeOn(Schedulers.immediate())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void postStep1(final Context context, Map<String, String> params, final OnPostStep1 listener) {
        final Observable<Response<TkpdResponse>> observable = txService.getApi()
                .doPayment(AuthUtil.generateParams(context, params));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    listener.onNoConnection();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    TkpdResponse tkpdResponse = response.body();
                    if (tkpdResponse.isError()) {
                        if (!tkpdResponse.getErrorMessages().isEmpty()) {
                            listener.onError(tkpdResponse.getErrorMessages().get(0));
                        } else {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                        return;
                    }
                    if (tkpdResponse.isNullData()) {
                        listener.onError(ErrorNetMessage.MESSAGE_ERROR_NULL_DATA);
                        return;
                    }
                    listener.onSuccess(tkpdResponse.convertDataObj(CarStep1Data.class));
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                    }, response.code());
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.immediate())
                .unsubscribeOn(Schedulers.immediate())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void postStep2(final Context context, Map<String, String> params, final OnPostStep2 listener) {
        final Observable<Response<TkpdResponse>> observable = txService.getApi()
                .doPayment(AuthUtil.generateParams(context, params));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof UnknownHostException) {
                    listener.onNoConnection();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    TkpdResponse tkpdResponse = response.body();
                    if (tkpdResponse.isError()) {
                        if (!tkpdResponse.getErrorMessages().isEmpty()) {
                            listener.onError(tkpdResponse.getErrorMessages().get(0));
                        } else {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                        return;
                    }
                    if (tkpdResponse.isNullData()) {
                        listener.onError(ErrorNetMessage.MESSAGE_ERROR_NULL_DATA);
                        return;
                    }
                    listener.onSuccess(tkpdResponse.convertDataObj(CartStep2Data.class));
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void getThanksDynamicPayment(final Context context, final Map<String, String> params,
                                        final OnGetThanksDynamicPayment listener) {
        final Observable<Response<TkpdResponse>> observable = txActService.getApi()
                .getThanksDynamicPayment(AuthUtil.generateParams(context, params));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    DialogNoConnection.createShow(context,
                            new DialogNoConnection.ActionListener() {
                                @Override
                                public void onRetryClicked() {
                                    getThanksDynamicPayment(context, params, listener);
                                }
                            });
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    TkpdResponse tkpdResponse = response.body();
                    if (tkpdResponse.isError()) {
                        if (!tkpdResponse.getErrorMessages().isEmpty()) {
                            listener.onError(tkpdResponse.getErrorMessages().get(0));
                        } else {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                        return;
                    }
                    if (tkpdResponse.isNullData()) {
                        listener.onError(ErrorNetMessage.MESSAGE_ERROR_NULL_DATA);
                        return;
                    }
                    listener.onSuccess(tkpdResponse.convertDataObj(ThanksPaymentData.class));
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
    public void checkVoucher(final Context context, final Map<String, String> params,
                             final OnCheckVoucher listener) {
        final Observable<Response<TkpdResponse>> observable = txVoucherService.getApi()
                .checkVoucherCode(AuthUtil.generateParams(context, params));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    TkpdResponse tkpdResponse = response.body();
                    if (tkpdResponse.isError()) {
                        if (!tkpdResponse.getErrorMessages().isEmpty()) {
                            listener.onVoucherError(tkpdResponse.getErrorMessages().get(0));
                        } else {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                        return;
                    }
                    if (tkpdResponse.isNullData()) {
                        listener.onError(ErrorNetMessage.MESSAGE_ERROR_NULL_DATA);
                        return;
                    }
                    listener.onSuccess(tkpdResponse.convertDataObj(VoucherCodeData.class));
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
    public void unSubscribeObservable() {
        if (compositeSubscription.hasSubscriptions()) compositeSubscription.unsubscribe();
    }

    @Override
    public void postStep1CreditCard(final Context context, final Map<String, String> params,
                                    final OnStep1CreditCard listener) {
        final Observable<Response<TkpdResponse>> observable = txActService.getApi()
                .step1ProcessCreditCard(AuthUtil.generateParams(context, params));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    listener.onNoConnection();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    TkpdResponse tkpdResponse = response.body();
                    if (tkpdResponse.isError()) {
                        if (!tkpdResponse.getErrorMessages().isEmpty()) {
                            listener.onError(tkpdResponse.getErrorMessages().get(0));
                        } else {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                        return;
                    }
                    if (tkpdResponse.isNullData()) {
                        listener.onError(ErrorNetMessage.MESSAGE_ERROR_NULL_DATA);
                        return;
                    }
                    CreditCardStep1Data creditCardStep1Data = response.body()
                            .convertDataObj(CreditCardStep1Data.class);
                    if (creditCardStep1Data == null) {
                        listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        return;
                    }
                    if (creditCardStep1Data.getDataCredit().getFakeCcAgent() != 0) {
                        switch (creditCardStep1Data.getDataCredit().getFakeCcAgent()) {
                            case 1:
                                listener.onSuccessVeritrans(creditCardStep1Data.getDataCredit());
                                break;
                            case 2:
                                listener.onSuccessSprintAsia(creditCardStep1Data.getDataCredit());
                                break;
                            default:
                                listener.onError(MESSAGE_ERROR_UNDEFINED_PAYMENT_METHOD);
                                break;
                        }
                    } else {
                        switch (creditCardStep1Data.getDataCredit().getCcAgent()) {
                            case 1:
                                listener.onSuccessVeritrans(creditCardStep1Data.getDataCredit());
                                break;
                            case 2:
                                listener.onSuccessSprintAsia(creditCardStep1Data.getDataCredit());
                                break;
                            default:
                                listener.onError(MESSAGE_ERROR_UNDEFINED_PAYMENT_METHOD);
                                break;
                        }
                    }

                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void postSprintAsia(final Context context, final Map<String, String> params,
                               final OnSprintAsia listener) {
        final Observable<Response<String>> observable = txSprintAsiaService.getApi()
                .paymentSprintAsia(AuthUtil.generateParams(context, params));
        Subscriber<Response<String>> subscriber = new Subscriber<Response<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    DialogNoConnection.createShow(context,
                            new DialogNoConnection.ActionListener() {
                                @Override
                                public void onRetryClicked() {
                                    postSprintAsia(context, params, listener);
                                }
                            });
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(Response<String> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void updateInsurance(final Context context, final Map<String, String> params, final OnUpdateInsurance listener) {
        final Observable<Response<TkpdResponse>> observable = txCartActService.getApi()
                .editInsurance(AuthUtil.generateParams(context, params));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                listener.onError(context.getString(R.string.default_request_error_unknown));
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    TkpdResponse tkpdResponse = response.body();
                    if (tkpdResponse.isError()) {
                        if (!tkpdResponse.getErrorMessages().isEmpty()) {
                            listener.onError(tkpdResponse.getErrorMessages().get(0));
                        } else {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                        return;
                    }
                    if (tkpdResponse.isNullData()) {
                        listener.onError(ErrorNetMessage.MESSAGE_ERROR_NULL_DATA);
                        return;
                    }
                    if (!response.body().getJsonData().isNull(KEY_FLAG_IS_SUCCESS)) {
                        try {
                            int status = response.body().getJsonData()
                                    .getInt(KEY_FLAG_IS_SUCCESS);
                            String message = status == 1 ? response.body()
                                    .getStatusMessages().get(0)
                                    : response.body().getErrorMessages().get(0);
                            switch (status) {
                                case 1:
                                    listener.onSuccess(message);
                                    break;
                                default:
                                    listener.onError(message);
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
    public void updateCart(final Context context, final Map<String, String> params, final OnUpdateCart listener) {
        final Observable<Response<TkpdResponse>> observable = txCartActService.getApi()
                .editCart(AuthUtil.generateParams(context, params));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess("");
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
    public void cancelCart(final Context context, final Map<String, String> params, final OnCancelCart listener) {
        final Observable<Response<TkpdResponse>> observable = txCartActService.getApi()
                .cancelCart(AuthUtil.generateParams(context, params));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    TkpdResponse tkpdResponse = response.body();
                    if (tkpdResponse.isError()) {
                        if (!tkpdResponse.getErrorMessages().isEmpty()) {
                            listener.onError(tkpdResponse.getErrorMessages().get(0));
                        } else {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                        return;
                    }
                    if (tkpdResponse.isNullData()) {
                        listener.onError(ErrorNetMessage.MESSAGE_ERROR_NULL_DATA);
                        return;
                    }
                    if (!response.body().getJsonData().isNull(KEY_FLAG_IS_SUCCESS)) {
                        try {
                            int status = response.body().getJsonData()
                                    .getInt(KEY_FLAG_IS_SUCCESS);
                            String message = status == 1 ? response.body()
                                    .getStatusMessages().get(0)
                                    : response.body().getErrorMessages().get(0);
                            switch (status) {
                                case 1:
                                    listener.onSuccess(message);
                                    break;
                                default:
                                    listener.onError(message);
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                            );
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
    public void cancelProduct(final Context context, final Map<String, String> params, final OnCancelProduct listener) {
        final Observable<Response<TkpdResponse>> observable = txCartActService.getApi()
                .cancelCart(AuthUtil.generateParams(context, params));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    TkpdResponse tkpdResponse = response.body();
                    if (tkpdResponse.isError()) {
                        if (!tkpdResponse.getErrorMessages().isEmpty()) {
                            listener.onError(tkpdResponse.getErrorMessages().get(0));
                        } else {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                        return;
                    }
                    if (tkpdResponse.isNullData()) {
                        listener.onError(ErrorNetMessage.MESSAGE_ERROR_NULL_DATA);
                        return;
                    }
                    if (!response.body().getJsonData().isNull(KEY_FLAG_IS_SUCCESS)) {
                        try {
                            int status = response.body().getJsonData()
                                    .getInt(KEY_FLAG_IS_SUCCESS);
                            String message = status == 1 ? response.body()
                                    .getStatusMessages().get(0)
                                    : response.body().getErrorMessages().get(0);
                            switch (status) {
                                case 1:
                                    listener.onSuccess(message);
                                    break;
                                default:
                                    listener.onError(message);
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
    public void editAddress(final Context context, final Map<String, String> params, final OnEditAddress listener) {
        final Observable<Response<TkpdResponse>> observable = txCartActService.getApi()
                .editAddress(AuthUtil.generateParams(context, params));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    TkpdResponse tkpdResponse = response.body();
                    if (tkpdResponse.isError()) {
                        if (!tkpdResponse.getErrorMessages().isEmpty()) {
                            listener.onError(tkpdResponse.getErrorMessages().get(0));
                        } else {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                        return;
                    }
                    if (tkpdResponse.isNullData()) {
                        listener.onError(ErrorNetMessage.MESSAGE_ERROR_NULL_DATA);
                        return;
                    }
                    if (!response.body().getJsonData().isNull(KEY_FLAG_IS_SUCCESS)) {
                        try {
                            int status = response.body().getJsonData()
                                    .getInt(KEY_FLAG_IS_SUCCESS);
                            String message = status == 1 ? response.body()
                                    .getStatusMessages().get(0)
                                    : response.body().getErrorMessages().get(0);
                            switch (status) {
                                case 1:
                                    listener.onSuccess(message);
                                    break;
                                default:
                                    listener.onError(message);
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
    public void calculateShipping(final Context context, final Map<String, String> params, final OnCalculateShipping listener) {
        final Observable<Response<TkpdResponse>> observable = txCartService.getApi()
                .calculateCart(AuthUtil.generateParams(context, params));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.onNoConnection();
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body().convertDataObj(CalculateShipping.class));
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
    public void saveNewLocation(final Context context, final Map<String, String> params, final OnSaveNewLocation listener) {
        final Observable<Response<TkpdResponse>> observable = peopleActService.getApi()
                .editAddress(AuthUtil.generateParams(context, params));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.onNoConnection();
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    TkpdResponse tkpdResponse = response.body();
                    if (tkpdResponse.isError()) {
                        if (!tkpdResponse.getErrorMessages().isEmpty()) {
                            listener.onError(tkpdResponse.getErrorMessages().get(0));
                        } else {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                        return;
                    }
                    if (tkpdResponse.isNullData()) {
                        listener.onError(ErrorNetMessage.MESSAGE_ERROR_NULL_DATA);
                        return;
                    }
                    if (!response.body().getJsonData().isNull(KEY_FLAG_IS_SUCCESS)) {
                        try {
                            int status = response.body().getJsonData()
                                    .getInt(KEY_FLAG_IS_SUCCESS);
                            String message = status == 1 ? response.body()
                                    .getStatusMessages().get(0)
                                    : response.body().getErrorMessages().get(0);
                            switch (status) {
                                case 1:
                                    listener.onSuccess(message);
                                    break;
                                default:
                                    listener.onError(message);
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
