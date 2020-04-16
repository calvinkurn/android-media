package com.tokopedia.payment.router

/**
 * @author anggaprasetiyo on 7/11/17.
 */
interface IPaymentModuleRouter {
    val baseUrlDomainPayment: String?
    fun getGeneratedOverrideRedirectUrlPayment(originUrl: String): String?
    fun getGeneratedOverrideRedirectHeaderUrlPayment(originUrl: String): Map<String, String>
    val enableFingerprintPayment: Boolean
}