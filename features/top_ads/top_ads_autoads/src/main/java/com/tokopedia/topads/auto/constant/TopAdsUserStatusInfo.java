package com.tokopedia.topads.auto.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.topads.auto.constant.TopAdsUserStatusInfo.AUTOADS_USER;
import static com.tokopedia.topads.auto.constant.TopAdsUserStatusInfo.MANUAL_USER;
import static com.tokopedia.topads.auto.constant.TopAdsUserStatusInfo.NO_ADS;
import static com.tokopedia.topads.auto.constant.TopAdsUserStatusInfo.NO_PRODUCT;

/**
 * Author errysuprayogi on 14,May,2019
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({NO_PRODUCT, NO_ADS, MANUAL_USER, AUTOADS_USER})
public @interface TopAdsUserStatusInfo {
    int NO_PRODUCT = 1;
    int NO_ADS = 2;
    int MANUAL_USER = 3;
    int AUTOADS_USER = 4;
}
