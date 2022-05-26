package com.tokopedia.tokomember_seller_dashboard.util

sealed class TmCouponResponseState<out T: Any> {
    object TmCouponLoading : TmCouponResponseState<Nothing>()
    class TmCouponSuccess<T: Any>(val data: T): TmCouponResponseState<T>()
    class TmCouponError(val throwable: Throwable,
                       val errorType: String? = null): TmCouponResponseState<Nothing>()
}