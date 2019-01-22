package com.tokopedia.logisticaddaddress.service;

import android.accounts.NetworkErrorException;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.logisticaddaddress.R;
import com.tokopedia.logisticaddaddress.di.AddressModule;
import com.tokopedia.logisticaddaddress.di.DaggerAddressComponent;
import com.tokopedia.logisticaddaddress.utils.NetworkParam;
import com.tokopedia.logisticdata.data.apiservice.PeopleActApi;
import com.tokopedia.logisticdata.data.module.qualifier.LogisticPeopleActApiQualifier;
import com.tokopedia.logisticdata.data.module.qualifier.LogisticUserSessionQualifier;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.net.SocketTimeoutException;

import javax.inject.Inject;

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

    private String action;
    private String addressID;
    private ResultReceiver receiver;

    @Inject
    @LogisticPeopleActApiQualifier
    PeopleActApi peopleActApi;
    @Inject
    @LogisticUserSessionQualifier
    UserSessionInterface userSession;

    public ManagePeopleAddressService() {
        super("ManagePeopleAddressService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAddressComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .addressModule(new AddressModule()).build().inject(this);
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
        peopleActApi
                .editDefaultAddress(AuthUtil.generateParamsNetwork(
                        userSession.getUserId(), userSession.getDeviceId(), NetworkParam.paramEditDefaultAddress(addressId))
                )
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(getDefaultSubscriber());
    }

    private void handleActionDeleteAddress(String addressId) {
        peopleActApi
                .deleteAddress(AuthUtil.generateParamsNetwork(
                        userSession.getUserId(), userSession.getDeviceId(), NetworkParam.paramDeleteAddress(addressId))
                )
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(getDefaultSubscriber());
    }

    private Subscriber<Response<TokopediaWsV4Response>> getDefaultSubscriber() {
        return new Subscriber<Response<TokopediaWsV4Response>>() {
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
            public void onNext(Response<TokopediaWsV4Response> response) {
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
                    ErrorHandler.getErrorMessage(getApplicationContext(), new NetworkErrorException());
                }
            }
        };
    }
}
