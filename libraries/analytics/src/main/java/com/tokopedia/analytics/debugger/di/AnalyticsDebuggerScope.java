package com.tokopedia.analytics.debugger.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * @author okasurya on 5/17/18.
 */

@Scope
@Retention(RetentionPolicy.CLASS)
public @interface AnalyticsDebuggerScope {
}
