package com.tokopedia.tokopedia_applink_annotation.start

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class StartsMatcher(
    val value: Array<StartMatcher>
)
