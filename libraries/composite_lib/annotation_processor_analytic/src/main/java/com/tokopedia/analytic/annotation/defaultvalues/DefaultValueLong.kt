package com.tokopedia.annotation.defaultvalues

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class DefaultValueLong(val value: Long)