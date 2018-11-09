package com.tokopedia.events.view.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.events.EventModuleRouter;
import com.tokopedia.events.view.activity.EventBaseActivity;

/**
 * Created by pranaymohapatra on 07/07/18.
 */

public class FinishActivityReceiver extends BroadcastReceiver {
    EventBaseActivity mActivity;

    public FinishActivityReceiver(EventBaseActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(EventModuleRouter.ACTION_CLOSE_ACTIVITY)) {
            if (mActivity != null) {
                mActivity.finish();
                mActivity = null;
            }
        }
    }
}
