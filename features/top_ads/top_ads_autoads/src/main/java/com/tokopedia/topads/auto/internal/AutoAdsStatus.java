package com.tokopedia.topads.auto.internal;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.topads.auto.internal.AutoAdsStatus.STATUS_ACTIVE;
import static com.tokopedia.topads.auto.internal.AutoAdsStatus.STATUS_INACTIVE;
import static com.tokopedia.topads.auto.internal.AutoAdsStatus.STATUS_IN_PROGRESS_ACTIVE;
import static com.tokopedia.topads.auto.internal.AutoAdsStatus.STATUS_IN_PROGRESS_AUTOMANAGE;
import static com.tokopedia.topads.auto.internal.AutoAdsStatus.STATUS_IN_PROGRESS_INACTIVE;
import static com.tokopedia.topads.auto.internal.AutoAdsStatus.STATUS_NOT_DELIVERED;

/**
 * Author errysuprayogi on 14,May,2019
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({STATUS_INACTIVE, STATUS_IN_PROGRESS_ACTIVE, STATUS_IN_PROGRESS_INACTIVE, STATUS_IN_PROGRESS_AUTOMANAGE, STATUS_ACTIVE, STATUS_NOT_DELIVERED})
public @interface AutoAdsStatus {
    int STATUS_INACTIVE = 100;
    int STATUS_IN_PROGRESS_ACTIVE = 200;
    int STATUS_IN_PROGRESS_AUTOMANAGE = 300;
    int STATUS_IN_PROGRESS_INACTIVE = 400;
    int STATUS_ACTIVE = 500;
    int STATUS_NOT_DELIVERED = 600;
}
