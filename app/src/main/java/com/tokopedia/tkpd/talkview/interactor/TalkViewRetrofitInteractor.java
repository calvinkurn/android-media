package com.tokopedia.tkpd.talkview.interactor;

import android.content.Context;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by stevenfredian on 4/28/16.
 */
public interface TalkViewRetrofitInteractor {

    void getComment(Context context, Map<String, String> paramComment, String from, GetCommentListener getCommentListener);

    void reply(Context context, Map<String, String> param, AddCommentListener addCommentListener);

    void unSubscribe();

    interface AddCommentListener{
        void onSuccess(JSONObject Result);

        void onError(String error);

        void onThrowable(Throwable throwable);

        void onTimeout();
    }

    interface GetCommentListener{
        void onSuccess(JSONObject Result);

        void onError(String error);

        void onThrowable(Throwable throwable);

        void onTimeout();
    }
}
