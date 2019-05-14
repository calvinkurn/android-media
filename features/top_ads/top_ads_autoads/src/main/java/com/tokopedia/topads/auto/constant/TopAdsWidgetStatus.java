package com.tokopedia.topads.auto.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.topads.auto.constant.TopAdsWidgetStatus.STATUS_ACTIVE;
import static com.tokopedia.topads.auto.constant.TopAdsWidgetStatus.STATUS_INACTIVE;
import static com.tokopedia.topads.auto.constant.TopAdsWidgetStatus.STATUS_IN_PROGRESS;
import static com.tokopedia.topads.auto.constant.TopAdsWidgetStatus.STATUS_NOT_DELIVERED;

/**
 * Author errysuprayogi on 14,May,2019
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({STATUS_INACTIVE, STATUS_IN_PROGRESS, STATUS_ACTIVE, STATUS_NOT_DELIVERED})
public @interface TopAdsWidgetStatus {
    int STATUS_INACTIVE = 100;
    int STATUS_IN_PROGRESS = 200;
    int STATUS_ACTIVE = 300;
    int STATUS_NOT_DELIVERED = 400;
}
