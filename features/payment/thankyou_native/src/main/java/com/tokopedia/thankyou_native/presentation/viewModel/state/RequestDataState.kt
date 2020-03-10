package com.tokopedia.thankyou_native.presentation.viewModel.state


import com.tokopedia.usecase.coroutines.Result

sealed class RequestDataState<out T: Any>
object Loading: RequestDataState<Nothing>()
data class Loaded<out T: Any>(val data: Result<T>): RequestDataState<T>()