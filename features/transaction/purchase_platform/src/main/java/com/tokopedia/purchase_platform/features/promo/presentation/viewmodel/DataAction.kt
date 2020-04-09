package com.tokopedia.purchase_platform.features.promo.presentation.viewmodel

sealed class Action<out T: Any>
data class Insert<out T: Any>(val data: T): Action<T>()
data class Update<out T: Any>(val data: T): Action<T>()
data class Delete<out T: Any>(val data: T): Action<T>()