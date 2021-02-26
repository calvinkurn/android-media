package com.tokopedia.analyticsdebugger;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.analyticsdebugger.AnalyticsSource.ERROR;
import static com.tokopedia.analyticsdebugger.AnalyticsSource.GTM;
import static com.tokopedia.analyticsdebugger.AnalyticsSource.ALL;
import static com.tokopedia.analyticsdebugger.AnalyticsSource.APPS_FLYER;
import static com.tokopedia.analyticsdebugger.AnalyticsSource.BRANCH_IO;

@Retention(RetentionPolicy.SOURCE)
@StringDef({GTM, BRANCH_IO, APPS_FLYER, ERROR, ALL})
public @interface AnalyticsSource {
    String GTM = "gtm";
    String BRANCH_IO = "branch_io";
    String APPS_FLYER = "apps_flyer";
    String ERROR = "error";
    String ALL = "";
}