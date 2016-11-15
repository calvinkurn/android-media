package com.tokopedia.core.session.intentservice;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.tokopedia.core.R;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.service.constant.DownloadServiceConstant;
import com.tokopedia.core.session.presenter.Login;
import com.tokopedia.core.session.subscriber.BaseAccountSubscriber;
import com.tokopedia.core.util.SessionHandler;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ResetPasswordService extends IntentService implements DownloadServiceConstant{

    public static final String TAG = "ResetPasswordService";

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private ResultReceiver receiver;
    private SessionHandler sessionHandler;

    public ResetPasswordService() {
        super("ResetPasswordService");
    }

    public static void startDownload(Context context, ResetPasswordResultReceiver receiver, Bundle bundle, int type) {
        Intent intent = new Intent(Intent.ACTION_SYNC, null, context, ResetPasswordService.class);
        if (receiver != null)
            intent.putExtra(DownloadService.RECEIVER, receiver);

        intent.putExtra(TYPE, type);

        switch (type) {
            case RESET_PASSWORD:
                intent.putExtra(Login.EMAIL , bundle.getString("email"));
                break;
        }
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        receiver = intent.getParcelableExtra(RECEIVER);
        int type = intent.getIntExtra(TYPE, INVALID_TYPE);
        sessionHandler = new SessionHandler(getApplicationContext());

        Bundle bundle;
        AccountsService accountsService;
        Map<String, String> params;

        switch(type){
            case RESET_PASSWORD:
                params = new HashMap<>();
                params = AuthUtil.generateParams(getApplicationContext(),params);
                params.put(Login.EMAIL, intent.getStringExtra(Login.EMAIL));

                bundle = new Bundle();
                bundle.putString(AccountsService.AUTH_KEY, AuthUtil.KEY.KEY_WSV4);
                bundle.putBoolean(AccountsService.USING_HMAC, true);

                accountsService = new AccountsService(bundle);
                accountsService.getApi().resetPassword(params)
                        .subscribeOn(Schedulers.immediate())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Subscriber(getApplicationContext(),type,receiver,sessionHandler));
                break;
        }
    }

    private class Subscriber extends BaseAccountSubscriber {

        public Subscriber(Context context, int type, ResultReceiver receiver, SessionHandler sessionHandler) {
            super(context, type, receiver, sessionHandler);
        }

        public void onSuccessResponse(JSONObject jsonObject) {
            Bundle result;
            switch (type) {
                case RESET_PASSWORD :
                    result = new Bundle();
                    result.putInt(TYPE, type);
                    if (jsonObject.optInt("is_success", 0) == 1) {
                        receiver.send(STATUS_FINISHED, result);
                    } else {
                        sendMessageErrorToReceiver(context.getString(R.string.default_request_error_unknown));
                    }break;

            }
        }
    }
}
