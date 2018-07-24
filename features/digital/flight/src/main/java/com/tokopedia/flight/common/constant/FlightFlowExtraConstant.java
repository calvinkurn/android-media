package com.tokopedia.flight.common.constant;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author by alvarisi on 12/5/17.
 */
@StringDef({FlightFlowExtraConstant.EXTRA_FLOW_DATA})
@Retention(RetentionPolicy.SOURCE)
public @interface FlightFlowExtraConstant {
    String EXTRA_FLOW_DATA = "EXTRA_FLOW_DATA";
}
