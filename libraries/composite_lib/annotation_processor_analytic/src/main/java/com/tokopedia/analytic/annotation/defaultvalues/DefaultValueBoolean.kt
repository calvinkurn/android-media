package com.tokopedia.annotation.defaultvalues

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class DefaultValueBoolean(val value: Boolean)