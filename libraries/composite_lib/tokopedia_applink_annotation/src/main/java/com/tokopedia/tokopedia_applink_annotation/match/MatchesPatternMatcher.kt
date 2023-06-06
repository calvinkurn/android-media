package com.tokopedia.tokopedia_applink_annotation.match

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class MatchesPatternMatcher(
    val value: Array<MatchPatternMatcher>
)
