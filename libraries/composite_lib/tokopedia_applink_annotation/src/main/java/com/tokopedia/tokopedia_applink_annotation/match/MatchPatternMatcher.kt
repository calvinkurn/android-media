package com.tokopedia.tokopedia_applink_annotation.match

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@Repeatable
@java.lang.annotation.Repeatable(MatchesPatternMatcher::class)
annotation class MatchPatternMatcher(val matchesAppLink: String, val destinationAppLink: String = "")
