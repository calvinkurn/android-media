package com.tokopedia.flight.airport.di

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

import javax.inject.Scope

/**
 * Created by zulfikarrahman on 10/24/17.
 */

@Scope
@Retention(RetentionPolicy.CLASS)
internal annotation class FlightAirportScope
