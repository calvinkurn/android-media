package com.tokopedia.tokopedia_applink_annotation.host

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class HostsMatcher(
    val value: Array<HostMatcher>
)
