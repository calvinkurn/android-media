package com.tokopedia.tokopedia_applink_annotation.start

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@Repeatable
@java.lang.annotation.Repeatable(StartsMatcher::class)
annotation class StartMatcher(val matchesAppLink: String, val destinationAppLink: String)
