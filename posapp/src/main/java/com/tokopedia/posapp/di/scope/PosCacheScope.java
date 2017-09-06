package com.tokopedia.posapp.di.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by okasurya on 9/5/17.
 */

@Scope
@Retention(RetentionPolicy.CLASS)
public @interface PosCacheScope {
}
