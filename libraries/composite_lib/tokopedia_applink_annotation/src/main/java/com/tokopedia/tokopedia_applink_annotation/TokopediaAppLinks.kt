package com.tokopedia.tokopedia_applink_annotation

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class TokopediaAppLinks(
    val value: Array<TokopediaAppLink>
)
