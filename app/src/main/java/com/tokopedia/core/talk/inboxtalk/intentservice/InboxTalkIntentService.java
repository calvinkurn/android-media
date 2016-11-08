package com.tokopedia.core.talk.inboxtalk.intentservice;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.tokopedia.core.R;
import com.tokopedia.core.network.apiservices.kunyit.KunyitService;
import com.tokopedia.core.network.apiservices.product.TalkActService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.talkview.model.TalkPass;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by stevenfredian on 4/12/16.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class InboxTalkIntentService extends IntentService {

    public static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final String EXTRA_BUNDLE = "EXTRA_BUNDLE";
    public static final String EXTRA_RECEIVER = "EXTRA_RECEIVER";
    public static final String EXTRA_RESULT = "EXTRA_RESULT";


    TalkActService talkActService;
    KunyitService kunyitService;
    ResultReceiver receiver;

    public static final int ACTION_FOLLOW = 1;
    public static final int ACTION_DELETE = 2;
    public static final int ACTION_REPORT = 3;

    public static final String PARAM_FOLLOW = "follow";
    public static final String PARAM_DELETE = "delete";
    public static final String PARAM_REPORT = "report";

    private static final int STATUS_ERROR = 20;

    public static final int STATUS_SUCCESS_FOLLOW = 11;
    public static final int STATUS_ERROR_FOLLOW = 21;

    public static final int STATUS_SUCCESS_DELETE = 12;
    public static final int STATUS_ERROR_DELETE = 22;

    public static final int STATUS_SUCCESS_REPORT = 13;
    public static final int STATUS_ERROR_REPORT = 23;


    public InboxTalkIntentService() {
        super("InboxTalk");
    }

    public static void startAction(Context context, Bundle param, InboxTalkResultReceiver receiver, int type) {
        Intent intent = new Intent(context, InboxTalkIntentService.class);
        intent.putExtra(EXTRA_TYPE, type);
        intent.putExtra(EXTRA_BUNDLE, param);
        intent.putExtra(EXTRA_RECEIVER, receiver);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            int action = intent.getIntExtra(EXTRA_TYPE, 0);
            Bundle bundle = intent.getBundleExtra(EXTRA_BUNDLE);
            talkActService = new TalkActService();
            kunyitService = new KunyitService();
            receiver = intent.getParcelableExtra(EXTRA_RECEIVER);

            switch (action) {
                case ACTION_FOLLOW:
                    handleFollow(bundle, receiver);
                    break;
                case ACTION_DELETE:
                    handleDelete(bundle, receiver);
                    break;
                case ACTION_REPORT:
                    handleReport(bundle, receiver);
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown Action");
            }
        }
    }

    private void handleFollow(Bundle bundle, final ResultReceiver receiver) {
        TalkPass param = (TalkPass) bundle.get(PARAM_FOLLOW);
        final Bundle resultData = new Bundle();
        resultData.putParcelable(PARAM_FOLLOW, param);
        talkActService.getApi().followProductTalk(AuthUtil.
                generateParams(getBaseContext(), param.getParamFollow()))
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber(ACTION_FOLLOW));
    }

    private void handleDelete(Bundle bundle, final ResultReceiver receiver) {
        TalkPass param = (TalkPass) bundle.get(PARAM_DELETE);
        final Bundle resultData = new Bundle();
        resultData.putParcelable(PARAM_DELETE, param);
        talkActService.getApi().deleteProductTalk(AuthUtil.
                generateParams(getBaseContext(), param.getParamDelete()))
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber(ACTION_DELETE));
    }

    private void handleReport(Bundle bundle, final ResultReceiver receiver) {
        TalkPass param = (TalkPass) bundle.get(PARAM_REPORT);
        final Bundle resultData = new Bundle();
        resultData.putParcelable(PARAM_REPORT, param);
        talkActService.getApi().reportProductTalk(AuthUtil.
                generateParams(getBaseContext(), param.getParamReport()))
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber(ACTION_REPORT));

    }

    private class Subscriber extends rx.Subscriber<Response<TkpdResponse>> {
        int type;
        final Bundle resultData = new Bundle();

        public Subscriber(int type) {
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
                        case ACTION_FOLLOW:
                            sendResult(result.optString("is_success"), STATUS_SUCCESS_FOLLOW);
                            break;
                        case ACTION_DELETE:
                            sendResult(result.optString("is_success"), STATUS_SUCCESS_DELETE);
                            break;
                        case ACTION_REPORT:
                            sendResult(result.optString("is_success"), STATUS_SUCCESS_REPORT);
                            break;

                    }
                } else {
                    String error = responseData.body().getErrorMessages().get(0);
                    switch (type) {
                        case ACTION_FOLLOW:
                            sendResult(error, STATUS_ERROR_FOLLOW);
                            break;
                        case ACTION_DELETE:
                            sendResult(error, STATUS_ERROR_DELETE);
                            break;
                        case ACTION_REPORT:
                            sendResult(error, STATUS_ERROR_REPORT);
                            break;

                    }
                }
            } else {
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

        private void sendError(String error) {
            sendResult(error, type + 20);
        }

        private void sendResult(String result, int status) {
            resultData.putString(EXTRA_RESULT, result);
            receiver.send(status, resultData);
        }

    }
}
