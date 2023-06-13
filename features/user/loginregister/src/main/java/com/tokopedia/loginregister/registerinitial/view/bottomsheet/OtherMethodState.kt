package com.tokopedia.loginregister.registerinitial.view.bottomsheet

sealed class OtherMethodState<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : OtherMethodState<T>(data)
    class Loading<T> : OtherMethodState<T>()
    class Failed<T>(message: String?) : OtherMethodState<T>(message = message)
}