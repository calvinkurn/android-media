package com.tokopedia.product.detail.estimasiongkir.view.viewmodel

import com.tokopedia.usecase.coroutines.Result

sealed class RatesEstimationDetailState<out T: Any>
object Shimmering: RatesEstimationDetailState<Nothing>()
data class Loaded<out T: Any>(val data: Result<T>): RatesEstimationDetailState<T>()