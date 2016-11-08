package com.tokopedia.core.home.util;

import android.util.Log;

import com.tokopedia.core.util.RetryHandler;

/**
 * Created by m.normansyah on 2/5/16.
 */
public class DefaultRetryListener implements RetryHandler.OnRetryListener{

    public static interface OnClickRetry{
        void onRetryFull();
        void onRetryFooter();
    }

    public static final int RETRY_FULL = 0;
    public static final int RETRY_FOOTER = 1;
    int type;
    OnClickRetry onClickRetry;

    public DefaultRetryListener(int type, OnClickRetry onClickRetry){
        this.type = type;
        this.onClickRetry = onClickRetry;
    }

    @Override
    public void onRetryCliked() {
        Log.d("Default Retry Listener", "onRetryCliked type "+type);
        switch (type){
            case RETRY_FULL:
                onClickRetry.onRetryFull();
                break;
            case RETRY_FOOTER:
                onClickRetry.onRetryFooter();
                break;
        }
    }
}
