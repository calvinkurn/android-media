package com.tokopedia.contactus.orderquery.view.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.contactus.ContactUsModuleRouter;
import com.tokopedia.contactus.createticket.ContactUsConstant;

/**
 * Created by pranaymohapatra on 07/07/18.
 */

public class FinishActivityReceiver extends BroadcastReceiver {
    BaseSimpleActivity mActivity;

    public FinishActivityReceiver(BaseSimpleActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == ContactUsConstant.ACTION_CLOSE_ACTIVITY) {
            if (mActivity != null) {
                mActivity.finish();
                mActivity = null;
            }
        }
    }
}
