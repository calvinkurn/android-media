package com.tokopedia.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class BundleThis(val nameAsKey: Boolean = true, val defaultAll: Boolean = true)