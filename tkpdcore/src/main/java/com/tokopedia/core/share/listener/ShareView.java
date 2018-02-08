package com.tokopedia.core.share.listener;

import android.app.Activity;

/**
 * Created by alvarisi on 6/17/2016.
 */
public interface ShareView {
    void showDialogShareFb(String shortUrl);

    Activity getActivity();
}
