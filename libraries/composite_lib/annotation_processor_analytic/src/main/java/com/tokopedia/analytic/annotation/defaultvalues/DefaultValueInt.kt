package com.tokopedia.annotation.defaultvalues

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class DefaultValueInt(val value: Int)