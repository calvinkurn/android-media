package com.tokopedia.analytic.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class ErrorHandler(val errorHandler: KClass<*>)