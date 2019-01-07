package com.tokopedia.common.travel.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by nabillasabbaha on 21/11/18.
 */
@IntDef({TravelPlatformType.TRAIN, TravelPlatformType.FLIGHT})
@Retention(RetentionPolicy.SOURCE)
public @interface TravelPlatformType {
    public static final int TRAIN = 1;
    public static final int FLIGHT = 2;
}
