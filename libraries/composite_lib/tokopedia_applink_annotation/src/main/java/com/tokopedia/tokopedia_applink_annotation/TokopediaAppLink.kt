package com.tokopedia.tokopedia_applink_annotation

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@Repeatable
@java.lang.annotation.Repeatable(TokopediaAppLinks::class)
// if you want to use custom annotation, please leave the internalAppLink value as empty, otherwise it will always mapped to internalAppLink
annotation class TokopediaAppLink(
    val matchedAppLink: String ,
    val internalAppLink: String = "",
    val dlpLogic: String
)
