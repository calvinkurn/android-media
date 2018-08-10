package com.tokopedia.train.common.util;

import android.app.Activity;
import android.content.Intent;

public class TrainFlowUtil {
    public TrainFlowUtil() {
    }

    public void actionSetResultAndClose(Activity activity, Intent intent, int status) {
        intent.putExtra(TrainFlowExtraConstant.EXTRA_FLOW_DATA, status);
        activity.setResult(Activity.RESULT_CANCELED, intent);
        activity.finish();
    }
}
