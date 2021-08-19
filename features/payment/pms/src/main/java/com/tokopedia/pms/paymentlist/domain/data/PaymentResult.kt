package com.tokopedia.pms.paymentlist.domain.data


sealed class PaymentResult<out T: Any>
data class Success<out T: Any>(val data: T): PaymentResult<T>()
data class Fail(val throwable: Throwable): PaymentResult<Nothing>()
object EmptyState: PaymentResult<Nothing>()
object LoadingState: PaymentResult<Nothing>()
object ProgressState: PaymentResult<Nothing>()