package com.tokopedia.tkpd.talk.talkproduct.interactor;

import android.content.Context;

import com.tokopedia.tkpd.talk.talkproduct.model.TalkProductModel;
import com.tokopedia.tkpd.util.NewPagingHandler;
import com.tokopedia.tkpd.util.PagingHandler;

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
