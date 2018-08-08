package com.tokopedia.core.talkview.intentservice;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.tokopedia.core.R;
import com.tokopedia.core.talkview.interactor.TalkViewRetrofitInteractor;
import com.tokopedia.core.talkview.interactor.TalkViewRetrofitInteractorImpl;
import com.tokopedia.core.talkview.model.ReplyCommentPass;

import org.json.JSONObject;

/**
 * Created by stevenfredian on 4/29/16.
 */
public class TalkViewIntentService extends IntentService {

    public static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final String EXTRA_BUNDLE = "EXTRA_BUNDLE";
    public static final String EXTRA_RECEIVER = "EXTRA_RECEIVER";
    public static final String EXTRA_RESULT = "EXTRA_RESULT";

    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_ERROR = 2;

    public static final String PARAM_REPLY = "reply";
    TalkViewRetrofitInteractor facade;

    public TalkViewIntentService() {
        super("TalkViewIntentService");
        this.facade = TalkViewRetrofitInteractorImpl.createInstance();
    }

    public static void replyComment(Context context, Bundle param,
                                         TalkViewResultReceiver receiver, String type){
        Intent intent = new Intent(Intent.ACTION_SYNC, null, context,
                TalkViewIntentService.class);
        intent.putExtra(EXTRA_TYPE,type);
        intent.putExtra(EXTRA_BUNDLE,param);
        intent.putExtra(EXTRA_RECEIVER, receiver);
        context.startService(intent);
        Log.d("steven", "nembak");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent != null){
            String action = intent.getStringExtra(EXTRA_TYPE);
            Bundle bundle = intent.getBundleExtra(EXTRA_BUNDLE);
            ResultReceiver receiver = intent.getParcelableExtra(EXTRA_RECEIVER);

            switch (action){
                case PARAM_REPLY:
                    handleReply(bundle, receiver);
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown Action");
            }
        }
    }

    private void handleReply(Bundle bundle, final ResultReceiver receiver) {
        ReplyCommentPass param = (ReplyCommentPass) bundle.get(PARAM_REPLY);
        final Bundle resultData = new Bundle();
        resultData.putParcelable(PARAM_REPLY,param);
        facade.reply(getBaseContext(), param.getReplyParam(), new TalkViewRetrofitInteractor.AddCommentListener() {
            @Override
            public void onSuccess(JSONObject result) {
                resultData.putString(EXTRA_RESULT, getString(R.string.message_add_new_comment));
                receiver.send(STATUS_SUCCESS, resultData);
                Log.d("steven", "gotcha");
            }

            @Override
            public void onError(String error) {
                resultData.putString(EXTRA_RESULT, error);
                receiver.send(STATUS_ERROR, resultData);
            }

            @Override
            public void onThrowable(Throwable e) {
                resultData.putString(EXTRA_RESULT, getString(R.string.error_no_connection2));
                receiver.send(STATUS_ERROR, resultData);
            }

            @Override
            public void onTimeout() {
                resultData.putString(EXTRA_RESULT, getString(R.string.title_verification_timeout));
                receiver.send(STATUS_ERROR, resultData);
            }
        });
    }
}
