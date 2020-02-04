package com.tokopedia.flight.common.util;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.flight.common.constant.FlightFlowExtraConstant;

/**
 * Created by alvarisi on 12/5/17.
 */

public class FlightFlowUtil {
    public static void actionSetResultAndClose(Activity activity, Intent intent, int status) {
        intent.putExtra(FlightFlowExtraConstant.EXTRA_FLOW_DATA, status);
        activity.setResult(Activity.RESULT_CANCELED, intent);
        activity.finish();
    }
}
