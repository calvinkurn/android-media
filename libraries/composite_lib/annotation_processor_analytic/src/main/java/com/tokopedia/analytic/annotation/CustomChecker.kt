package com.tokopedia.analytic.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
annotation class CustomChecker(
        val checkerClass: KClass<*>,
        val level: Level,
        vararg val functionName: String
)