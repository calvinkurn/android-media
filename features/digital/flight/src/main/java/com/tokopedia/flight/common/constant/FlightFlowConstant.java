package com.tokopedia.flight.common.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author by alvarisi on 12/5/17.
 */
@IntDef({FlightFlowConstant.PRICE_CHANGE, FlightFlowConstant.EXPIRED_JOURNEY})
@Retention(RetentionPolicy.SOURCE)
public @interface FlightFlowConstant {
    int PRICE_CHANGE = 1;
    int EXPIRED_JOURNEY = 2;
}
