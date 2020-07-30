package com.tokopedia.annotation.defaultvalues

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class DefaultValueDouble(val value: Double)