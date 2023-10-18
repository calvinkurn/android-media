package com.tokopedia.tokopedia_applink_annotation.host

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@Repeatable
@java.lang.annotation.Repeatable(HostsMatcher::class)
annotation class HostMatcher(val matchesAppLink: String, val destinationAppLink: String = "")
