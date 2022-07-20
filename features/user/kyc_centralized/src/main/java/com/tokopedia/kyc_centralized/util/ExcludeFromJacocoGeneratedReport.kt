package com.tokopedia.kyc_centralized.util

/**
 * Used to exclude method or function from jacoco generated report
 *
 * Warning :
 * Just method or function that no need or not important to be included on test
 * */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ExcludeFromJacocoGeneratedReport