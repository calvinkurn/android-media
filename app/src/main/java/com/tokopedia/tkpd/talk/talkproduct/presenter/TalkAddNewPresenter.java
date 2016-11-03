package com.tokopedia.tkpd.talk.talkproduct.presenter;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.tkpd.talk.talkproduct.fragment.TalkAddNewFragment;
import com.tokopedia.tkpd.talk.talkproduct.model.AddNewTalkPass;

import java.util.Map;

/**
 * Created by stevenfredian on 8/2/16.
 */
public interface TalkAddNewPresenter {
    void send(Context activity, AddNewTalkPass param);

    void unSubscribe();
}
