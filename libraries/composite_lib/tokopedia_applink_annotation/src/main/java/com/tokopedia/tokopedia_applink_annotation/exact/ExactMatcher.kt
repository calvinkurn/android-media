package com.tokopedia.tokopedia_applink_annotation.exact

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@Repeatable
@java.lang.annotation.Repeatable(ExactsMatcher::class)
annotation class ExactMatcher(val matchesAppLink: String, val destinationAppLink: String = "")
