package com.tokopedia.core.talk.inboxtalk.interactor;

import android.content.Context;

import com.tokopedia.core.talk.inboxtalk.model.InboxTalkListModel;
import com.tokopedia.core.util.NewPagingHandler;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by stevenfredian on 4/5/16.
 */
public interface InboxTalkRetrofitInteractor {
    void getInboxTalk(Context context, Map<String, String> param, GetInboxTalkListener getInboxTalkListener);

    InboxTalkListModel parseList(JSONObject result);

    void unSubscribe();

    NewPagingHandler.PagingBean parsePaging(JSONObject result);

//    void followTalk(Context baseContext, Map<String, String> paramFollow, FollowListener followListener);
//
//    void deleteTalk(Context baseContext, Map<String, String> param, DeleteListener listener);
//
//    void reportTalk(Context baseContext, Map<String, String> param, ReportListener listener);

    interface GetInboxTalkListener{
        void onError(String error);

        void onThrowable(Throwable e);

        void onTimeout();

        void onSuccess(JSONObject result);
    }

    interface FollowListener{
        void onError(String error);

        void onThrowable(Throwable e);

        void onTimeout();

        void onSuccess(JSONObject result);
    }

    interface DeleteListener{
        void onError(String error);

        void onThrowable(Throwable e);

        void onTimeout();

        void onSuccess(JSONObject result);
    }

    interface ReportListener{
        void onError(String error);

        void onThrowable(Throwable e);

        void onTimeout();

        void onSuccess(JSONObject result);
    }
}
