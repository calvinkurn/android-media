package com.tokopedia.test.application.annotations

import java.lang.annotation.Inherited

/**
 * UiAnalyticsTest must only be used when writing test
 * for GTM, Appsflyer or BranchIO instrumentation test
 * */
@Retention
@Inherited
annotation class UiAnalyticsTest