package com.example.tokopedia_deeplink_annotation

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class TokopediaAppLinks(
    val value: Array<TokopediaAppLink>
)
