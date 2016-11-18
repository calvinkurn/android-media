package com.tokopedia.core.manage.people.password.intentservice;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.tokopedia.core.R;
import com.tokopedia.core.manage.people.password.model.ChangePasswordParam;
import com.tokopedia.core.network.apiservices.user.PeopleActService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;

import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ManagePasswordIntentService extends IntentService {

    private static final String ACTION_CHANGE_PASSWORD = "com.tokopedia.core.manage.people.password.intentservice.action.change_password";


    public final static String PARAM_CHANGE_PASSWORD = "change_password";
    public final static int SUCCESS_CHANGE_PASSWORD = 14;
    public final static int ERROR_CHANGE_PASSWORD = SUCCESS_CHANGE_PASSWORD +10;

    public static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final String EXTRA_BUNDLE = "EXTRA_BUNDLE";
    public static final String EXTRA_RECEIVER = "EXTRA_RECEIVER";
    public static final String EXTRA_RESULT = "EXTRA_RESULT";
    private PeopleActService peopleActService;
    ResultReceiver receiver;

    public ManagePasswordIntentService() {
        super("ManagePasswordIntentService");
    }

    public static void changePassword(Context context, Bundle map, ManagePasswordResultReceiver receiver) {
        Intent intent = new Intent(context, ManagePasswordIntentService.class);
        intent.setAction(ACTION_CHANGE_PASSWORD);
        intent.putExtra(EXTRA_BUNDLE, map);
        intent.putExtra(EXTRA_RECEIVER, receiver);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Bundle bundle = intent.getBundleExtra(EXTRA_BUNDLE);
            receiver = intent.getParcelableExtra(EXTRA_RECEIVER);
            peopleActService = new PeopleActService();
            if (ACTION_CHANGE_PASSWORD.equals(action)) {
                handleActionChangePassword(bundle);
            }
        }
    }

    private void handleActionChangePassword(Bundle bundle) {
        ChangePasswordParam param = (ChangePasswordParam) bundle.get(PARAM_CHANGE_PASSWORD);
        Map<String,String> map = param.getParamChangePassword();
        map = AuthUtil.generateParams(getBaseContext(),map);
        peopleActService.getApi().editPassword(map)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber(ACTION_CHANGE_PASSWORD));
    }

    private class Subscriber extends rx.Subscriber<Response<TkpdResponse>> {
        String type;
        final Bundle resultData = new Bundle();
        public Subscriber(String type) {
            this.type = type;
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            String error;
            if (e instanceof UnknownHostException) {
                error = getString(R.string.default_request_error_unknown);
            } else if (e instanceof SocketTimeoutException) {
                error = getString(R.string.default_request_error_timeout);
            } else {
                error = getString(R.string.default_request_error_internal_server);
            }
            sendError(error);
        }

        @Override
        public void onNext(Response<TkpdResponse> responseData) {
            if (responseData.isSuccessful()) {

                TkpdResponse response = responseData.body();
                if (!response.isError()) {
                    JSONObject result = response.getJsonData();

                    switch (type) {
                        case ACTION_CHANGE_PASSWORD:
                            if(result.optString("is_success").equals("1")) {
                                sendResult(response.getStatusMessages().get(0), SUCCESS_CHANGE_PASSWORD);
                            }else{
                                sendResult(response.getStatusMessages().get(0), ERROR_CHANGE_PASSWORD);
                            }
                            break;

                    }
                } else {
                    String error = responseData.body().getErrorMessages().get(0);
                    switch (type){
                        case ACTION_CHANGE_PASSWORD:
                            sendResult(error,ERROR_CHANGE_PASSWORD);
                            break;
                    }
                }
            }else {
                new ErrorHandler(new ErrorListener() {
                    @Override
                    public void onUnknown() {
                        sendError(getString(R.string.default_request_error_unknown));
                    }

                    @Override
                    public void onTimeout() {
                        sendError(getString(R.string.default_request_error_timeout));
                    }

                    @Override
                    public void onServerError() {
                        sendError(getString(R.string.default_request_error_internal_server));
                    }

                    @Override
                    public void onBadRequest() {
                        sendError(getString(R.string.default_request_error_bad_request));
                    }

                    @Override
                    public void onForbidden() {
                        sendError(getString(R.string.default_request_error_forbidden_auth));
                    }
                }, responseData.code());
            }
        }
        private void sendError(String error){
            sendResult(error,ERROR_CHANGE_PASSWORD);
        }

        private void sendResult(String result, int status){
            resultData.putString(EXTRA_RESULT, result);
            receiver.send(status, resultData);
        }

    }
}
