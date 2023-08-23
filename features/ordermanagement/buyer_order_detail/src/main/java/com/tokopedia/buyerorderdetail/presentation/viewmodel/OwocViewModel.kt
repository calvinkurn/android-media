package com.tokopedia.buyerorderdetail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.buyerorderdetail.domain.usecases.GetBomGroupedOrderUseCase
import com.tokopedia.buyerorderdetail.presentation.model.OwocGroupedOrderWrapper
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OwocViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val getBomGroupedOrderUseCase: dagger.Lazy<GetBomGroupedOrderUseCase>
) : BaseViewModel(dispatcher.main) {

    private val _owocGroupedOrderWrapper: MutableLiveData<Result<OwocGroupedOrderWrapper>> =
        MutableLiveData()

    val owocGroupedOrderWrapper: LiveData<Result<OwocGroupedOrderWrapper>>
        get() = _owocGroupedOrderWrapper

    fun fetchBomGroupedOrder(txId: String, orderId: String) {
        launchCatchError(block = {
            val response = withContext(dispatcher.io) {
                getBomGroupedOrderUseCase.get().execute(txId, orderId)
            }
            _owocGroupedOrderWrapper.value = Success(response)
        }, onError = {
                _owocGroupedOrderWrapper.value = Fail(it)
            })
    }
}
