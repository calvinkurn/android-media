package com.tokopedia.flight.common.util

import android.app.Activity
import android.content.Intent
import com.tokopedia.flight.common.constant.FlightFlowExtraConstant

/**
 * Created by alvarisi on 12/5/17.
 */
object FlightFlowUtil {
    fun actionSetResultAndClose(activity: Activity, intent: Intent, status: Int) {
        intent.putExtra(FlightFlowExtraConstant.EXTRA_FLOW_DATA.value, status)
        activity.setResult(Activity.RESULT_CANCELED, intent)
        activity.finish()
    }
}