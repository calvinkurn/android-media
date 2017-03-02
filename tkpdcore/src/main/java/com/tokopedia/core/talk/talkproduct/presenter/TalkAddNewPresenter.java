package com.tokopedia.core.talk.talkproduct.presenter;

import android.content.Context;

import com.tokopedia.core.talk.talkproduct.model.AddNewTalkPass;

/**
 * Created by stevenfredian on 8/2/16.
 */
public interface TalkAddNewPresenter {
    void send(Context activity, AddNewTalkPass param);

    void unSubscribe();
}
