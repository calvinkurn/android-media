package com.tokopedia.core.talk.talkproduct.intentservice;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.tokopedia.core.R;
import com.tokopedia.core.network.apiservices.kunyit.KunyitService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.talk.talkproduct.model.AddNewTalkPass;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TalkAddNewIntentService extends IntentService {

    public static final String EXTRA_BUNDLE = "EXTRA_BUNDLE";
    public static final String EXTRA_RECEIVER = "EXTRA_RECEIVER";
    public static final String EXTRA_RESULT = "EXTRA_RESULT";

    public static final String PARAM_ADD_TALK = "add_talk";

    KunyitService kunyitService;
    ResultReceiver receiver;

    public static final int STATUS_SUCCESS_ADD = 7813;
    public static final int STATUS_ERROR_ADD = 7823;

    public TalkAddNewIntentService() {
        super("TalkAddNewIntentService");
    }

    public static void startAction(Context context, Bundle param, TalkAddNewResultReceiver receiver) {
        Intent intent = new Intent(context, TalkAddNewIntentService.class);
        intent.putExtra(EXTRA_BUNDLE, param);
        intent.putExtra(EXTRA_RECEIVER, receiver);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra(EXTRA_BUNDLE);
            kunyitService = new KunyitService();
            receiver = intent.getParcelableExtra(EXTRA_RECEIVER);
            handleAddNewTalk(bundle, receiver);
        }
    }

    private void handleAddNewTalk(Bundle bundle, final ResultReceiver receiver) {
        AddNewTalkPass param = (AddNewTalkPass) bundle.get(PARAM_ADD_TALK);
        final Bundle resultData = new Bundle();
        resultData.putParcelable(PARAM_ADD_TALK, param);
        kunyitService.getApi().addProductTalk(AuthUtil.
                generateParams(getBaseContext(), param.getAddTalkNewPass()))
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber());
    }

    private class Subscriber extends rx.Subscriber<Response<TkpdResponse>> {
        final Bundle resultData = new Bundle();

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e("Add Product Talk", e.toString());
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
        public void onNext(Response<TkpdResponse> response) {
            if (response.isSuccessful()) {
                final TkpdResponse tkpdResponse = response.body();
                if (!tkpdResponse.isError()) {
                    JSONObject result = tkpdResponse.getJsonData();
                    sendResult(result.optString("is_success"), STATUS_SUCCESS_ADD);
                } else {
                    String error = tkpdResponse.getErrorMessages().get(0);
                    sendResult(error, STATUS_ERROR_ADD);
                }
            } else {
                new ErrorHandler(new ErrorListener() {
                    @Override
                    public void onUnknown() {
                        //context.getResources().getString(R.string.default_request_error_unknown)
                        sendError("Network Unknown Error!");
                    }

                    @Override
                    public void onTimeout() {
                        sendError("Network Timeout Error!");
                    }

                    @Override
                    public void onServerError() {
                        sendError("Network Internal Server Error!");
                    }

                    @Override
                    public void onBadRequest() {
                        sendError("Network Bad Request Error!");
                    }

                    @Override
                    public void onForbidden() {

                    }
                }, response.code());
            }
        }

        private void sendError(String error) {
            sendResult(error, STATUS_ERROR_ADD);
        }

        private void sendResult(String result, int status) {
            resultData.putString(EXTRA_RESULT, result);
            receiver.send(status, resultData);
        }
    }
}
