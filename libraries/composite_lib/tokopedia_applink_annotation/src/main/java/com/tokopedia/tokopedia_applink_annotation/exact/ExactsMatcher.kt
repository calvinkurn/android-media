package com.tokopedia.tokopedia_applink_annotation.exact

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class ExactsMatcher(
    val value: Array<ExactMatcher>
)
