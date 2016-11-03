package com.tokopedia.tkpd.talk.talkproduct.listener;

import android.app.Activity;

/**
 * Created by stevenfredian on 8/2/16.
 */
public interface TalkAddNewView {
    void onSuccessAdd();

    void onErrorAdd(String error);

    Activity getActivity();
}
