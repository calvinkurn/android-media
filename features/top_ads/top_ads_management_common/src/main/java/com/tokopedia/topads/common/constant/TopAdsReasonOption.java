package com.tokopedia.topads.common.constant;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.topads.common.constant.TopAdsReasonOption.ELIGIBLE;
import static com.tokopedia.topads.common.constant.TopAdsReasonOption.INSUFFICIENT_CREDIT;
import static com.tokopedia.topads.common.constant.TopAdsReasonOption.NOT_ELIGIBLE;

/**
 * Created by hadi.putra on 26/04/18.
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({ELIGIBLE, NOT_ELIGIBLE, INSUFFICIENT_CREDIT})
public @interface TopAdsReasonOption {
    String ELIGIBLE = "eligible";
    String NOT_ELIGIBLE = "not_eligible";
    String INSUFFICIENT_CREDIT = "insufficient_credit";

}
