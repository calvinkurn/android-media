package com.tokopedia.core.manage.people.address.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.tokopedia.core2.R;
import com.tokopedia.core.manage.people.address.datamanager.NetworkParam;
import com.tokopedia.core.network.apiservices.user.PeopleActService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;

import java.net.SocketTimeoutException;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class ManagePeopleAddressService extends IntentService {

    private static final String TAG = ManagePeopleAddressService.class.getSimpleName();

    public static final String ACTION_SET_DEFAULT_ADDRESS = "com.tokopedia.tkpd.manage.people.address.service.action.ACTION_SET_DEFAULT_ADDRESS";
    public static final String ACTION_DELETE_ADDRESS = "com.tokopedia.tkpd.manage.people.address.service.action.ACTION_DELETE_ADDRESS";

    public static final String EXTRA_PARAM_ACTION_TYPE = "EXTRA_PARAM_ACTION_TYPE";
    public static final String EXTRA_PARAM_NETWORK_ERROR_TYPE = "EXTRA_PARAM_NETWORK_ERROR_TYPE";
    public static final String EXTRA_PARAM_NETWORK_ERROR_MESSAGE = "EXTRA_PARAM_NETWORK_ERROR_MESSAGE";

    public static final String EXTRA_PARAM_ADDRESS_ID = "com.tokopedia.tkpd.manage.people.address.service.extra.EXTRA_PARAM_ADDRESS_ID";
    public static final String EXTRA_PARAM_RECEIVER = "com.tokopedia.tkpd.manage.people.address.service.extra.EXTRA_PARAM_RECEIVER";

    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 0;

    public static final int STATUS_TIME_OUT = 98;

    public static final String MESSAGE_TIMEOUT_INFO = "Network Timeout";
    public static final String MESSAGE_UNKNOWN_INFO = "Network Error";
    public static final String MESSAGE_SERVER_INFO = "Network Server Error";
    public static final String MESSAGE_BAD_REQUEST_INFO = "Network Bad Request";
    public static final String MESSAGE_FORBIDDEN_INFO = "Network Forbidden";

    private String action;
    private String addressID;
    private ResultReceiver receiver;

    public ManagePeopleAddressService() {
        super("ManagePeopleAddressService");
    }

    public static void startActionSetDefaultAddress(Context context,
                                                    String addressId,
                                                    ManagePeopleAddressReceiver mReceiver) {
        Intent intent = new Intent(context, ManagePeopleAddressService.class);
        intent.setAction(ACTION_SET_DEFAULT_ADDRESS);
        intent.putExtra(EXTRA_PARAM_ADDRESS_ID, addressId);
        intent.putExtra(EXTRA_PARAM_RECEIVER, mReceiver);
        context.startService(intent);
    }

    @SuppressWarnings("unused")
    public static void startActionDeleteAddress(Context context,
                                                String addressId,
                                                ManagePeopleAddressReceiver mReceiver) {
        Intent intent = new Intent(context, ManagePeopleAddressService.class);
        intent.setAction(ACTION_DELETE_ADDRESS);
        intent.putExtra(EXTRA_PARAM_ADDRESS_ID, addressId);
        intent.putExtra(EXTRA_PARAM_RECEIVER, mReceiver);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            addressID = intent.getStringExtra(EXTRA_PARAM_ADDRESS_ID);
            receiver = intent.getParcelableExtra(EXTRA_PARAM_RECEIVER);
            action = intent.getAction();

            switch (action) {
                case ACTION_SET_DEFAULT_ADDRESS:
                    handleActionSetDefaultAddress(addressID);
                    break;
                case ACTION_DELETE_ADDRESS:
                    handleActionDeleteAddress(addressID);
                    break;
                default:
                    throw new UnsupportedOperationException("Not yet implemented: " + action);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionSetDefaultAddress(String addressId) {
        PeopleActService actService = new PeopleActService();
        actService.getApi()
                .editDefaultAddress(AuthUtil.generateParams(getApplicationContext(), NetworkParam.paramEditDefaultAddress(addressId)))
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(getDefaultSubscriber());
    }

    private void handleActionDeleteAddress(String addressId) {
        PeopleActService actService = new PeopleActService();
        actService.getApi()
                .deleteAddress(AuthUtil.generateParams(getApplicationContext(), NetworkParam.paramDeleteAddress(addressId)))
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(getDefaultSubscriber());
    }

    private Subscriber<Response<TkpdResponse>> getDefaultSubscriber() {
        return new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                String message;
                int statusRequest;

                if (e instanceof SocketTimeoutException) {
                    statusRequest = STATUS_TIME_OUT;
                    message = getString(R.string.default_request_error_timeout);
                } else {
                    statusRequest = STATUS_ERROR;
                    message = getString(R.string.default_request_error_unknown);
                }

                Log.d(TAG + "-step6", message);
                Bundle resultData = new Bundle();
                resultData.putString(EXTRA_PARAM_ACTION_TYPE, action);
                resultData.putString(EXTRA_PARAM_ADDRESS_ID, addressID);
                resultData.putInt(EXTRA_PARAM_NETWORK_ERROR_TYPE, statusRequest);
                resultData.putString(EXTRA_PARAM_NETWORK_ERROR_MESSAGE, message);
                receiver.send(STATUS_ERROR, resultData);
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        switch (action) {
                            case ACTION_SET_DEFAULT_ADDRESS:
                                // nothing spesific at the end develop, join in default section
                                break;
                            case ACTION_DELETE_ADDRESS:
                                // nothing spesific at the end develop, join in default section
                                break;
                            default:
                                break;
                        }
                        Bundle resultData = new Bundle();
                        resultData.putString(EXTRA_PARAM_ACTION_TYPE, action);
                        resultData.putString(EXTRA_PARAM_ADDRESS_ID, addressID);
                        receiver.send(STATUS_FINISHED, resultData);
                    } else {
                        throw new RuntimeException(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            throw new RuntimeException(MESSAGE_UNKNOWN_INFO);
                        }

                        @Override
                        public void onTimeout() {
                            throw new RuntimeException(MESSAGE_TIMEOUT_INFO);
                        }

                        @Override
                        public void onServerError() {
                            throw new RuntimeException(MESSAGE_SERVER_INFO);
                        }

                        @Override
                        public void onBadRequest() {
                            throw new RuntimeException(MESSAGE_BAD_REQUEST_INFO);
                        }

                        @Override
                        public void onForbidden() {
                            throw new RuntimeException(MESSAGE_FORBIDDEN_INFO);
                        }
                    }, response.code());
                }
            }
        };
    }
}
