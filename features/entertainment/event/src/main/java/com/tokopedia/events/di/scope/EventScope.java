package com.tokopedia.events.di.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by ashwanityagi on 03/11/17.
 */

@Scope
@Retention(RetentionPolicy.CLASS)
public @interface EventScope {
}
