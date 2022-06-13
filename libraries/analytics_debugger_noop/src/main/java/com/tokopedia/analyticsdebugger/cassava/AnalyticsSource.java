package com.tokopedia.analyticsdebugger.cassava;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.analyticsdebugger.cassava.AnalyticsSource.ERROR;
import static com.tokopedia.analyticsdebugger.cassava.AnalyticsSource.GTM;
import static com.tokopedia.analyticsdebugger.cassava.AnalyticsSource.ALL;
import static com.tokopedia.analyticsdebugger.cassava.AnalyticsSource.APPS_FLYER;
import static com.tokopedia.analyticsdebugger.cassava.AnalyticsSource.BRANCH_IO;
import static com.tokopedia.analyticsdebugger.cassava.AnalyticsSource.LEGACY_GTM;
import static com.tokopedia.analyticsdebugger.cassava.AnalyticsSource.OTHER;

@Retention(RetentionPolicy.SOURCE)
@StringDef({GTM, LEGACY_GTM, BRANCH_IO, APPS_FLYER, ERROR, OTHER, ALL})
public @interface AnalyticsSource {
    String GTM = "gtm";
    String LEGACY_GTM = "legacy_gtm";
    String BRANCH_IO = "branch_io";
    String APPS_FLYER = "apps_flyer";
    String ERROR = "error";
    String OTHER = "other";
    String ALL = "";
}