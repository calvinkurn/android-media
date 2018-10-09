package com.tokopedia.flight.cancellation.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * @author by furqan on 21/03/18.
 */

@Scope
@Retention(RetentionPolicy.CLASS)
public @interface FlightCancellationScope {
}
