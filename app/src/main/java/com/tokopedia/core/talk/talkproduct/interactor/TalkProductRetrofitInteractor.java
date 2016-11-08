package com.tokopedia.core.talk.talkproduct.interactor;

import android.content.Context;

import com.tokopedia.core.talk.talkproduct.model.TalkProductModel;
import com.tokopedia.core.util.NewPagingHandler;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by stevenfredian on 4/5/16.
 */
public interface TalkProductRetrofitInteractor {
    void getTalkProduct(Context context, Map<String, String> param, GetTalkProductListener getTalkProductListener);

    TalkProductModel parse(JSONObject result);

    NewPagingHandler.PagingBean parsePaging(JSONObject result);

    void unSubscribe();

    interface GetTalkProductListener{
        void onSuccess(TalkProductModel result, NewPagingHandler.PagingBean pagingBean);

        void onError(String s);

        void onTimeout();

        void onThrowable(Throwable e);
    }
}
